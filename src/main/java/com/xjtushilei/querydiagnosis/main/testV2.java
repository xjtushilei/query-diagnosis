package com.xjtushilei.querydiagnosis.main;

import com.xjtushilei.querydiagnosis.core.lucene.search.Search;
import com.xjtushilei.querydiagnosis.core.sym.SymMethod;

import java.util.*;

import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class testV2 {
    public static void main(String[] args) {
        //得到jingwei的诊断分类结果，包含概率
        List<HashMap<String, Object>> diagnosisResult = new ArrayList<>();
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", "内分泌、营养和代谢疾病");
        temp.put("code", "E00-E90");
        temp.put("rate", 0.6);
        diagnosisResult.add(temp);
        temp = new HashMap<>();
        temp.put("name", "消化系统疾病");
        temp.put("code", "K00-K93");
        temp.put("rate", 0.95);
        diagnosisResult.add(temp);

        //得到jingwei的识别实体
        List<String> input = Arrays.asList("胃难受", "难受吐", "拉肚子", "头疼", "恶心");
        //        List<String> input = Arrays.asList("胃难受   每次都吐    有时拉肚子    头疼    一着凉胃就难受吐    最近隔三差五胃就难受   去挂消炎水");

        //进行查询
        LinkedHashMap<String, Float> searchResult = Search.searchAll(input, diagnosisResult, 2);
        print(searchResult);
        print(SymMethod.getSymptomRecommend(searchResult, diagnosisResult, 10));

    }
}
