package com.msp.jdbc.util;

public class StringUtils {

    /**
     * Check null or blank
     *
     * @param str
     * @return boolean
     */
    public static boolean isNullOrBlank(String string) {

        if ((string == null) || Common.EMPTY.equals(string) || isWhiteSpace(string)) {
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(String str) {
        if ((str == null) || Common.EMPTY.equals(("" + str).trim())) {
            return true;
        }
        return false;

    }

    /**
     * Check string is white space
     *
     * @param string String
     * @return boolean
     */
    public static boolean isWhiteSpace(String string) {
        boolean result = false;
        if (string.length() > 0) {
            for (int i = 0; i < string.length(); i++) {
                int index = i;
                if (string.charAt(i) == Common.SPACE) {
                    index++;
                } else {
                    break;
                }
                if ((string.length() - index) == 0) {
                    result = true;
                    break;

                }
            }

        }
        return result;
    }

    /**
     * Compare two String
     *
     * @param string1 String
     * @param string2 String
     * @return
     */
    public static boolean compareString(String string1, String string2) {

        boolean result = false;
        int length1 = string1.length();
        int length2 = string2.length();

        if (StringUtils.isNullOrBlank(string1)) {
            return false;
        }
        if (StringUtils.isNullOrBlank(string2)) {
            return false;
        }

        if (!StringUtils.isNullOrBlank(string1) && !StringUtils.isNullOrBlank(string2)) {
            if (length1 > length2) {
                return false;
            } else if (length1 < length2) {
                return false;
            } else {
                for (int i = 0; i < length1; i++) {
                    if (string1.charAt(i) != string2.charAt(i)) {
                        return false;
                    }
                }
                result = true;
            }

        }

        return result;
    }

    /**
     * Check string contains string
     *
     * @param string1 String
     * @param string2 String
     * @return boolean
     */
    public static boolean contains(String string1, String string2) {
        boolean result = false;
        int length1 = string1.length();
        int length2 = string2.length();

        if (StringUtils.isNullOrBlank(string1)) {
            return false;
        }
        if (StringUtils.isNullOrBlank(string2)) {
            return false;
        }

        if (!isNullOrBlank(string1) && !isNullOrBlank(string2)) {

            if (length1 > length2) {
                for (int i = 0; i <= length1 - length2; i++) {
                    if (string1.charAt(i) == string2.charAt(0)) {
                        int index = i;
                        for (int j = 0; j < length2; j++) {
                            if (string1.charAt(index) == string2.charAt(j)) {
                                index++;
                            } else {
                                break;
                            }
                        }
                        if (index - i == string2.length()) {
                            result = true;

                        }
                    }
                }
            } else if (length1 < length2) {
                for (int i = 0; i <= length2 - length1; i++) {
                    if (string2.charAt(i) == string1.charAt(0)) {
                        int index = i;
                        for (int j = 0; j < length1; j++) {
                            if (string2.charAt(index) == string1.charAt(j)) {
                                index++;

                            } else {
                                break;
                            }
                        }
                        if (index - i == string1.length()) {
                            result = true;

                        }
                    }
                }
            } else {
                for (int i = 0; i < length1; i++) {
                    if (string2.charAt(i) == string1.charAt(0)) {
						result = true;
                    } else {
                        result = false;
                        break;
                    }
                }
            }
        }
        return result;
    }

    /*
     * public String nomalizeName(String name) { name = "tran   Van giang"; int diff
     * = 'a' - 'A';
     *
     * char prevChar = name.charAt(0); StringBuilder builder = new StringBuilder();
     * for (int i = 1; i < name.length(); i++) { if (prevChar != Common.SPACE) { } }
     *
     * }
     */

    /**
     * Normalize name
     *
     * @param text String
     * @return String
     */
    public static String normalize(String text) {
        StringBuilder sb = new StringBuilder();
        if (!StringUtils.isNullOrBlank(text)) {
            char prevChar = text.charAt(0);
            int diff = 'a' - 'A';

            if (prevChar != Common.SPACE) {
                sb.append(Character.isLowerCase(prevChar) ? (char) (prevChar - diff) : prevChar);
            }

            for (int i = 1; i < text.length(); i++) {
                char c = text.charAt(i);

                if (prevChar == Common.SPACE) {
                    if (Common.SPACE != c) {
                        if (Character.isLowerCase(c)) {
                            c -= diff;
                        }
                        sb.append(c);
                        prevChar = c;
                    }
                } else {

                    if (Character.isUpperCase(c)) {
                        c += diff;
                    }
                    /*
                     * sb.append(Character.isLowerCase(prevChar) ? (char) (prevChar - diff) :
                     * prevChar);
                     */
                    sb.append(c);
                    prevChar = c;
                }
            }
        }

        return sb.toString();
    }

    public static String trim(String string) {

        char charNew = 0;

        String stringNew = null;

        if (!StringUtils.isNullOrBlank(string)) {

            int startString = 0;
            for (int i = 0; i < string.length(); i++) {
                startString = i;
                if (string.charAt(startString) == Common.SPACE) {
                    startString++;
                } else {
                    break;
                }

            }
            int lastString = 0;
            for (int j = string.length(); j < 0; j--) {
                lastString = j;
                if (string.charAt(lastString) == Common.SPACE) {
                    lastString--;
                } else {
                    break;
                }

            }

            for (int g = startString; g <= lastString; g++) {
                charNew = string.charAt(g);
                stringNew = String.valueOf(stringNew);

            }
            string = stringNew;

        }

        return stringNew;

        /*
         * if (!StringUtils.isNullOrBlank(string)) {
         *
         * int length = string.length();
         *
         * char prevChar = string.charAt(0); char lastChar = string.charAt(length-1);
         * if(prevChar == Common.SPACE) { for (int i = 1; i < length; i++) { int start =
         * i; if(string.charAt(start)== Common.SPACE) { start++; }else { break; }
         *
         * } } } return string;
         */

    }

}
