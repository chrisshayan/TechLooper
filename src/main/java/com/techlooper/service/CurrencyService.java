package com.techlooper.service;

import java.util.Locale;

/**
 * Created by phuonghqh on 5/28/15.
 */
public interface CurrencyService {

    Long usdToVndRate();

    String formatCurrency(Double value, Locale currentLocale);

}
