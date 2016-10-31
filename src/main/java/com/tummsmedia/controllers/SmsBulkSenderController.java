package com.tummsmedia.controllers;

import com.tummsmedia.entities.Post;
import com.tummsmedia.entities.User;
import com.tummsmedia.services.PostRepository;
import com.tummsmedia.services.UserRepository;
import com.tummsmedia.utilities.PasswordStorage;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.h2.tools.Server;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.servlet.http.HttpSession;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by john.tumminelli on 10/29/16.
 */
@Controller
public class SmsBulkSenderController {
    // Account Sid and Token at twilio.com/user/account
    public static final String ACCOUNT_SID = "ACdcf4e767d4ebe5e1ac57bff838a2557f";
    public static final String AUTH_TOKEN = "83051e053feeb53cd130dcf11ff3a6a0";


    @Autowired
    UserRepository users;

    @Autowired
    PostRepository posts;

    Server h2Server;

    @PostConstruct
    public void init() throws SQLException, InterruptedException {
        h2Server = Server.createWebServer().start();
        Thread smsThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

                    List<User> userList;
                    userList = (List<User>) users.findAll();

                    List<Post> postList;
                    postList = posts.findByIsSent(false);
                    if (postList != null) {
                        for (User user : userList) {
                            for (Post post : postList) {
                                String msgText = String.format("Subject: %s\nPost: %s", post.subject, post.content);
                                Message message = Message
                                        .creator(new PhoneNumber("+1" + user.phone), new PhoneNumber("+15162523511"),
                                                msgText)
                                        .create();
                                post.setSent(true);
                                posts.save(post);

                                System.out.println(message.getSid());
                            }
                        }
                    }
                    try {
                        Thread.sleep(60000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        //Start sms thread here. Set to check db every 60 seconds
        smsThread.start();
    }
    @PreDestroy
    public void destroy() {
        h2Server.stop();
    }

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String login() {
        return "index";
    }

    @RequestMapping(path = "/login", method = RequestMethod.POST)
    public String login(String username, String password, HttpSession session) throws Exception {
        User user = users.findFirstByName(username);
        if (user == null){
            return "redirect:/createuser";
        }
        else if (!PasswordStorage.verifyPassword(password, user.password)){
            throw new Exception("Wrong Password");
        }
        session.setAttribute("username", username);
        return "redirect:/home";
    }
    @RequestMapping(path = "/createuser", method = RequestMethod.GET)
    public String newUser() {
        return "createuser";
    }
    @RequestMapping(path = "/createuser", method = RequestMethod.POST)
    public String addUser(String username, String password, String passwordverify, String cellphone, HttpSession session) throws Exception {
        if (!password.equals(passwordverify)) {
            throw new Exception("Passwords do not agree!");
        }
        User user = new User(username, PasswordStorage.createHash(passwordverify), cellphone);
        users.save(user);
        session.setAttribute("username", username);
        return "redirect:/";
    }

    @RequestMapping(path = "/addpost", method = RequestMethod.POST)
    public String addPost(HttpSession session, String subject, String content) throws Exception {
        String name = (String) session.getAttribute("username");
        User user = users.findFirstByName(name);
        if (user == null){
            throw new Exception("Not logged in!");
        }
        Post post = new Post(subject, content, user, false);
        posts.save(post);
        return "redirect:/home";
    }
    @RequestMapping(path = "/home", method = RequestMethod.GET)
    public String home (Model model, Integer page){
        page = (page == null) ? 0 : page;
        PageRequest pr = new PageRequest(page, 10);
        Page<Post> postList;
        postList = posts.findAll(pr);

        model.addAttribute("posts", postList);
        model.addAttribute("nextPage", page + 1);
        model.addAttribute("showNext", postList.hasNext());
        model.addAttribute("prevPage", page -1);
        model.addAttribute("showPrev", postList.hasPrevious());

        return "home";
    }
    @RequestMapping(path = "/logout", method = RequestMethod.POST)
    public String logout(HttpSession session, User user) {
        session.invalidate();
        user = null;
        return "redirect:/";
    }

}
