package com.vidviz.back.service;

import com.vidviz.back.model.Video;
import com.vidviz.back.repository.VideoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VideoService {

    @Autowired
    VideoRepository videoRepository;

    public Video saveVideo(Video video) {
        return videoRepository.save(video);
    }

    public void createAllFiles(List<Video> videos) {
        videoRepository.saveAll(videos);
    }

    public Video findByName(String name) {
        return videoRepository.findByName(name);
    }

    public Video findByNameAndFolderName(String name, String folder){
        return videoRepository.findByNameAndFolderName(name, folder);
    }

    public Optional<Video> findById(Long id){
        return videoRepository.findById(id);
    }

    public void deleteVideo(Long id) {
        Optional<Video> video = videoRepository.findById(id);
        video.ifPresent(video1 -> {
            videoRepository.delete(video1);
        });

    }
}
