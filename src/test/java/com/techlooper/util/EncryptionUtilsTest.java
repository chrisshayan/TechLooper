package com.techlooper.util;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class EncryptionUtilsTest {

    private String input;
    private String expected;

    // Parameters pass via this constructor
    public EncryptionUtilsTest(String input, String expected) {
        this.input = input;
        this.expected = expected;
    }

    // Declares parameters data here
    @Parameters(name = "{index}: encodeHexa({0})={1}")
    public static Iterable<Object[]> dataForEncodeHexa() {
        return Arrays.asList(new Object[][]{
                {"junit", "6a756e6974"},
                {"junit4", "6a756e697434"},
                {"spring mvc", "737072696e67206d7663"},
                {"spring_mvc", "737072696e675f6d7663"},
                {"C#", "4323"},
                {"C/C++", "432f432b2b"},
                {"", ""}
        });
    }

    @Test
    public void testEncodeHexa() throws Exception {
        assertEquals(expected, EncryptionUtils.encodeHexa(input));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodeHexaWithNullInput() throws Exception {
        EncryptionUtils.encodeHexa(null);
    }

    @Test
    public void testDecodeHexa() throws Exception {
        // In order not to repeat the parameter data collection, just swap the input and expected value for decode method
        assertEquals(input, EncryptionUtils.decodeHexa(expected));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDecodeHexaWithNullInput() throws Exception {
        EncryptionUtils.decodeHexa(null);
    }
}