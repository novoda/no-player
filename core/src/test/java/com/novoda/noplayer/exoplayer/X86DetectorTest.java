package com.novoda.noplayer.exoplayer;

import java.util.Arrays;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import static org.fest.assertions.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class X86DetectorTest {

    private static final boolean IS_X86 = true;
    private static final boolean IS_NOT_X86 = false;

    private final String[] abis;
    private final boolean isX86;

    @Parameterized.Parameters
    public static Iterable<Object[]> data() {
        return Arrays.asList(new Object[][]{
                {new String[]{"x86"}, IS_X86},
                {new String[]{"arm"}, IS_NOT_X86},
                {new String[]{"a", "b", "c"}, IS_NOT_X86},
                {new String[]{"a,b", "x86"}, IS_X86},
                {new String[]{"ax86b"}, IS_X86},
                {new String[]{}, IS_NOT_X86}
        });
    }

    public X86DetectorTest(String[] abis, boolean isX86) {
        this.isX86 = isX86;
        this.abis = abis;
    }

    @Test
    public void givenX86Abis_whenWeCheckTheArchitecture_thenItIsX86() {
        X86Detector x86Detector = new X86Detector(abis);

        boolean actual = x86Detector.isX86();

        assertThat(actual).isEqualTo(isX86);
    }

}
