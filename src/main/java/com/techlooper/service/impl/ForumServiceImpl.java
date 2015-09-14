package com.techlooper.service.impl;

import com.techlooper.model.TopicList;
import com.techlooper.model.TopicModel;
import com.techlooper.service.ForumService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class ForumServiceImpl implements ForumService {

    @Resource
    private RestTemplate restTemplate;

    @Value("${enableForumIntegration}")
    private boolean enableForumIntegration;

    @Value("${forum.daynhauhoc.latestTopicUrl}")
    private String latestTopicUrl;

    @Override
    public TopicList getLatestTopics() {
        TopicList topicList = new TopicList();

        if (enableForumIntegration) {
            ResponseEntity<TopicModel> responseEntity = restTemplate.getForEntity(latestTopicUrl, TopicModel.class);

            if (responseEntity != null) {
                topicList = responseEntity.getBody().getTopicList();
            }
        }

        return topicList;
    }

}
