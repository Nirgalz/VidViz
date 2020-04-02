package com.vidviz.back.service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.core.io.FileSystemResource;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageServiceImpl implements FileStorageService {

    public static final String VIDEOSFOLDER ="videos/";
    private final Path root = Paths.get(VIDEOSFOLDER);


    @Override
    public void init() {
        try {
            if (Files.notExists(root)) {
                Files.createDirectory(root);
            }
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize folder for upload!");
        }
    }

    @Override
    public String getVideosfolder(){
        return VIDEOSFOLDER;
    }

    @Override
    public boolean isFolderExists(String name) {
        return Files.exists(Paths.get(VIDEOSFOLDER+name));
    }

    @Override
    public void save(MultipartFile file, String pageName) {
        try {
            Path folder = Paths.get(VIDEOSFOLDER + pageName);
            if (Files.notExists(folder)) {
                Files.createDirectory(folder);
            }
            Files.copy(file.getInputStream(), folder.resolve(file.getOriginalFilename()));
        } catch (Exception e) {
            throw new RuntimeException("Could not store the file. Error: " + e.getMessage());
        }
    }



    @Override
    public FileSystemResource load(String filename, String folderName) {
        Path folder = Paths.get(VIDEOSFOLDER + folderName);

        Path file = folder.resolve(filename);
        FileSystemResource resource = new FileSystemResource(file);

        if (resource.exists() || resource.isReadable()) {
            return resource;
        } else {
            throw new RuntimeException("Could not read the file!");
        }
    }

    @Override
    public void editFolderName(String oldName, String newName) throws IOException {

        Files.move(Paths.get(VIDEOSFOLDER + oldName), Paths.get(VIDEOSFOLDER + newName));
    }

    @Override
    public void deleteFolder(String folder) throws IOException {
        FileSystemUtils.deleteRecursively(Paths.get(VIDEOSFOLDER + folder));
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(root.toFile());
        init();
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.root, 1).filter(path -> !path.equals(this.root)).map(this.root::relativize);
        } catch (IOException e) {
            return Stream.<Path>empty();
        }
    }

    @Override
    public void deleteFile(String folderName, String fileName) throws IOException {
        Path file = Paths.get(VIDEOSFOLDER+folderName+"/"+fileName);
        Files.deleteIfExists(file);
    }

    @Override
    public String[] getFilesInFolder(String folderName) {
        File folder = new File(VIDEOSFOLDER+folderName);
        return folder.list();
    }
}