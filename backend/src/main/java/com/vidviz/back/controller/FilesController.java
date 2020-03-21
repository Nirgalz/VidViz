package com.vidviz.back.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.vidviz.back.model.File;
import com.vidviz.back.model.Folder;
import com.vidviz.back.model.FolderFront;
import com.vidviz.back.model.ResponseMessage;
import com.vidviz.back.service.FileService;
import com.vidviz.back.service.FileStorageService;
import com.vidviz.back.service.FolderService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;



@Controller
@CrossOrigin("http://localhost:8081")
public class FilesController {

    private static final Logger LOG = LoggerFactory.getLogger(FilesController.class);

    @Autowired
    FileStorageService storageService;

    @Autowired
    FolderService folderService;

    @Autowired
    FileService fileService;

    @PostMapping("api/upload")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile[] files, @RequestParam("pageName") String folderName) {
        String message = "";
        Folder folder = folderService.getFolderByName(folderName);
        if (folder == null) {
            folder = new Folder();
            folder.setName(folderName);
            folderService.createFolder(folder);
        }
        for (MultipartFile file : files) {
            try {
                File savedFile = new File();

                savedFile.setName(file.getOriginalFilename());
                savedFile.setFolder(folder);

                folder.addFile(fileService.createFile(savedFile));
                storageService.save(file, folderName);
                LOG.info("upload from client");
                message = "Uploaded the file successfully: " + file.getOriginalFilename();

            } catch (Exception e) {
                LOG.info("Could not upload the file: " + file.getOriginalFilename() + "!");
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
            }
        }
        return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
    }

    @GetMapping("api/folders")
    public ResponseEntity<List<FolderFront>> getListFolders() {
        List<Folder> folders = folderService.findAll();
        List<FolderFront> foldersFront = new ArrayList<>();
        for (Folder folder : folders) {
            foldersFront.add(new FolderFront(folder.getName(), folder.getNumberOfFiles()));
        }

        return ResponseEntity.status(HttpStatus.OK).body(foldersFront);
    }


//    @GetMapping("api/files")
//    public ResponseEntity<List<File>> getListFiles() {
//        List<File> files = storageService.loadAll().map(path -> {
//            String filename = path.getFileName().toString();
//            String url = MvcUriComponentsBuilder
//                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();
//
//            return new File(filename, url);
//        }).collect(Collectors.toList());
//
//        return ResponseEntity.status(HttpStatus.OK).body(files);
//    }

    @GetMapping("api/files/{filename:.+}")
    @ResponseBody
    public ResponseEntity<Resource> getFile(@PathVariable String filename) {
        Resource file = storageService.load(filename);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + file.getFilename() + "\"").body(file);
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