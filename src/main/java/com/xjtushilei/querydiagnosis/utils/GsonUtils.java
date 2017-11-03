package com.xjtushilei.querydiagnosis.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xjtushilei.querydiagnosis.entity.icd10.L2;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class GsonUtils {

    public static void print(Object o) {
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        System.out.println(gson.toJson(o));
    }
    public static String toString(Object o) {
        Gson gson = new GsonBuilder().create();
        return gson.toJson(o);
    }


//    HashMap<String, L2> icd10L2 = new GsonBuilder().create().fromJson(json,new TypeToken<HashMap<String, L2>>(){}
//            .getType());
}
