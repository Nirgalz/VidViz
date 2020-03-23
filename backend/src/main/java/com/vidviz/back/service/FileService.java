package com.vidviz.back.service;

import com.vidviz.back.model.Video;
import com.vidviz.back.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

    public Video createFile(Video video) {
        return fileRepository.save(video);
    }

    public void createAllFiles(List<Video> videos) {
        fileRepository.saveAll(videos);
    }

    public Video findByName(String name) {
        return fileRepository.findByName(name);
    }
}
