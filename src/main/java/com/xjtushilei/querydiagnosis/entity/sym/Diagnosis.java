package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/11/2.
 */
public class Diagnosis {
    private String codeL1;
    private String nameL1;

    // id
    private String codeL2;
    private String nameL2;
    private int SymSum;
    private double RateSum;
    private HashMap<String, Sym> sufferSymMap;

    public Diagnosis() {
    }

    public Diagnosis(String codeL1, String nameL1, String codeL2, String nameL2, int symSum, double rateSum, HashMap<String, Sym> sufferSymMap) {
        this.codeL1 = codeL1;
        this.nameL1 = nameL1;
        this.codeL2 = codeL2;
        this.nameL2 = nameL2;
        SymSum = symSum;
        RateSum = rateSum;
        this.sufferSymMap = sufferSymMap;
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
