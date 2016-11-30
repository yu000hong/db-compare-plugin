package com.github.yu000hong.gradle

import org.testng.annotations.Test

import static org.testng.Assert.assertEquals

class TestFormatUtil {

    @Test
    public void testPadWidth() {
        assertEquals(FormatUtil.padWidth(1), 8)
        assertEquals(FormatUtil.padWidth(4), 8)
        assertEquals(FormatUtil.padWidth(5), 12)
    }

    @Test
    public void testPadding() {
        assertEquals(FormatUtil.padding(null, 4), '    ')
        assertEquals(FormatUtil.padding('', 4), '    ')
        assertEquals(FormatUtil.padding('hello world', 12), 'hello world ')
        assertEquals(FormatUtil.padding('hello world', 8), 'hello world')
    }

    @Test
    public void testRepeat() {
        assertEquals(FormatUtil.repeat('c', 4), 'cccc')
        assertEquals(FormatUtil.repeat('', 4), '')
        assertEquals(FormatUtil.repeat('-', 12), '------------')
        assertEquals(FormatUtil.repeat('hi ', 3), 'hi hi hi ')
        assertEquals(FormatUtil.repeat(null, 2), 'nullnull')
    }

}
