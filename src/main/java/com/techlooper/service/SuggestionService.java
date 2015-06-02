package com.techlooper.service;

import java.util.List;

/**
 * Created by NguyenDangKhoa on 5/29/15.
 */
public interface SuggestionService {

    List<String> suggestSkills(String query);

    List<String> suggestJobTitles(String query);

}
