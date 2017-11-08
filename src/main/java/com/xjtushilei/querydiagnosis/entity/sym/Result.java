package com.xjtushilei.querydiagnosis.entity.sym;

import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.List;

/**
 * @author shilei
 * @Date 2017/11/2.
 */
public class Result {
    private Integer code;
    private String msg;
    private boolean normalRecommendation;

    //推荐的疾病症状
    List<ImmutablePair<String, Double>> recommendResult;
    //诊断的结果列表
    private List<Diagnosis> diagnosis;

    public Result() {
        this.normalRecommendation = true;
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


    public List<ImmutablePair<String, Double>> getRecommendResult() {
        return recommendResult;
    }

    public void setRecommendResult(List<ImmutablePair<String, Double>> recommendResult) {
        this.recommendResult = recommendResult;
    }

    public List<Diagnosis> getDiagnosis() {
        return diagnosis;
    }

    public void setDiagnosis(List<Diagnosis> diagnosis) {
        this.diagnosis = diagnosis;
    }

    public boolean isNormalRecommendation() {
        return normalRecommendation;
    }

    public void setNormalRecommendation(boolean normalRecommendation) {
        this.normalRecommendation = normalRecommendation;
    }
}
