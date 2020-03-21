package com.vidviz.back.service;

import com.vidviz.back.repository.FileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FileService {

    @Autowired
    FileRepository fileRepository;

}
