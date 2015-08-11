package com.techlooper.controller;

import com.techlooper.model.KimonoDataModel;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class KimonoLabController {

    @RequestMapping(value = "/kimono/integrate", method = RequestMethod.POST)
    public boolean listProject(@RequestBody KimonoDataModel data) throws Exception {
        return true;
    }

}
