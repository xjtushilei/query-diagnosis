package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class L2Sym {
    private String codeL1;
    private String nameL1;

    // id
    private String codeL2;
    private String nameL2;
    private HashMap<String, Sym> allSymMap;
    private HashMap<String, Sym> sufferSymMap;
    private double rate;

    public L2Sym() {
    }

    public L2Sym(String codeL1, String nameL1, String codeL2, String nameL2) {
        this.codeL1 = codeL1;
        this.nameL1 = nameL1;
        this.codeL2 = codeL2;
        this.nameL2 = nameL2;
        this.allSymMap = new HashMap<>();
        this.sufferSymMap =  new HashMap<>();
        this.rate = 0.0;
    }

    @Override
    public String toString() {
        return "L2Sym{" +
                "codeL1='" + codeL1 + '\'' +
                ", nameL1='" + nameL1 + '\'' +
                ", codeL2='" + codeL2 + '\'' +
                ", nameL2='" + nameL2 + '\'' +
                ", allSymMap=" + allSymMap +
                ", sufferSymMap=" + sufferSymMap +
                ", rate=" + rate +
                '}';
    }

    public String getCodeL1() {
        return codeL1;
    }

    public void setCodeL1(String codeL1) {
        this.codeL1 = codeL1;
    }

    public String getNameL1() {
        return nameL1;
    }

    public void setNameL1(String nameL1) {
        this.nameL1 = nameL1;
    }

    public String getCodeL2() {
        return codeL2;
    }

    public void setCodeL2(String codeL2) {
        this.codeL2 = codeL2;
    }

    public String getNameL2() {
        return nameL2;
    }

    public void setNameL2(String nameL2) {
        this.nameL2 = nameL2;
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
