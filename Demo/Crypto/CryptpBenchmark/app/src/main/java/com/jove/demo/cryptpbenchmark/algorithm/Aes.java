package com.jove.demo.cryptpbenchmark.algorithm;

import android.util.Base64;
import android.util.Log;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;

/**
 * Algorithm of aes
 * Created by jove.zhu on 2017/7/31.
 */

public class Aes implements IAlgorithm {
    private SecretKey key = null;
    private IvParameterSpec ivParameterSpec;

    @Override
    public String name() {
        return "AES";
    }

    @Override
    public void init() throws Exception {
        KeyGenerator generator = KeyGenerator.getInstance("AES");
        generator.init(256);
        key = generator.generateKey();

        byte[] keyBytes = key.getEncoded();
        Log.d("AES KEY", Base64.encodeToString(keyBytes, 0));

        ivParameterSpec = new IvParameterSpec("4e5Wa71fYoT7MFEX".getBytes());



    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, key, ivParameterSpec);
        return cipher.doFinal(data);
    }
}
