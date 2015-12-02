package com.techlooper.service.impl;

import com.itextpdf.text.pdf.BaseFont;
import com.lowagie.text.DocumentException;
import com.techlooper.dto.FinalChallengeReportDto;
import com.techlooper.dto.PhaseEntry;
import com.techlooper.dto.PhaseEntry.PhaseEntryBuilder;
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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.xhtmlrenderer.pdf.ITextRenderer;

import javax.annotation.Resource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
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

  @Value("${web.baseUrl}")
  private String baseUrl;

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

    List<PhaseEntry> phaseEntries = registrantAgg.keySet().stream().map(phase -> {
      ChallengeRegistrantPhaseItem phaseItem = registrantAgg.get(phase);
      return PhaseEntryBuilder.phaseEntry()
        .withPhase(phaseItem.getPhase())
        .withDate(challenge.phaseStartDate(phaseItem.getPhase()))
        .withCountMembers(phaseItem.getRegistration())
        .build();
    }).collect(Collectors.toList());

    phaseEntries.add(PhaseEntryBuilder.phaseEntry()
      .withPhase(ChallengePhaseEnum.WINNER)
      .withDate(challenge.phaseStartDate(ChallengePhaseEnum.WINNER))
      .withCountMembers(challengeRegistrantService.countNumberOfFinalists(challengeId))
      .build());

    phaseEntries.sort((pe1, pe2) -> pe1.getPhase().getOrder().compareTo(pe2.getPhase().getOrder()));

    finalChallengeReportDto.setPhaseEntries(phaseEntries);
    finalChallengeReportDto.setWinnersInfo(challengeRegistrantService.findWinnerRegistrantsByChallengeId(challengeId));
    finalChallengeReportDto.setBaseUrl(baseUrl);
    finalChallengeReportDto.calculateRemainingFields();

    try {
      Template template = finalChallengeReportEn;
      StringWriter stringWriter = new StringWriter();
      template.process(finalChallengeReportDto, stringWriter);

      ByteArrayOutputStream os = new ByteArrayOutputStream();

      ITextRenderer renderer = new ITextRenderer();
      renderer.getFontResolver().addFont("font/verdana.ttf", BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
      renderer.setDocumentFromString(stringWriter.toString());
      renderer.layout();
      renderer.createPDF(os);
      stringWriter.flush();
      return os;
    }
    catch (TemplateException | IOException | DocumentException e) {
      LOGGER.debug("Can not process template", e);
    }

    return null;
  }
}
