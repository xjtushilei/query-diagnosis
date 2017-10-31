package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L2Sym {
    private String code;
    private String name;
    private HashMap<String, Sym> symMap;

    public L2Sym(String code, String name, HashMap<String, Sym> symMap) {
        this.code = code;
        this.name = name;
        this.symMap = symMap;
    }

    @Override
    public String toString() {
        return "L2Sym{" +
                "code='" + code + '\'' +
                ", name='" + name + '\'' +
                ", symMap=" + symMap +
                '}';
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

    public HashMap<String, Sym> getSymMap() {
        return symMap;
    }

    public void setSymMap(HashMap<String, Sym> symMap) {
        this.symMap = symMap;
    }
}
