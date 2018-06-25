package com.vpp.common.utils;

import java.util.Collection;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 继承自Spring util的工具类，减少jar依赖
 * 
 * @author lxy
 */
public class StringUtils extends org.springframework.util.StringUtils {
    /**
     * Check whether the given {@code CharSequence} contains actual <em>text</em>.
     * <p>
     * More specifically, this method returns {@code true} if the {@code CharSequence} is not {@code null}, its length is
     * greater than 0, and it contains at least one non-whitespace character.
     * <p>
     * 
     * <pre class="code">
     * StringUtils.isBlank(null) = true
     * StringUtils.isBlank("") = true
     * StringUtils.isBlank(" ") = true
     * StringUtils.isBlank("12345") = false
     * StringUtils.isBlank(" 12345 ") = false
     * </pre>
     * 
     * @param str the {@code CharSequence} to check (may be {@code null})
     * @return {@code true} if the {@code CharSequence} is not {@code null}, its length is greater than 0, and it does not
     *         contain whitespace only
     * @see Character#isWhitespace
     */
    public static boolean isBlank(final CharSequence cs) {
        return !StringUtils.isNotBlank(cs);
    }

    /**
     * <p>
     * Checks if a CharSequence is not empty (""), not null and not whitespace only.
     * </p>
     * 
     * <pre>
     * StringUtils.isNotBlank(null)      = false
     * StringUtils.isNotBlank("")        = false
     * StringUtils.isNotBlank(" ")       = false
     * StringUtils.isNotBlank("bob")     = true
     * StringUtils.isNotBlank("  bob  ") = true
     * </pre>
     * 
     * @param cs the CharSequence to check, may be null
     * @return {@code true} if the CharSequence is not empty and not null and not whitespace
     * @see Character#isWhitespace
     */
    public static boolean isNotBlank(final CharSequence cs) {
        return StringUtils.hasText(cs);
    }

    /**
     * Convert a {@code Collection} into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param coll the {@code Collection} to convert
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Collection<?> coll, String delim) {
        return StringUtils.collectionToDelimitedString(coll, delim);
    }

    /**
     * Convert a {@code String} array into a delimited {@code String} (e.g. CSV).
     * <p>
     * Useful for {@code toString()} implementations.
     * 
     * @param arr the array to display
     * @param delim the delimiter to use (typically a ",")
     * @return the delimited {@code String}
     */
    public static String join(Object[] arr, String delim) {
        return StringUtils.arrayToDelimitedString(arr, delim);
    }

    /**
     * 生成uuid
     * 
     * @return UUID
     */
    public static String getUUId() {
        return UUID.randomUUID().toString();
    }

    /**
     * 替换数据库时间字段.0
     * 
     * @author Lxl
     * @param source
     * @return
     */
    public static String beanTimeReplace(String source) {
        if (StringUtils.isNotBlank(source)) {
            return source.replace(".0", "");
        }
        return null;
    }

    /**
     * 检查string是否是纯数字
     *
     * @author shiming
     * @param string
     * @return
     */
    public static boolean isPureDigital(String string) {
        if (string == null || string.length() <= 0)
            return false;

        String regularEx = "^[0-9]*[1-9][0-9]*$";
        Pattern p;
        Matcher m;
        p = Pattern.compile(regularEx);
        m = p.matcher(string);
        if (m.matches())
            return true;
        else
            return false;
    }
}
