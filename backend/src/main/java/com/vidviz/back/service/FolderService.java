package com.vidviz.back.service;

import com.vidviz.back.model.Folder;
import com.vidviz.back.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    public Folder getFolderByName(String name) {
        return folderRepository.findByName(name);
    }

    public List<Folder> findAll() { return folderRepository.findAll(); }

    public Folder saveFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    public void deleteFolder(String folder) {
        folderRepository.delete(folderRepository.findByName(folder));
    }
}
