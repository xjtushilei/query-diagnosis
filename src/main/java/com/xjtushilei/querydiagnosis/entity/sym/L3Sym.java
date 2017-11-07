package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L3Sym {
    // id
    private String codeL3;
    private String nameL3;
    private HashMap<String, Sym> allSymMap;
    private HashMap<String, Sym> sufferSymMap;
    private double rate;

    public L3Sym() {
    }

    public L3Sym(String nameL3, String codeL3) {
        this.codeL3 = codeL3;
        this.nameL3 = nameL3;
        this.allSymMap = new HashMap<>();
        this.sufferSymMap = new HashMap<>();
        this.rate = 0.0;
    }

    public String getCodeL3() {
        return codeL3;
    }

    public void setCodeL3(String codeL3) {
        this.codeL3 = codeL3;
    }

    public String getNameL3() {
        return nameL3;
    }

    public void setNameL3(String nameL3) {
        this.nameL3 = nameL3;
    }

    public HashMap<String, Sym> getAllSymMap() {
        return allSymMap;
    }

    public void setAllSymMap(HashMap<String, Sym> allSymMap) {
        this.allSymMap = allSymMap;
    }

    public HashMap<String, Sym> getSufferSymMap() {
        return sufferSymMap;
    }

    public void setSufferSymMap(HashMap<String, Sym> sufferSymMap) {
        this.sufferSymMap = sufferSymMap;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

}
