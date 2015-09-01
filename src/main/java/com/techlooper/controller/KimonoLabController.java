package com.techlooper.controller;

import com.techlooper.model.KimonoJobModel;
import com.techlooper.service.ScrapeJobService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
public class KimonoLabController {

    private final static Logger LOGGER = LoggerFactory.getLogger(KimonoLabController.class);

    @Resource
    private ScrapeJobService scrapeJobService;

    @RequestMapping(value = "/kimono/integrate", method = RequestMethod.POST)
    public boolean listProject(@RequestBody KimonoJobModel data) {
        try {
            scrapeJobService.save(data);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage(), ex);
            return false;
        }
        return true;
    }

}
