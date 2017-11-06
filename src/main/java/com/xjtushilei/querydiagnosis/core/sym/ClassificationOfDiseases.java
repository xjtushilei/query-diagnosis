package com.xjtushilei.querydiagnosis.core.sym;


import org.apache.commons.lang3.tuple.ImmutableTriple;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class ClassificationOfDiseases {

    public static void main(String[] args) {
        print(diagnosisFirst());
    }

    public static List<HashMap<String, Object>> diagnosisFirst() {
        List<HashMap<String, Object>> result = new ArrayList<>();
        HashMap<String, Object> temp = new HashMap<>();
        temp.put("name", "消化系统疾病");
        temp.put("code", "K00-K93");
        ArrayList<ImmutableTriple<String, String, Double>> dList = new ArrayList<>();
        dList.add(new ImmutableTriple("K00-K14", "口腔、涎腺和颌疾病", 0.5));
        dList.add(new ImmutableTriple("K20-K31", "食管、胃和十二指肠疾病", 0.8));
        dList.add(new ImmutableTriple("K35-K38", "阑尾疾病", 0.3));
        dList.add(new ImmutableTriple("K40-K46", "疝", 0.0));
        dList.add(new ImmutableTriple("K50-K52", "非感染性小肠炎和结肠炎", 1.0));
        dList.add(new ImmutableTriple("K55-K63", "肠的其他疾病", 1.0));
        dList.add(new ImmutableTriple("K65-K67", "腹膜疾病", 0.5));
        dList.add(new ImmutableTriple("K70-K77", "肝疾病", 0.4));
        dList.add(new ImmutableTriple("K80-K87", "胆囊、胆道和胰腺疾患", 0.3));
        dList.add(new ImmutableTriple("K90-K93", "消化系统的其他疾病", 1.0));
        temp.put("analysis", dList);
        result.add(temp);

        //假设有第二个L1，则继续添加下面的
        //        dList = new ArrayList<>();
        //        dList.add(new Triplet("K00-K14", "口腔、涎腺和颌疾病", 0.5));
        //        dList.add(new Triplet("K20-K31", "食管、胃和十二指肠疾病", 0.8));
        //        dList.add(new Triplet("K35-K38", "阑尾疾病", 0.3));
        //        dList.add(new Triplet("K40-K46", "疝", 0.0));
        //        dList.add(new Triplet("K50-K52", "非感染性小肠炎和结肠炎", 1.0));
        //        dList.add(new Triplet("K55-K63", "肠的其他疾病", 1.0));
        //        dList.add(new Triplet("K65-K67", "腹膜疾病", 0.5));
        //        dList.add(new Triplet("K70-K77", "肝疾病", 0.4));
        //        dList.add(new Triplet("K80-K87", "胆囊、胆道和胰腺疾患", 0.3));
        //        dList.add(new Triplet("K90-K93", "消化系统的其他疾病", 1.0));
        //        temp.put("analysis",dList);
        //        result.add(temp);




        return result;
    }

}
