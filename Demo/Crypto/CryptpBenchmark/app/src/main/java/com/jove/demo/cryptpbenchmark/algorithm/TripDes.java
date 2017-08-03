package com.jove.demo.cryptpbenchmark.algorithm;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Algorithm of 3des
 * Created by jove.zhu on 2017/7/31.
 */

public class TripDes implements IAlgorithm {
    private Cipher encrypt = null;
    private Cipher decrypt = null;

    @Override
    public String name() {
        return "DESede";
    }

    @Override
    public void init() throws Exception {
        SecretKey key = new SecretKeySpec("2016-moku-iJhxSQeqnUvXH5".getBytes(), "DESede");

        encrypt = Cipher.getInstance("DESede");
        encrypt.init(Cipher.ENCRYPT_MODE, key);

        decrypt = Cipher.getInstance("DESede");
        decrypt.init(Cipher.DECRYPT_MODE, key);
    }

    @Override
    public byte[] decrypt(byte[] data) throws Exception {
        return decrypt.doFinal(data);
    }

    @Override
    public byte[] encrypt(byte[] data) throws Exception {
        return encrypt.doFinal(data);
    }
}
