package com.vidviz.back.model;

public class VideoFront {
    public Long id;
    public String fileName;
    public String url;
    public String jsonUrl;

    public VideoFront(Long id, String fileName, String url, String jsonUrl) {
        this.id = id;
        this.fileName = fileName;
        this.url = url;
        this.jsonUrl = jsonUrl;
    }
}
