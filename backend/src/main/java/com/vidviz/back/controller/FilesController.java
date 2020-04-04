package com.vidviz.back.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

import com.vidviz.back.model.*;
import com.vidviz.back.service.VideoEncodingService;
import com.vidviz.back.service.VideoService;
import com.vidviz.back.service.FileStorageService;
import com.vidviz.back.service.FolderService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletResponse;


@RestController
@CrossOrigin("http://localhost:8081")
public class FilesController {

    private static final Logger LOG = LoggerFactory.getLogger(FilesController.class);

    @Autowired
    private FileStorageService storageService;

    @Autowired
    private FolderService folderService;

    @Autowired
    private VideoService videoService;

    private VideoEncodingService videoEncodingService = new VideoEncodingService();

    @PostMapping("api/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile[] files, @RequestParam("pageName") String folderName) {
        String message = "";
        Folder folder = folderService.getFolderByName(folderName);
        if (folder == null) {
            folder = new Folder();
            folder.setName(folderName);
            folderService.saveFolder(folder);
        }
        List<MultipartFile> jsonFiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                Video savedVideo = new Video();
                if (Objects.requireNonNull(file.getOriginalFilename()).endsWith(".json")) {
                    jsonFiles.add(file);
                } else {
                    savedVideo.setName(file.getOriginalFilename());
                    savedVideo.setFolder(folder);

                    folder.addFile(videoService.saveVideo(savedVideo));
                }
                LOG.info("upload from client");
                message = "Uploaded the file successfully: " + file.getOriginalFilename();

                storageService.save(file, folderName);

            } catch (Exception e) {
                LOG.info("Could not upload the file: " + file.getOriginalFilename() + "!");
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        for (MultipartFile jsonFile : jsonFiles) {
            Video video = videoService.findByNameAndFolderName(jsonFile.getOriginalFilename()
                    .replace("jsonoutput", "ffmpegencodevideo")
                    .replace(".json", ".mp4"), folderName);
            video.setJson(jsonFile.getOriginalFilename());
            videoService.saveVideo(video);

        }
        encodeFolder(folderName);

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }

    @GetMapping("api/folders")
    public ResponseEntity<List<FolderFront>> getListFolders() {
        List<Folder> folders = folderService.findAll();
        List<FolderFront> foldersFront = new ArrayList<>();
        for (Folder folder : folders) {
            foldersFront.add(new FolderFront(folder.getName(), folder.getNumberOfFiles(), folder.getCreated()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(foldersFront);
    }

    @GetMapping("api/action/file/openexplorer/{id:.+}")
    @ResponseBody
    public ResponseEntity<ResponseMessage> openFileInExplorer(@PathVariable Long id) {
        Optional<Video> video = videoService.findById(id);
        video.ifPresent(video1 -> {
            try {
                String absolutePath = storageService.getAbsolutePath(video1.getFolder().getName() + "/" + video1.getName());
                Runtime.getRuntime().exec("explorer.exe /select," + absolutePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });

        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("folder opened"));
    }

    @GetMapping("api/folder/{folder:.+}")
    @ResponseBody
    public ResponseEntity<List<VideoFront>> getFilesByFolder(@PathVariable String folder) {
        List<VideoFront> filesFront = new ArrayList<>();
        List<Video> videos = folderService.getFolderByName(folder).getVideos();
        for (Video video : videos) {
            filesFront.add(new VideoFront(video.getId(), video.getName(),
                    "http://localhost:8080/" + storageService.getVideosfolder() + folder + "/" + video.getName(),
                    "http://localhost:8080/" + storageService.getVideosfolder() + folder + "/" + video.getJson()));
        }
        if (Files.exists(Paths.get(storageService.getVideosfolder() +folder+"/thumbnails"))) {
            folder += "/thumbnails";
            for (Video video : videos) {
                filesFront.add(new VideoFront(video.getId(), video.getName(),
                        "http://localhost:8080/" + storageService.getVideosfolder() + folder + "/" + video.getName(),
                        "http://localhost:8080/" + storageService.getVideosfolder() + folder.replace("/thumbnails", "") + "/" + video.getJson()));
            }
        }

        return ResponseEntity.status(HttpStatus.OK).body(filesFront);
    }


    public void encodeFolder( String folder) {
        List<Video> videos = folderService.getFolderByName(folder).getVideos();
        for (Video video : videos) {
            try {
                videoEncodingService.encodeVideo(Paths.get(folder), video.getName());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @PostMapping("api/action/folders/rename/")
    public ResponseEntity<ResponseMessage> renameFolder(@RequestParam("oldName") String oldName, @RequestParam("newName") String newName) {

        try {
            storageService.editFolderName(oldName, newName);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Folder folder = folderService.getFolderByName(oldName);
        folder.setName(newName);
        folderService.saveFolder(folder);


        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(oldName + " has been renamed in " + newName));
    }

    @PostMapping("api/action/files/delete")
    public ResponseEntity<ResponseMessage> deleteFile(@RequestParam("id") Long id) {

        Optional<Video> video = videoService.findById(id);

        video.ifPresent(video1 -> {
            try {
                storageService.deleteFile(video1.getFolder().getName(), video1.getName());
                storageService.deleteFile(video1.getFolder().getName() + "/thumbnails/", video1.getName());
                if (video1.getJson() != null) {
                    storageService.deleteFile(video1.getFolder().getName(), video1.getJson());
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            videoService.deleteVideo(id);

        });


        LOG.info("file deleted");
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(" file has been deleted"));
    }

    @GetMapping("api/action/folders/delete/{folder:.+}")
    @ResponseBody
    public ResponseEntity<String> deleteFolder(@PathVariable String folder) {
        try {
            storageService.deleteFolder(folder);
        } catch (IOException e) {
            e.printStackTrace();
        }
        folderService.deleteFolder(folder);
        return ResponseEntity.status(HttpStatus.OK).body("folder deleted");
    }


    @GetMapping("api/files/{foldername:.+}/{filename:.+}")
    @ResponseBody
    public void getFile(@PathVariable String filename, @PathVariable String foldername, HttpServletResponse
            response) {
        try {
            InputStream inputStream = storageService.load(filename, foldername).getInputStream();
            IOUtils.copy(inputStream, response.getOutputStream());
            response.setContentType("video/mp4");
            response.flushBuffer();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @GetMapping("api/files/deleteall")
    @ResponseBody
    public ResponseEntity<ResponseMessage> deleteAll() {
        try {
            storageService.deleteAll();
            LOG.info("All files deleted");
            return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage("all files deleted"));
        } catch (Exception e) {
            LOG.info("Files could not be deleted");
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("Files could not be deleted"));
        }

    }
}