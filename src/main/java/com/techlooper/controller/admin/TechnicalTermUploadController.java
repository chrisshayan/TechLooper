package com.techlooper.controller.admin;

import com.techlooper.repository.TechnicalTermRepository;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@Controller
public class TechnicalTermUploadController {

    private static final Logger LOGGER = LoggerFactory.getLogger(TechnicalTermUploadController.class);

    @Resource
    private TechnicalTermRepository technicalTermRepository;

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody String handleFileUpload(@RequestParam("file") MultipartFile uploadFile) {
        if (!uploadFile.isEmpty()) {
            try {
                File uploadedFile = technicalTermRepository.getSkillJsonResource().getFile();
                IOUtils.write(uploadFile.getBytes(), new BufferedOutputStream(new FileOutputStream(uploadedFile)));
                technicalTermRepository.refresh();
                return "Upload Successfully";
            } catch (IOException e) {
                LOGGER.error(e.getMessage(), e);
            }
        }
        return "You failed to upload because the file was empty.";
    }
}
