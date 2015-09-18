package com.techlooper.controller;

import com.techlooper.model.TopicList;
import com.techlooper.service.ForumService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class ForumController {

    @Resource
    private ForumService forumService;

    @RequestMapping(value = "/forum/latestTopic", method = RequestMethod.GET)
    public TopicList getLatestTopic() throws Exception {
        return forumService.getLatestTopics();
    }

}
