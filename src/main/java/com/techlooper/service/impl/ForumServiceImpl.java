package com.techlooper.service.impl;

import com.techlooper.model.Topic;
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

    @Value("${forum.daynhauhoc.apiEndpoint}")
    private String apiEndpoint;

    @Value("${forum.daynhauhoc.topicUrlPattern}")
    private String topicUrlPattern;

    @Override
    public TopicList getLatestTopics() {
        TopicList topicList = new TopicList();

        if (enableForumIntegration) {
            String latestTopicUrl = apiEndpoint + "/latest.json";
            ResponseEntity<TopicModel> responseEntity = restTemplate.getForEntity(latestTopicUrl, TopicModel.class);

            if (responseEntity != null) {
                topicList = responseEntity.getBody().getTopicList();
                buildTopicUrlPattern(topicList);
            }
        }

        return topicList;
    }

    private void buildTopicUrlPattern(TopicList topicList) {
        for (Topic topic : topicList.getTopics()) {
            topic.setUrl(String.format(topicUrlPattern, topic.getId()));
        }
    }

}
