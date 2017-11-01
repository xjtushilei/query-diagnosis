package com.xjtushilei.querydiagnosis.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class GsonUtils {

    public static void print(Object o) {
        Gson gson = new GsonBuilder()
                .setPrettyPrinting()
                .create();
        System.out.println(gson.toJson(o));
    }

}
