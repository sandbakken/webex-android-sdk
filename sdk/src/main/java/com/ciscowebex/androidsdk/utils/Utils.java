/*
 * Copyright 2016-2020 Cisco Systems Inc
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.ciscowebex.androidsdk.utils;

import android.os.Build;
import android.support.annotation.Nullable;
import android.util.Base64;

import com.cisco.spark.android.util.Strings;
import com.ciscowebex.androidsdk.Webex;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 * An utility class.
 *
 * @since 0.1
 */

public class Utils {

    private Utils(){}

    public static <T> T checkNotNull(@Nullable T object, String message) {
        if (object == null) {
            throw new NullPointerException(message);
        }
        return object;
    }

    public static String timestampUTC() {
        return DateTime.now(DateTimeZone.UTC).toString();
    }

    public static String versionInfo() {
        String tempUserAgent = String.format("%s/%s (Android %s; %s %s / %s %s;)",
                Webex.APP_NAME, Webex.APP_VERSION,
                Build.VERSION.RELEASE,
                Strings.capitalize(Build.MANUFACTURER),
                Strings.capitalize(Build.DEVICE),
                Strings.capitalize(Build.BRAND),
                Strings.capitalize(Build.MODEL)
        );
        return Strings.stripInvalidHeaderChars(tempUserAgent);
    }

    public static String encode(String id) {
        if (id == null || id.isEmpty())
            return id;

        String encodeString = "ciscospark://us/PEOPLE/" + id;
        return new String(Base64.encode(encodeString.getBytes(),
                Base64.NO_PADDING | Base64.URL_SAFE | Base64.NO_WRAP));
    }

    public static Map<String, Object> toMap(Object o) {
        Map<String, Object> result = new HashMap<>();
        if (o != null) {
            try {
                Field[] declaredFields = o.getClass().getDeclaredFields();
                for (Field field : declaredFields) {
                    String name = field.getName();
                    Object value = "@ERROR";
                    boolean accessible = field.isAccessible();
                    try {
                        field.setAccessible(true);
                        value = field.get(o);
                    } catch (Throwable ignored) {
                    } finally {
                        try {
                            field.setAccessible(accessible);
                        } catch (Throwable ignored1) {
                        }
                    }
                    result.put(name, value);
                }
            }
            catch (Throwable t) {
                t.printStackTrace();
            }
        }
        return result;
    }
}
