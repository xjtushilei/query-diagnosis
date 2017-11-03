package com.xjtushilei.querydiagnosis.core.sym;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class ClassificationOfDiseases {

    public static void main(String[] args) {

    }

    public static List<HashMap<String, Object>> diagnosisFirst() {
        List<HashMap<String, Object>> result = new ArrayList<>();
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", "消化系统疾病");
        temp.put("code", "K00-K93");
        temp.put("rate", 0.95);
        result.add(temp);

        return result;
    }

}
