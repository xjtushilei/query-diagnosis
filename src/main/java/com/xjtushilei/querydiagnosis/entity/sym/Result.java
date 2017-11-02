package com.xjtushilei.querydiagnosis.entity.sym;

import org.javatuples.Pair;

import java.util.List;

/**
 * @author shilei
 * @Date 2017/11/2.
 */
public class Result {
    private Integer code;
    private String msg;

    private String diagnosisSum;
    private String recommendSymSum;

    //推荐的疾病症状
    List<Pair<String, Double>> recommendResult;
    //诊断的结果列表
    private List<Diagnosis> diagnosis;

    public Result() {
    }

    public Result(Integer code, String msg, String diagnosisSum, String recommendSymSum, List<Pair<String, Double>> recommendResult, List<Diagnosis> diagnosis) {
        this.code = code;
        this.msg = msg;
        this.diagnosisSum = diagnosisSum;
        this.recommendSymSum = recommendSymSum;
        this.recommendResult = recommendResult;
        this.diagnosis = diagnosis;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getDiagnosisSum() {
        return diagnosisSum;
    }

    public void setDiagnosisSum(String diagnosisSum) {
        this.diagnosisSum = diagnosisSum;
    }

    public String getRecommendSymSum() {
        return recommendSymSum;
    }

    public void setRecommendSymSum(String recommendSymSum) {
        this.recommendSymSum = recommendSymSum;
    }

    public List<Pair<String, Double>> getRecommendResult() {
        return recommendResult;
    }

    public void setRecommendResult(List<Pair<String, Double>> recommendResult) {
        this.recommendResult = recommendResult;
    }

    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Diagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }
}
