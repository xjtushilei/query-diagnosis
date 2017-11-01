package com.xjtushilei.querydiagnosis.core.sym;

import com.xjtushilei.querydiagnosis.entity.icd10.L1;
import com.xjtushilei.querydiagnosis.entity.icd10.L2;
import com.xjtushilei.querydiagnosis.entity.sym.Sym;
import com.xjtushilei.querydiagnosis.utils.FileUtils;
import org.javatuples.Pair;

import java.util.*;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class SymMethod {
    public static void main(String[] args) {
        //得到jingwei的诊断分类结果，包含概率
        List<HashMap<String, Object>> diagnosisResult = diagnosisFirst();
        //        getSymptomRecommend(Arrays.asList("呕吐", "上吐下泻"));
        //        getDisease(Arrays.asList("呕吐", "上吐下泻", "胃热疼痛", "稀便"));
    }

    /**
     * 根据输入的症状，进行疾病查找分析
     *
     * @param diagnosisResult 第一步的诊断结果
     * @param searchResult    经过字面距离计算得到的特征
     * @return 可能患有疾病的名字，suffer症状和all症状
     */
    public static HashMap<String, HashMap<String, HashMap<String, Float>>> analyzingDiseases
    (LinkedHashMap<String, Float> searchResult, List<HashMap<String, Object>> diagnosisResult) {
        HashSet<String> diagnosisCode = new HashSet<>();
        HashMap<String, HashMap<String, ArrayList<Sym>>> symMapL1 = DealData.getDealSymDataL1();
        diagnosisResult.forEach(s -> {
            symMapL1.get(s.get("code")).forEach((s2, symsList) -> {
                diagnosisCode.add(s2);
            });
        });

        HashMap<String, ArrayList<Sym>> symMap = DealData.getDealSymDataL2();
        HashMap<String, HashMap<String, Float>> mySymMap = new HashMap<>();
        //构建一个我想要的map类型的数据结构，同时只保留有限个L1
        symMap.forEach((s, list) -> {
            if (diagnosisCode.contains(s)) {
                HashMap<String, Float> tempMap = new HashMap<>();
                list.forEach(sym -> tempMap.put(sym.getName(), sym.getRate()));
                mySymMap.put(s, tempMap);
            }
        });

        // 可能患有疾病的名字，suffer症状和all症状
        HashMap<String, HashMap<String, HashMap<String, Float>>> symptomsOfIllness = new HashMap<>();

        mySymMap.forEach((s, m) -> {
            searchResult.forEach((key, value) -> {
                if (m.containsKey(key)) {
                    if (symptomsOfIllness.containsKey(s)) {
                        HashMap<String, Float> temp1 = new HashMap<>();
                        temp1.put(key, m.get(key));
                        symptomsOfIllness.get(s).get("suffer").put(key, m.get(key));
                    } else {
                        HashMap<String, HashMap<String, Float>> tempMap = new HashMap<>();
                        HashMap<String, Float> temp1 = new HashMap<>();
                        temp1.put(key, m.get(key));
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
     * @param searchResult 经过字面距离计算得到的特征
     * @return 推荐结果，包含概率
     */
    public static HashMap<String, Object> getSymptomRecommend(LinkedHashMap<String, Float> searchResult, List<HashMap<String, Object>> diagnosisResult, int maxRecommendNum) {

        //可能患有疾病的名字，suffer症状和all症状
        HashMap<String, HashMap<String, HashMap<String, Float>>> symptomsOfIllness = analyzingDiseases(searchResult, diagnosisResult);

        HashMap<String, Object> result = new HashMap<>();

        //推荐的疾病症状
        List<Pair<String, Float>> recommendResult = new ArrayList<>();
        //当前的诊断结果
        List<HashMap<String, Object>> diagnosis = new ArrayList<>();


        if (symptomsOfIllness.size() == 0) {
            result.put("code", 102);
            result.put("msg", ",没有得到任何推荐疾病，请检查您输入的症状！");
            return result;
        }
        HashMap<String, L1> icd10L1 = FileUtils.getIcd10DataLevel1();
        HashMap<String, L2> icd10L2 = FileUtils.getIcd10DataLevel2();

        //目前已经有的症状
        HashSet<String> nowSyms = new HashSet<>();

        symptomsOfIllness.forEach((s, m) -> {
            m.get("suffer").forEach((k, v) -> {
                nowSyms.add(k);
            });
        });
        //开始分析
        symptomsOfIllness.forEach((s, m) -> {
            //推荐症状
            ArrayList<Sym> tempList = new ArrayList<>();
            m.get("all").forEach((symName, symRate) -> tempList.add(new Sym(symName, symRate)));
            Collections.sort(tempList, (s1, s2) -> Float.compare(s2.getRate(), s1.getRate()));
            //推荐几个候选
            int recommendSize = 1;
            if (symptomsOfIllness.size() < 6) {
                recommendSize = 2;
            }
            for (int i = 0; i < tempList.size() && i < recommendSize; i++) {
                if (!nowSyms.contains(tempList.get(i).getName())) {
                    recommendResult.add(new Pair<>(tempList.get(i).getName(), tempList.get(i).getRate()));
                }
            }


            //同步计算这时候的诊断结果
            HashMap<String, Object> tempMap = new HashMap<>();

            //该病所属的L1的概率
            final double[] l1rate = {0};
            diagnosisResult.forEach(diaM -> {
                if (icd10L1.get(diaM.get("code")).getL2map().containsKey(s)) {
                    l1rate[0] = (double) diaM.get("rate");
                }
            });
            if (l1rate[0] == 0) {
                System.out.println("?????????????????????");
            }
            final float[] rateSum = {0};
            m.get("suffer").forEach((name, rate) -> {
                //                if ()
                rateSum[0] = rateSum[0] + rate;
            });


            tempMap.put("name", icd10L2.get(s).getName());
            tempMap.put("code", icd10L2.get(s).getCode());
            tempMap.put("SymSum", m.get("suffer").size());
            tempMap.put("RateSum", l1rate[0] * rateSum[0] / m.get("suffer").size() + m.get("suffer").size() * 2);
            tempMap.put("RateSum", l1rate[0] * 2 / m.get("suffer").size() + m.get("suffer").size() * 2);
            tempMap.put("allSym", m.get("suffer"));
            diagnosis.add(tempMap);
        });


        if (recommendResult.size() == 0) {
            result.put("code", 101);
            result.put("msg", "症状描述模糊，匹配到了大量的疾病症状，无法准确诊断相关疾病!");
            return result;
        }

        //推荐症状排序
        Collections.sort(recommendResult, (p1, p2) -> Float.compare(p2.getValue1(), p1.getValue1()));
        if (recommendResult.size() > maxRecommendNum) {
            result.put("recommend", recommendResult.subList(0, maxRecommendNum));
        } else {
            result.put("recommend", recommendResult);
        }

        //诊断结果排序
        Collections.sort(diagnosis, (m1, m2) -> {
            //            if ((int)m1.get("SymSum") ==(int)m2.get("SymSum")){
            return Double.compare((double) m2.get("RateSum"), (double) m1.get("RateSum"));
            //            }
            //            else{
            //                return  Integer.compare((int) m2.get("SymSum"), (int) m1.get("SymSum"));
            //            }
        });

        result.put("推荐症状种类", symptomsOfIllness.size());
        result.put("diagnosis", diagnosis);
        result.put("诊断疾病种类", diagnosis.size());
        result.put("code", 100);
        result.put("msg", "推荐成功！");
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
