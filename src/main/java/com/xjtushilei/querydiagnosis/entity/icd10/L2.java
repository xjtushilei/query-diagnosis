package com.xjtushilei.querydiagnosis.entity.icd10;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L2 {
    private String code;
    private String name;
    private HashMap<String, L3> l3map;

    public L2(String code, String name, HashMap<String, L3> l3map) {
        this.code = code;
        this.name = name;
        this.l3map = l3map;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, L3> getL3map() {
        return l3map;
    }

    public void setL3map(HashMap<String, L3> l3map) {
        this.l3map = l3map;
    }

    @Override
    public String toString() {
        return "L2{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", l3map=" + l3map +
                '}';
    }
}
