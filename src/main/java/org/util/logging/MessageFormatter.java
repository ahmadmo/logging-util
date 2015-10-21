package org.util.logging;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ahmad
 */
final class MessageFormatter {

    static final char DELIM_START = '{';
    static final char DELIM_STOP = '}';
    static final String DELIM_STR = "{}";
    private static final char ESCAPE_CHAR = '\\';

    private MessageFormatter() {
    }

    static FormattingTuple format(String messagePattern, Object arg) {
        return arrayFormat(messagePattern, new Object[]{arg});
    }

    static FormattingTuple format(final String messagePattern, Object argA, Object argB) {
        return arrayFormat(messagePattern, new Object[]{argA, argB});
    }

    static Throwable getThrowableCandidate(Object[] argArray) {
        if (argArray == null || argArray.length == 0) {
            return null;
        }
        final Object lastEntry = argArray[argArray.length - 1];
        return lastEntry instanceof Throwable ? (Throwable) lastEntry : null;
    }

    static FormattingTuple arrayFormat(final String messagePattern, final Object[] argArray) {
        Throwable throwableCandidate = getThrowableCandidate(argArray);
        if (messagePattern == null) {
            return new FormattingTuple(null, argArray, throwableCandidate);
        }
        if (argArray == null) {
            return new FormattingTuple(messagePattern);
        }
        int i = 0;
        int j;
        int L;
        StringBuilder sb = new StringBuilder(messagePattern.length() + 50);
        for (L = 0; L < argArray.length; L++) {
            j = messagePattern.indexOf(DELIM_STR, i);
            if (j == -1) {
                if (i == 0) {
                    return new FormattingTuple(messagePattern, argArray, throwableCandidate);
                } else {
                    sb.append(messagePattern.substring(i, messagePattern.length()));
                    return new FormattingTuple(sb.toString(), argArray, throwableCandidate);
                }
            } else if (isEscapedDelimiter(messagePattern, j)) {
                if (!isDoubleEscaped(messagePattern, j)) {
                    L--;
                    sb.append(messagePattern.substring(i, j - 1));
                    sb.append(DELIM_START);
                    i = j + 1;
                } else {
                    sb.append(messagePattern.substring(i, j - 1));
                    deeplyAppendParameter(sb, argArray[L], new HashMap<Object[], Void>());
                    i = j + 2;
                }
            } else {
                sb.append(messagePattern.substring(i, j));
                deeplyAppendParameter(sb, argArray[L], new HashMap<Object[], Void>());
                i = j + 2;
            }
        }
        sb.append(messagePattern.substring(i, messagePattern.length()));
        return L < argArray.length - 1
                ? new FormattingTuple(sb.toString(), argArray, throwableCandidate)
                : new FormattingTuple(sb.toString(), argArray, null);
    }

    static boolean isEscapedDelimiter(String messagePattern, int delimiterStartIndex) {
        return delimiterStartIndex != 0 && messagePattern.charAt(delimiterStartIndex - 1) == ESCAPE_CHAR;
    }

    static boolean isDoubleEscaped(String messagePattern, int delimiterStartIndex) {
        return delimiterStartIndex >= 2 && messagePattern.charAt(delimiterStartIndex - 2) == ESCAPE_CHAR;
    }

    private static void deeplyAppendParameter(StringBuilder sb, Object o, Map<Object[], Void> seenMap) {
        if (o == null) {
            sb.append("null");
            return;
        }
        if (o.getClass().isArray()) {
            if (o instanceof boolean[]) {
                booleanArrayAppend(sb, (boolean[]) o);
            } else if (o instanceof byte[]) {
                byteArrayAppend(sb, (byte[]) o);
            } else if (o instanceof char[]) {
                charArrayAppend(sb, (char[]) o);
            } else if (o instanceof short[]) {
                shortArrayAppend(sb, (short[]) o);
            } else if (o instanceof int[]) {
                intArrayAppend(sb, (int[]) o);
            } else if (o instanceof long[]) {
                longArrayAppend(sb, (long[]) o);
            } else if (o instanceof float[]) {
                floatArrayAppend(sb, (float[]) o);
            } else if (o instanceof double[]) {
                doubleArrayAppend(sb, (double[]) o);
            } else {
                objectArrayAppend(sb, (Object[]) o, seenMap);
            }
        } else {
            safeObjectAppend(sb, o);
        }
    }

    private static void safeObjectAppend(StringBuilder sb, Object o) {
        try {
            sb.append(o.toString());
        } catch (Throwable t) {
            System.err.println("SLF4J: Failed toString() invocation on an object of type [" + o.getClass().getName() + ']');
            t.printStackTrace();
            sb.append("[FAILED toString()]");
        }
    }

    private static void objectArrayAppend(StringBuilder sb, Object[] a, Map<Object[], Void> seenMap) {
        sb.append('[');
        if (!seenMap.containsKey(a)) {
            seenMap.put(a, null);
            final int len = a.length;
            for (int i = 0; i < len; i++) {
                deeplyAppendParameter(sb, a[i], seenMap);
                if (i != len - 1) {
                    sb.append(", ");
                }
            }
            seenMap.remove(a);
        } else {
            sb.append("...");
        }
        sb.append(']');
    }

    private static <T> void arrapAppend(StringBuilder sb, T[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void booleanArrayAppend(StringBuilder sb, boolean[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void byteArrayAppend(StringBuilder sb, byte[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void charArrayAppend(StringBuilder sb, char[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void shortArrayAppend(StringBuilder sb, short[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void intArrayAppend(StringBuilder sb, int[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void longArrayAppend(StringBuilder sb, long[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void floatArrayAppend(StringBuilder sb, float[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

    private static void doubleArrayAppend(StringBuilder sb, double[] a) {
        sb.append('[');
        final int len = a.length;
        for (int i = 0; i < len; i++) {
            sb.append(a[i]);
            if (i != len - 1) {
                sb.append(", ");
            }
        }
        sb.append(']');
    }

}