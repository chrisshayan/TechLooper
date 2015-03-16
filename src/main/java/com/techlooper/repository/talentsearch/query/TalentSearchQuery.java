package com.techlooper.repository.talentsearch.query;

import com.techlooper.model.TalentSearchRequest;
import org.springframework.data.elasticsearch.core.query.SearchQuery;

/**
 * Created by NguyenDangKhoa on 3/16/15.
 */
public interface TalentSearchQuery {

    SearchQuery getSearchQuery(final TalentSearchRequest searchRequest);

}
