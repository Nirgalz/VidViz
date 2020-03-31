package com.vidviz.back.service;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void init();

    public void save(MultipartFile file, String pageName);


    FileSystemResource load(String filename, String folderName);

    void deleteAll();

    Stream<Path> loadAll();

    void deleteFolder(String folder) throws IOException;

    void editFolderName(String oldName, String newName) throws IOException;

    void deleteFile(String folderName, String fileName) throws IOException;

    List<String> getNewFolders() throws IOException;

    void moveFolder(String folderName) throws IOException;

    String[] getFilesInFolder(String folderName);
}