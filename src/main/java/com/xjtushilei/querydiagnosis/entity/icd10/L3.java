package com.xjtushilei.querydiagnosis.entity.icd10;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L3 {
    private String code;
    private String name;
    private HashMap<String, L4> l4map;

    public L3(String code, String name, HashMap<String, L4> l4map) {
        this.code = code;
        this.name = name;
        this.l4map = l4map;
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

    public HashMap<String, L4> getL4map() {
        return l4map;
    }

    public void setL4map(HashMap<String, L4> l4map) {
        this.l4map = l4map;
    }

    @Override
    public String toString() {
        return "L3{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", l4map=" + l4map +
                '}';
    }
}
