package com.xjtushilei.querydiagnosis.entity.sym;

import java.util.ArrayList;

/**
 * @author shilei
 * @Date 2017/11/2.
 */
public class SymptomProbability {


    private String name;
    private Double rate;
    private double ourRate;
    private ArrayList<Double> rateList;

    public SymptomProbability(String name, Double rate) {
        this.name = name;
        this.rate = rate;
        this.ourRate=0;
        this.rateList=new ArrayList<>();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SymptomProbability)) return false;

        SymptomProbability that = (SymptomProbability) o;

        return getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Double> getRateList() {
        return rateList;
    }

    public void setRateList(ArrayList<Double> rateList) {
        this.rateList = rateList;
    }

    public Double getRate() {
        return rate;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public double getOurRate() {
        return ourRate;
    }

    public void setOurRate(double ourRate) {
        this.ourRate = ourRate;
    }

    @Override
    public String toString() {
        return "SymptomProbability{" +
                "name='" + name + '\'' +
                ", rate=" + rate +
                ", rateList=" + rateList +
                '}';
    }
}
