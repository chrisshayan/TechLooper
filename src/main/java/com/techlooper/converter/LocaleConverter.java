package com.techlooper.converter;

import org.dozer.DozerConverter;

import java.util.Locale;

/**
 * Created by phuonghqh on 12/15/14.
 */
public class LocaleConverter extends DozerConverter<Locale, Locale> {

    public LocaleConverter() {
        super(Locale.class, Locale.class);
    }

    public Locale convertTo(Locale source, Locale destination) {
        return source;
    }

    public Locale convertFrom(Locale source, Locale destination) {
        return source;
    }
}
