package com.vidviz.back.repository;

import com.vidviz.back.model.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FolderRepository extends PagingAndSortingRepository<Folder, Long> {

    Folder findByName(String name);

    List<Folder> findAll();
}
