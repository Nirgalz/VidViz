package com.vidviz.back.repository;

import com.vidviz.back.model.Video;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileRepository extends CrudRepository<Video, Long> {
    Video findByName(String name);
    Video findByNameAndFolderName(String name, String folder);
}
