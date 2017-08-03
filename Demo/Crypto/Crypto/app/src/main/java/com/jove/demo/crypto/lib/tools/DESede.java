package com.jove.demo.crypto.lib.tools;

import android.util.Log;

import java.io.UnsupportedEncodingException;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * DESede
 * Created by jove.zhu on 2017/7/24.
 */

public class DESede implements ITool {
    private final static String TAG = DESede.class.getSimpleName();

    public static final String DES_CRYPT_KEY = "2016-moku-iJhxSQeqnUvXH5";
    private static final String Algorithm = "DESede"; // 定义 加密算法,可用

    @Override
    public byte[] encrypt(byte[] data) {
        try {
            SecretKey desKey = new SecretKeySpec(build3DesKey(DES_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.ENCRYPT_MODE, desKey);
            return c1.doFinal(data);
        } catch (Exception e) {
            Log.e(TAG, "encrypt", e);
        }
        return null;
    }


    @Override
    public byte[] decrypt(byte[] data) {
        try {
            SecretKey desKey = new SecretKeySpec(build3DesKey(DES_CRYPT_KEY), Algorithm);
            Cipher c1 = Cipher.getInstance(Algorithm);
            c1.init(Cipher.DECRYPT_MODE, desKey);
            return c1.doFinal(data);
        } catch (Exception e) {
            Log.e(TAG, "decrypt", e);
        }
        return null;
    }

    private static byte[] build3DesKey(String keyStr)
            throws UnsupportedEncodingException {
        byte[] key = new byte[24]; // 声明一个24位的字节数组，默认里面都是0
        byte[] temp = keyStr.getBytes("UTF-8"); // 将字符串转成字节数组

		/*
         * 执行数组拷贝 System.arraycopy(源数组，从源数组哪里开始拷贝，目标数组，拷贝多少位)
		 */
        if (key.length > temp.length) {
            // 如果temp不够24位，则拷贝temp数组整个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            // 如果temp大于24位，则拷贝temp数组24个长度的内容到key数组中
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }
}
