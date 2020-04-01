package com.vidviz.back.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "folder")
@Table(name="folders")
public class Folder {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Date created;


    @OneToMany(
            mappedBy = "folder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<Video> videos = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }

    public int getNumberOfFiles() {
        return videos.size();
    }

    public void addAllFiles(List<Video> videos) {
        videos.addAll(videos);
    }

    public void addFile(Video video) {
        videos.add(video);
    }

    public List<Video> getVideos() {
        return videos;
    }

    public void removeFile(Video video) {
        videos.remove(video);
    }

    public void removeAllFiles(List<Video> videos) {
        videos.removeAll(videos);
    }

    public void emptyFileList() {
        videos.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
