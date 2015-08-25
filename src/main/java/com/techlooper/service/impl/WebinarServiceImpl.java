package com.techlooper.service.impl;

import com.techlooper.dto.WebinarInfoDto;
import com.techlooper.entity.WebinarEntity;
import com.techlooper.repository.userimport.WebinarRepository;
import com.techlooper.service.WebinarService;
import org.dozer.Mapper;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class WebinarServiceImpl implements WebinarService {

    @Resource
    private WebinarRepository webinarRepository;

    @Resource
    private Mapper dozerMapper;

    @Override
    public List<WebinarInfoDto> listUpcomingWebinar() {
        List<WebinarInfoDto> upcomingWebinars = new ArrayList<>();
        NativeSearchQueryBuilder searchQueryBuilder = new NativeSearchQueryBuilder().withTypes("webinar");
        searchQueryBuilder.withQuery(QueryBuilders.rangeQuery("startDate").from("now"));
        searchQueryBuilder.withSort(SortBuilders.fieldSort("startDate").order(SortOrder.DESC));
        searchQueryBuilder.withPageable(new PageRequest(0, 3));

        List<WebinarEntity> webinarEntities = webinarRepository.search(searchQueryBuilder.build()).getContent();
        if (!webinarEntities.isEmpty()) {
            for (WebinarEntity webinarEntity : webinarEntities) {
                WebinarInfoDto webinarInfoDto = dozerMapper.map(webinarEntity, WebinarInfoDto.class);
                upcomingWebinars.add(webinarInfoDto);
            }
        }

        return upcomingWebinars;
    }
}
