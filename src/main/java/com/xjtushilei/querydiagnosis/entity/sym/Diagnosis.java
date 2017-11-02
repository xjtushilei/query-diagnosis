package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.HashMap;

/**
 * @author shilei
 * @Date 2017/11/2.
 */
public class Diagnosis {
    private String name;
    private String code;
    private int SymSum;
    private double RateSum;
    private HashMap<String, Double> allSufferSym;

    public Diagnosis() {
    }

    public Diagnosis(String name, String code, int symSum, double rateSum, HashMap<String, Double> allSufferSym) {
        this.name = name;
        this.code = code;
        SymSum = symSum;
        RateSum = rateSum;
        this.allSufferSym = allSufferSym;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
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

    public HashMap<String, Double> getAllSufferSym() {
        return allSufferSym;
    }

    public void setAllSufferSym(HashMap<String, Double> allSufferSym) {
        this.allSufferSym = allSufferSym;
    }
}
