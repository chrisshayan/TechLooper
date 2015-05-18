package com.techlooper.service.impl;

import com.techlooper.entity.SalaryReview;
import com.techlooper.repository.elasticsearch.SalaryReviewRepository;
import com.techlooper.service.JobQueryBuilder;
import com.techlooper.service.SalaryReviewService;
import org.elasticsearch.index.query.QueryBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.FacetedPage;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import static org.elasticsearch.index.query.FilterBuilders.boolFilter;
import static org.elasticsearch.index.query.FilterBuilders.rangeFilter;
import static org.elasticsearch.index.query.FilterBuilders.termsFilter;
import static org.elasticsearch.index.query.QueryBuilders.filteredQuery;

/**
 * Created by NguyenDangKhoa on 5/18/15.
 */
@Service
public class SalaryReviewServiceImpl implements SalaryReviewService {

    public static final long MIN_SALARY_ACCEPTABLE = 250L;

    public static final long MAX_SALARY_ACCEPTABLE = 5000L;

    @Resource
    private SalaryReviewRepository salaryReviewRepository;

    @Resource
    private JobQueryBuilder jobQueryBuilder;

    @Value("${elasticsearch.index.name}")
    private String elasticSearchIndexName;

    @Override
    public List<SalaryReview> searchSalaryReview(String jobTitle, List<Long> jobCategories) {

        Calendar now = Calendar.getInstance();
        now.add(Calendar.MONTH, -6);
        QueryBuilder queryBuilder = filteredQuery(jobQueryBuilder.jobTitleQueryBuilder(jobTitle),
                boolFilter().must(termsFilter("jobCategories", jobCategories))
                        .must(rangeFilter("netSalary").from(MIN_SALARY_ACCEPTABLE).to(MAX_SALARY_ACCEPTABLE))
                        .must(rangeFilter("createdDateTime").from(now.getTimeInMillis())));

        List<SalaryReview> salaryReviews = new ArrayList<>();
        FacetedPage<SalaryReview> salaryReviewFirstPage = salaryReviewRepository.search(queryBuilder, new PageRequest(0, 100));
        salaryReviews.addAll(salaryReviewFirstPage.getContent());

        int totalPage = salaryReviewFirstPage.getTotalPages();
        int pageIndex = 1;
        while (pageIndex < totalPage) {
            salaryReviews.addAll(salaryReviewRepository.search(queryBuilder, new PageRequest(pageIndex, 100)).getContent());
        }
        return salaryReviews;
    }
}
