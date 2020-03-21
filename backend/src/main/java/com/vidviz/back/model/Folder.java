package com.vidviz.back.model;

import org.springframework.data.annotation.Id;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity(name = "folder")
@Table(name="folders")
public class Folder {
    @javax.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;


    @OneToMany(
            mappedBy = "folder",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<File> files = new ArrayList<>();

    public void addAllFiles(List<File> files) {
        files.addAll(files);
    }

    public void addFile(File file) {
        files.add(file);
    }

    public void removeFile(File file) {
        files.remove(file);
    }

    public void removeAllFiles(List<File> files) {
        files.removeAll(files);
    }

    public void emptyFileList() {
        files.clear();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
