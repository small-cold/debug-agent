package me.wlchao.debug.agent;

import java.nio.ByteBuffer;

/**
 * @author wanglichao
 * @date 2021/7/1 13:28
 */
public class MainTest {

    public static void main(String[] args) {
        ByteBuffer.allocateDirect(100);
    }
}
