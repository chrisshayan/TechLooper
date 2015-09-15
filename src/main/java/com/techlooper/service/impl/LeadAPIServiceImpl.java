package com.techlooper.service.impl;

import com.techlooper.entity.vnw.VnwCompany;
import com.techlooper.entity.vnw.VnwUser;
import com.techlooper.model.LeadEventEnum;
import com.techlooper.model.LeadModel;
import com.techlooper.service.LeadAPIService;
import com.techlooper.util.JsonUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.Resource;

@Service
public class LeadAPIServiceImpl implements LeadAPIService {

    private final static Logger LOGGER = LoggerFactory.getLogger(LeadAPIServiceImpl.class);

    @Value("${CRM.LeadAPI.CreateLead.Subject.PostChallenge}")
    private String postChallengeLeadSubject;

    @Value("${CRM.LeadAPI.CreateLead.Subject.PostProject}")
    private String postProjectLeadSubject;

    @Value("${CRM.LeadAPI.CreateLead.URL}")
    private String createLeadUrl;

    @Value("${CRM.LeadAPI.CreateLead.QualityCode}")
    private Integer qualityCode;

    @Value("${CRM.LeadAPI.CreateLead.CampaignId}")
    private String campaignId;

    @Resource
    private RestTemplate restTemplate;

    @Override
    public int createNewLead(VnwUser employer, VnwCompany company, LeadEventEnum leadEvent) {
        LeadModel leadModel = buildLeadModel(employer, company, leadEvent);
        HttpEntity<String> requestEntity = configureHttpRequestEntity(leadModel);

        ResponseEntity<String> responseEntity;
        try {
            responseEntity = restTemplate.exchange(createLeadUrl, HttpMethod.POST, requestEntity, String.class);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return HttpStatus.INTERNAL_SERVER_ERROR.value();
        }

        return responseEntity.getStatusCode().value();
    }

    private LeadModel buildLeadModel(VnwUser employer, VnwCompany company, LeadEventEnum leadEvent) {
        LeadModel leadModel = new LeadModel();
        String subject = (leadEvent == LeadEventEnum.POST_CHALLENGE) ? postChallengeLeadSubject : postProjectLeadSubject;
        leadModel.setSubject(subject);
        leadModel.setTelephone1(company.getTelephone());
        leadModel.setFirstName(employer.getFirstName());
        leadModel.setSource(company.getCompanyName());
        leadModel.setLeadQualityCode(qualityCode);
        leadModel.setLegalName(company.getCompanyName());
        leadModel.setCampaignId(campaignId);
        leadModel.setCompanyName(company.getCompanyName());
        leadModel.setMobilePhone(company.getCellphone());
        leadModel.setJobTitle(employer.getJobTitle());
        leadModel.setEmailAddress1(employer.getEmail());
        leadModel.setEmailAddress2(employer.getEmail2());
        leadModel.setFax(company.getFax());
        leadModel.setWebsiteUrl(company.getWebsiteUrl());
        leadModel.setAddress(company.getAddress());
        leadModel.setDistrict(company.getDistrict());
        leadModel.setCity(company.getCity());
        leadModel.setCountry(company.getCountry());
        leadModel.setCompanySize(company.getCompanySize());
        return leadModel;
    }

    private HttpEntity<String> configureHttpRequestEntity(LeadModel leadModel) {
        final String leadJsonModel = JsonUtils.toJSON(leadModel).get();
        final HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.setContentType(MediaType.APPLICATION_JSON);
        return new HttpEntity<>(leadJsonModel, requestHeaders);
    }
}
