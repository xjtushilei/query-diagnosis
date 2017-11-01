package com.xjtushilei.querydiagnosis.sym;

import com.xjtushilei.querydiagnosis.entity.icd10.L2;
import com.xjtushilei.querydiagnosis.entity.sym.Sym;
import com.xjtushilei.querydiagnosis.utils.FileUtils;

import java.util.*;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class SymMethod {
    public static void main(String[] args) {
        getSymptomRecommend(Arrays.asList("呕吐", "上吐下泻"));
        //        getDisease(Arrays.asList("呕吐", "上吐下泻", "胃热疼痛", "稀便"));
    }

    /**
     * 根据输入的症状，进行疾病查找分析
     *
     * @param input 用户输入的特征
     * @return 可能患有疾病的名字，suffer症状和all症状
     */
    private static HashMap<String, HashMap<String, HashMap<String, Float>>> analyzingDiseases(List<String> input) {
        List<String> disease15 = get15disease();
        HashMap<String, ArrayList<Sym>> symMap = DealData.getDealSymData();
        HashMap<String, HashMap<String, Float>> mySymMap = new HashMap<>();
        //构建一个我想要的map类型的数据结构，同时限制我们想要研究的疾病的数量
        symMap.forEach((s, list) -> {
            // 只保留15个疾病
            if (disease15.contains(s)) {
                HashMap<String, Float> tempMap = new HashMap<>();
                list.forEach(sym -> tempMap.put(sym.getName(), sym.getRate()));
                mySymMap.put(s, tempMap);
            }
        });

        // 可能患有疾病的名字，suffer症状和all症状
        HashMap<String, HashMap<String, HashMap<String, Float>>> symptomsOfIllness = new HashMap<>();
        mySymMap.forEach((s, m) -> {
            input.forEach(in -> {
                if (m.containsKey(in)) {
                    if (symptomsOfIllness.containsKey(s)) {
                        HashMap<String, Float> temp1 = new HashMap<>();
                        temp1.put(in, m.get(in));
                        symptomsOfIllness.get(s).get("suffer").put(in, m.get(in));
                    } else {
                        HashMap<String, HashMap<String, Float>> tempMap = new HashMap<>();
                        HashMap<String, Float> temp1 = new HashMap<>();
                        temp1.put(in, m.get(in));
                        tempMap.put("suffer", temp1);
                        tempMap.put("all", m);
                        symptomsOfIllness.put(s, tempMap);
                    }
                }
            });
        });
        return symptomsOfIllness;
    }

    /**
     * 推荐其他的疾病
     *
     * @param input 用户输入的特征
     * @return 推荐结果，包含概率
     */
    public static HashMap<String, Object> getSymptomRecommend(List<String> input) {
        HashMap<String, HashMap<String, HashMap<String, Float>>> symptomsOfIllness = analyzingDiseases(input);

        HashMap<String, Object> result = new HashMap<>();

        HashMap<String, Float> recommendResult = new HashMap<>();
        if (symptomsOfIllness.size() == 0) {
            result.put("code", 102);
            result.put("msg", ",没有得到任何推荐疾病，请检查您输入的症状！");
            return result;
        }
        HashMap<String, L2> icd10L2 = FileUtils.getIcd10DataLevel2();

        //开始推荐，其他症状
        symptomsOfIllness.forEach((s, m) -> {
            ArrayList<Sym> tempList = new ArrayList<>();
            m.get("all").forEach((symName, symRate) -> tempList.add(new Sym(symName, symRate)));
            Collections.sort(tempList, (s1, s2) -> Float.compare(s2.getRate(), s1.getRate()));

            // 15>symptomsOfIllness.size()>=6则选一个，<6则选两个
            //>=15 描述不清楚，请重新描述
            int recommendSize = 0;
            if (symptomsOfIllness.size() < 6) {
                recommendSize = 2;
            } else if (symptomsOfIllness.size() < 15) {
                recommendSize = 1;
            }

            for (int i = 0; i < tempList.size() && i < recommendSize; i++) {
                recommendResult.put(tempList.get(i).getName(), tempList.get(i).getRate());
            }
        });

        if (recommendResult.size() == 0) {
            result.put("code", 101);
            result.put("msg", "症状描述模糊，匹配到了大量的疾病症状，无法准确诊断相关疾病!");
            return result;
        }
        result.put("可能患有种类", symptomsOfIllness.size());
        result.put("code", 100);
        result.put("msg", "推荐成功！");
        result.put("recommend", recommendResult);
        return result;
    }

    /**
     * 推荐其他的疾病
     *
     * @param input 用户输入的特征
     * @return 推荐结果，包含概率
     */
    public static HashMap<String, Object> getDisease(List<String> input) {
        HashMap<String, HashMap<String, HashMap<String, Float>>> symptomsOfIllness = analyzingDiseases(input);
        HashMap<String, Object> result = new HashMap<>();

        List<HashMap<String, Object>> diagnosis = new ArrayList<>();
        if (symptomsOfIllness.size() == 0) {
            result.put("code", 101);
            result.put("msg", ",很抱歉，没有得到任何推荐！您可以尝试重新开始 ~ ");
            return result;
        }
        HashMap<String, L2> icd10L2 = FileUtils.getIcd10DataLevel2();
        //开始推荐，其他症状
        symptomsOfIllness.forEach((s, m) -> {

            HashMap<String, Object> tempMap = new HashMap<>();
            final float[] rateSum = {0};
            m.get("suffer").forEach((name, rate) -> {
                rateSum[0] = rateSum[0] + rate;
            });
            tempMap.put("name", icd10L2.get(s).getName());
            tempMap.put("code", icd10L2.get(s).getCode());
            tempMap.put("SymSum", m.get("suffer").size());
            tempMap.put("RateSum", rateSum[0] + m.get("suffer").size() * 8);
            tempMap.put("allSym", m.get("suffer"));
            diagnosis.add(tempMap);

        });
        Collections.sort(diagnosis, (m1, m2) -> {
            if (m1.get("SymSum") == m2.get("SymSum")) {
                return Float.compare((float) m2.get("RateSum"), (float) m1.get("RateSum"));
            } else {
                return (int) m2.get("SymSum") - (int) m1.get("SymSum");
            }
        });


        result.put("code", 100);
        result.put("msg", "诊断成功！");
        result.put("diagnosis", diagnosis);

        return result;
    }


    public static List<String> get15disease() {
        //25中疾病，对应level2，有重复，最后有18种level2
        return Arrays.asList("J40-J47",
                "N80-N98",
                "E00-E07",
                "E10-E14",
                "J09-J18",
                "J00-J06",
                "E20-E35",
                "M60-M79",
                "E50-E64",
                "K50-K52",
                "L20-L30",
                "N80-N98",
                "E20-E35",
                "F00-F09",
                "K00-K14",
                "J30-J39",
                "N60-N64",
                "J20-J22",
                "N70-N77",
                "N80-N98",
                "B00-B09",
                "L20-L30",
                "N70-N77"
        );
    }

}
