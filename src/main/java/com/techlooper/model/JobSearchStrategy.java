package com.techlooper.model;

import com.techlooper.entity.JobEntity;
import com.techlooper.util.DataUtils;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by NguyenDangKhoa on 12/7/15.
 */
public abstract class JobSearchStrategy {

    public List<JobEntity> searchJob() {
        List<Object> allEntities = DataUtils.getAllEntities(getJobRepository(), getSearchQueryBuilder());
        return mapToJobEntities(allEntities);
    }

    protected List<JobEntity> mapToJobEntities(List<Object> allEntities) {
        List<JobEntity> jobEntities = new ArrayList<>();
        for (Object entity : allEntities) {
            JobEntity jobEntity = (JobEntity) entity;
            jobEntities.add(jobEntity);
        }
        return jobEntities;
    }

    protected abstract NativeSearchQueryBuilder getSearchQueryBuilder();

    protected abstract <T> ElasticsearchRepository<T, ?> getJobRepository();

}
