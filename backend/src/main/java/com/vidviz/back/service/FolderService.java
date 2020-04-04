package com.vidviz.back.service;

import com.vidviz.back.model.Folder;
import com.vidviz.back.repository.FolderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class FolderService {

    @Autowired
    private FolderRepository folderRepository;

    public Folder getFolderByName(String name) {
        return folderRepository.findByName(name);
    }

    public List<Folder> findAll() { return folderRepository.findAll(); }

    public List<Folder> findAllPaginated(Integer pageNo, Integer pageSize, String sortBy) {
        Pageable paging = PageRequest.of(pageNo, pageSize, Sort.by(sortBy));
        Page<Folder> pagedResult = folderRepository.findAll(paging);
        if(pagedResult.hasContent()) {
            return pagedResult.getContent();
        } else {
            return new ArrayList<Folder>();
        }
    }

    public Folder saveFolder(Folder folder) {
        return folderRepository.save(folder);
    }

    public void deleteFolder(String folder) {
        folderRepository.delete(folderRepository.findByName(folder));
    }
}
