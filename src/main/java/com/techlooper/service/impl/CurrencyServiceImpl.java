package com.techlooper.service.impl;

import com.techlooper.service.CurrencyService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

/**
 * Created by phuonghqh on 5/28/15.
 */
@Service
public class CurrencyServiceImpl implements CurrencyService {

    @Value("${usd2vnd}")
    private Long usd2vnd;

    public Long usdToVndRate() {
        return usd2vnd;
    }

    public String formatCurrency(Double value, Locale currentLocale) {
        NumberFormat numberFormatter = NumberFormat.getNumberInstance(currentLocale);
        DecimalFormat decimalFormatter = (DecimalFormat) numberFormatter;
        String fullPattern = Currency.getInstance(currentLocale).getSymbol() + "###,###";
        decimalFormatter.applyPattern(fullPattern);
        return decimalFormatter.format(value);
    }
}