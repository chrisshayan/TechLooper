package com.techlooper.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by NguyenDangKhoa on 11/5/14.
 */
public class LookUpUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(LookUpUtils.class);

    public static <E extends Enum<E>> E lookup(Class<E> e, String id, E defaultValue) {
        try {
            return Enum.valueOf(e, id);
        } catch (IllegalArgumentException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return defaultValue;
    }
}
