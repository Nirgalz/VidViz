package com.vidviz.back.service;

import java.nio.file.Path;
import java.util.stream.Stream;

import com.vidviz.back.model.File;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

public interface FileStorageService {
    public void init();

    public void save(MultipartFile file, String pageName);


    FileSystemResource load(String filename, String folderName);

    public void deleteAll();

    public Stream<Path> loadAll();
}