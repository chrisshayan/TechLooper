package com.techlooper.model;

import java.util.List;

/**
 * Created by S-MIS-TRINHPT on 6/1/15.
 */
public class SuggestionResponse {

    private List<SuggestionItem> items;

    public List<SuggestionItem> getItems() {
        return items;
    }

    public void setItems(List<SuggestionItem> items) {
        this.items = items;
    }
}
