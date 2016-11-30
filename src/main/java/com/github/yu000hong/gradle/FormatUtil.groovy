package com.github.yu000hong.gradle

class FormatUtil {
    public static final int PAD_WIDTH = 4

    public static int padWidth(int width) {
        assert width > 0
        return ((int) ((width - 1) / PAD_WIDTH) + 2) * PAD_WIDTH
    }

    public static String padding(String text, int width) {
        StringBuilder sb = new StringBuilder()
        int len = 0
        if (text != null) {
            sb.append(text)
            len = text.length()
        }
        (width - len).times {
            sb.append(' ')
        }
        return sb.toString()
    }

    public static String repeat(CharSequence c, int times) {
        def sb = new StringBuilder()
        times.times {
            sb.append(c)
        }
        return sb.toString()
    }

}
