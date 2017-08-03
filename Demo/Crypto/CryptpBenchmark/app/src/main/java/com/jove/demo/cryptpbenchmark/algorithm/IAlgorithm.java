package com.jove.demo.cryptpbenchmark.algorithm;

/**
 * Created by jove.zhu on 2017/7/31.
 */

public interface IAlgorithm {
    String name();

    void init() throws Exception;

    byte[] decrypt(byte[] data) throws Exception;

    byte[] encrypt(byte[] data) throws Exception;
}
