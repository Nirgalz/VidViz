package com.vidviz.back.repository;

import com.vidviz.back.model.Folder;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FolderRepository extends CrudRepository<Folder, Long> {

    Folder findByName(String name);
}
