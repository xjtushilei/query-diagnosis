package com.xjtushilei.querydiagnosis.entity.icd10;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L1 {
    private String code;
    private String name;
    private HashMap<String, L2> l2map;

    public L1(String code, String name, HashMap<String, L2> l2map) {
        this.code = code;
        this.name = name;
        this.l2map = l2map;
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

    public HashMap<String, L2> getL2map() {
        return l2map;
    }

    public void setL2map(HashMap<String, L2> l2map) {
        this.l2map = l2map;
    }

    @Override
    public String toString() {
        return "L1{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", l2map=" + l2map +
                '}';
    }
}
