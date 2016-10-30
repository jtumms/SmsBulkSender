package com.tummsmedia.controllers;

import com.tummsmedia.entities.Post;
import com.tummsmedia.entities.User;
import com.tummsmedia.services.PostRepository;
import com.tummsmedia.services.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;

/**
 * Created by john.tumminelli on 10/29/16.
 */

@RestController
public class SmsBulkSenderRestController {
    @Autowired
    UserRepository users;

    @Autowired
    PostRepository posts;

    @RequestMapping(path = "/users", method = RequestMethod.GET)
    public Iterable<User> getAllUsers(HttpSession session) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        if (user == null) {
            throw new Exception("Not logged in!");
        }
        return users.findAll();
    }
    @RequestMapping(path = "/posts.json", method = RequestMethod.GET)
    public Iterable<Post> getAllPosts(HttpSession session) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        if (user == null) {
            throw new Exception("Not logged in!");
        }
        return posts.findAll();
    }
    @RequestMapping(path = "/addpost.json", method = RequestMethod.POST)
    public Post addPost(Post post, HttpSession session) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        if (user == null) {
            throw new Exception("Not logged in!");
        }
        return posts.save(post);
    }

}
