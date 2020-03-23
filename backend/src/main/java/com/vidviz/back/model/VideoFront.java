package com.vidviz.back.model;

public class VideoFront {
    public String fileName;
    public String url;
    public String jsonUrl;

    public VideoFront(String fileName, String url, String jsonUrl) {
        this.fileName = fileName;
        this.url = url;
        this.jsonUrl = jsonUrl;
    }
}
