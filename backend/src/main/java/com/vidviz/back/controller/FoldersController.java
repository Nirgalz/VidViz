package com.vidviz.back.controller;

import com.vidviz.back.model.Folder;
import com.vidviz.back.model.ResponseMessage;
import com.vidviz.back.model.Video;
import com.vidviz.back.service.FileStorageService;
import com.vidviz.back.service.FolderService;
import com.vidviz.back.service.VideoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Stream;

@RestController
@CrossOrigin("http://localhost:8081")
public class FoldersController {

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private VideoService videoService;

    @Autowired
    private FolderService folderService;

    @GetMapping("api/action/scan")
    public ResponseEntity<ResponseMessage> scanFolders() {
        List<String> newFolders;
        List<Folder> dbFolders = folderService.findAll();
        Stream<Path> fileSystemFolders = storageService.loadAll();
        AtomicInteger newFolderCount = new AtomicInteger();
        int foldersDeleted = 0;
        for (Folder dbFolder : dbFolders) {
            if (!storageService.isFolderExists(dbFolder.getName())) {
                folderService.deleteFolder(dbFolder.getName());
                foldersDeleted += 1;
            }
        }

        fileSystemFolders.forEach(path -> {
            String folderName = path.getFileName().toString();
            if (folderService.getFolderByName(folderName) == null) {
                newFolderCount.addAndGet(1);
                Folder folder = new Folder();
                folder.setName(folderName);
                try {
                    BasicFileAttributes attr = Files.readAttributes(
                            Paths.get(storageService.getVideosfolder() + folderName), BasicFileAttributes.class
                    );
                    folder.setCreated(new Date(attr.creationTime().toMillis()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                folderService.saveFolder(folder);

                List<String> jsonFiles = new ArrayList<>();
                String[] files = storageService.getFilesInFolder(folderName);

                for (String fileName : files) {
                    if (fileName.endsWith(".json")) {
                        jsonFiles.add(fileName);
                    } else {
                        Video video = new Video();
                        video.setName(fileName);
                        video.setFolder(folder);

                        folder.addFile(videoService.saveVideo(video));
                    }
                }

                for (String fileName : jsonFiles) {
                    Video video = videoService.findByNameAndFolderName(fileName
                            .replace("jsonoutput", "ffmpegencodevideo")
                            .replace(".json", ".mp4"), folderName);
                    video.setJson(fileName);
                    videoService.saveVideo(video);
                }
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(
                new ResponseMessage("Updated : " + foldersDeleted + " folders were deleted and " + newFolderCount.get() + " folders were added."));
    }
}
