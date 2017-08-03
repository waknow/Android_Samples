package com.jove.demo.crypto.lib;

import android.content.Context;
import android.util.Base64;

import com.jove.demo.crypto.lib.tools.DESede;
import com.jove.demo.crypto.lib.tools.ITool;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;

/**
 * get raw data
 * <p>
 * Created by jove.zhu on 2017/7/24.
 */

public class Data {
    private static ITool tool = new DESede();

    public static String getData(Context ctx, String name) throws IOException {
        final InputStream in = ctx.getAssets().open(name);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String content = sb.toString();

        byte[] result = tool.decrypt(Encodes.decodeBase64(URLDecoder.decode(content)));
        if (result == null) {
            return null;
        }
        return new String(result);
    }

    private static class Encodes {
        private static final String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        private static final char[] BASE62 = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz"
                .toCharArray();
        private static final String DEFAULT_URL_ENCODING = "UTF-8";

        private Encodes() {
        }

        /**
         * Base64编码, byte[]->String.
         */
        public static String encodeBase64(byte[] input) {

            return Base64.encodeToString(input, Base64.NO_WRAP);

//		return Base64.encodeBase64String(input);
        }

        /**
         * Base64编码, URL安全(将Base64中的URL非法字符'+'和'/'转为'-'和'_', 见RFC3548).
         */
//	public static String encodeUrlSafeBase64(byte[] input) {
//		return Base64.encodeBase64URLSafeString(input);
//	}

        /**
         * Base64解码, String->byte[].
         */
        public static byte[] decodeBase64(String input) {
            return Base64.decode(input, Base64.DEFAULT);
            //return Base64.decodeBase64(input);
        }

        private static String alphabetEncode(long num, int base) {
            num = Math.abs(num);
            StringBuilder sb = new StringBuilder();
            for (; num > 0; num /= base) {
                sb.append(ALPHABET.charAt((int) (num % base)));
            }

            return sb.toString();
        }

        public static String encodeURIComponent(String s) {
            String result = null;
            try {
                result = URLEncoder.encode(s, "UTF-8")
                        .replaceAll("\\%2F", "/")
                        .replaceAll("\\%3A", ":")
                        .replaceAll("\\+", "%20")
                        .replaceAll("\\%21", "!")
                        .replaceAll("\\%27", "'")
                        .replaceAll("\\%28", "(")
                        .replaceAll("\\%29", ")")
                        .replaceAll("\\%7E", "~");
            }
            // This exception should never occur.
            catch (UnsupportedEncodingException e) {
                result = s;
            }

            return result;
        }
    }

}
