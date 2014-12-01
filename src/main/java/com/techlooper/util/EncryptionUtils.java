package com.techlooper.util;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by NguyenDangKhoa on 10/24/14.
 */
public class EncryptionUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(EncryptionUtils.class);

    public static String encodeHexa(String value) {
        return new String(Hex.encodeHex(value.getBytes()));
    }

    public static String decodeHexa(String value) {
        try {
            return new String(Hex.decodeHex(value.toCharArray()));
        } catch (DecoderException ex) {
            LOGGER.error(ex.getMessage(), ex);
        }
        return value;
    }

}
