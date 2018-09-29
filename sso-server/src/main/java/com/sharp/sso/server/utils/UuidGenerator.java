package com.sharp.sso.server.utils;

import java.util.UUID;

/**
 * uuid生成器工具类
 */
public class UuidGenerator {

    public static String generate(){
        return UUID.randomUUID().toString().replace("-", "");
    }
}
