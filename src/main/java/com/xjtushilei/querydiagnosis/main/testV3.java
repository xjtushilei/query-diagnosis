package com.xjtushilei.querydiagnosis.main;

import com.xjtushilei.querydiagnosis.core.lucene.search.Search;
import com.xjtushilei.querydiagnosis.entity.sym.Result;

import java.util.*;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;
import static com.xjtushilei.querydiagnosis.core.sym.SymMethod.diagnosis;
import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class testV3 {
    public static void main(String[] args) {
        //得到jingwei的诊断分类结果，包含概率
        List<HashMap<String, Object>> diagnosisResult = new ArrayList<>();
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", "消化系统疾病");
        temp.put("code", "K00-K93");
        temp.put("rate", 0.95);
        diagnosisResult.add(temp);
        //得到jingwei的诊断分类结果，包含概率
        List<HashMap<String, Object>> step1DiagnosisResult = diagnosisFirst();


        //3天前嗓子疼，发烧，肚子疼就诊，口腔轻微疱疹，吃药3天，肚子还是疼，做彩超肠系膜淋巴结肿大，初诊急性肠胃炎

        //数字是返回前几个推荐症状
        Result result= diagnosis(Arrays.asList("肚子疼", "腹泻是急性肠", "孕妇拉肚子", "肚脐疼"), step1DiagnosisResult,10);
        print(result);

    }
}
