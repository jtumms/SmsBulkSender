package com.tummsmedia.services;

import com.tummsmedia.entities.Post;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * Created by john.tumminelli on 10/29/16.
 */
public interface PostRepository extends PagingAndSortingRepository<Post, Integer> {
    public List<Post> findByIsSent(boolean isSent);



}
