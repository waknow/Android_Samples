package com.jove.demo.crypto.lib.tools;

/**
 * Created by jove.zhu on 2017/7/24.
 */

public interface ITool {
    byte[] encrypt(byte[] data);
    byte[] decrypt(byte[] data);
}
