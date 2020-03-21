package com.vidviz.back.service;

import com.vidviz.back.model.File;
import com.vidviz.back.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public File createFile(File file) {
        return fileRepository.save(file);
    }

    public void createAllFiles(List<File> files) {
        fileRepository.saveAll(files);
    }
}
