package com.pokepet.util;

import java.util.UUID;

/**
 * Created by Fade on 2018/11/19.
 */
public class UUIDUtils {

    public static String randomUUID(){
        return UUID.randomUUID().toString().replace("-","");
    }
}
