package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/11/2.
 */
public class Diagnosis {
    // id
    private String codeL3;
    private String nameL3;
    private int SymSum;
    private double RateSum;
    private HashMap<String, Sym> sufferSymMap;

    public Diagnosis() {
    }

    public Diagnosis(String codeL3, String nameL3, int symSum, double rateSum, HashMap<String, Sym> sufferSymMap) {
        this.codeL3 = codeL3;
        this.nameL3 = nameL3;
        SymSum = symSum;
        RateSum = rateSum;
        this.sufferSymMap = sufferSymMap;
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

    public int getSymSum() {
        return SymSum;
    }

    public void setSymSum(int symSum) {
        SymSum = symSum;
    }

    public double getRateSum() {
        return RateSum;
    }

    public void setRateSum(double rateSum) {
        RateSum = rateSum;
    }

    public HashMap<String, Sym> getSufferSymMap() {
        return sufferSymMap;
    }

    public void setSufferSymMap(HashMap<String, Sym> sufferSymMap) {
        this.sufferSymMap = sufferSymMap;
    }
}
