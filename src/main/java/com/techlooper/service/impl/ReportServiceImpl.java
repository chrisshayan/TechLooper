package com.techlooper.service.impl;

import com.techlooper.dto.FinalChallengeReportDto;
import com.techlooper.entity.ChallengeEntity;
import com.techlooper.model.ChallengePhaseEnum;
import com.techlooper.model.ChallengeRegistrantPhaseItem;
import com.techlooper.repository.elasticsearch.ChallengeRepository;
import com.techlooper.service.ChallengeRegistrantService;
import com.techlooper.service.ReportService;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.dozer.Mapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by phuonghqh on 11/30/15.
 */
@Service
public class ReportServiceImpl implements ReportService {

  private final Logger LOGGER = LoggerFactory.getLogger(ReportServiceImpl.class);

  @Resource
  private ChallengeRepository challengeRepository;

  @Resource
  private ChallengeRegistrantService challengeRegistrantService;

  @Resource
  private Mapper dozerMapper;

  @Resource
  private Template finalChallengeReportEn;

  public ByteArrayOutputStream generateFinalChallengeReport(String challengeAuthorEmail, Long challengeId) {
    ChallengeEntity challenge = challengeRepository.findOne(challengeId);
    if (challenge == null || !challenge.getAuthorEmail().equalsIgnoreCase(challengeAuthorEmail)) {
      return null;
    }

    Map<ChallengePhaseEnum, ChallengeRegistrantPhaseItem> registrantAgg = challengeRegistrantService.countNumberOfRegistrantsByPhase(challengeId);
    Set<ChallengePhaseEnum> redundantPhases = registrantAgg.keySet().stream().filter(phase -> !challenge.hasPhase(phase)).collect(Collectors.toSet());
    registrantAgg.keySet().removeAll(redundantPhases);

    FinalChallengeReportDto finalChallengeReportDto = new FinalChallengeReportDto();
    dozerMapper.map(challenge, finalChallengeReportDto);
    finalChallengeReportDto.setRegistrantPhaseAgg(registrantAgg);

    try {
      Template template = finalChallengeReportEn;
      StringWriter stringWriter = new StringWriter();
      template.process(finalChallengeReportDto, stringWriter);

//      String outputFile = "firstdoc.pdf";
      ByteArrayOutputStream os = new ByteArrayOutputStream();
//      ITextRenderer renderer = new ITextRenderer();
//      renderer.setDocumentFromString(stringWriter.toString());
//      renderer.layout();
//      renderer.createPDF(os);
//      stringWriter.flush();
      return os;
    }
    catch (TemplateException | IOException e) {
      LOGGER.debug("Can not process template", e);
    }

    return null;
  }
}
