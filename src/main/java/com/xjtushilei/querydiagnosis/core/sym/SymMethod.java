package com.xjtushilei.querydiagnosis.core.sym;

import com.xjtushilei.querydiagnosis.entity.icd10.L1;
import com.xjtushilei.querydiagnosis.entity.sym.L2Sym;
import com.xjtushilei.querydiagnosis.entity.sym.SymptomProbability;
import org.apache.commons.math3.stat.descriptive.summary.Sum;

import java.util.*;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;
import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;
import static com.xjtushilei.querydiagnosis.utils.GsonUtils.printBeautiful;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class SymMethod {
    public static void main(String[] args) {
        //得到jingwei的诊断分类结果，包含概率
        List<HashMap<String, Object>> step1DiagnosisResult = diagnosisFirst();
        //        getSymptomRecommend(Arrays.asList("呕吐", "上吐下泻"));
        diagnosis(Arrays.asList("呕吐", "上吐下泻", "胃热疼痛", "稀便"), step1DiagnosisResult);
    }

    public static List<L2Sym> diagnosis(List<String> inputList, List<HashMap<String, Object>> step1DiagnosisResult) {
        //得到第一步的诊断结果，根据code进行限制
        HashSet<String> step1Codes = new HashSet<>();

        step1DiagnosisResult.forEach(map -> {
            step1Codes.add((String) map.get("code"));
        });
        //待处理的L2sym,只包含两个大类科室
        ArrayList<L2Sym> L2symLess = new ArrayList<>();
        //目前所有特征的概率
        ArrayList<SymptomProbability> rateMatrix = new ArrayList<>();
        //得到只有两个level2的疾病们，并初始化特征矩阵
        HashMap<String, L2Sym> l2SymMap = DealData.getSymFromJson();
        l2SymMap.values().forEach(l2Sym -> {
            //根据第一部的结果，删除掉无关的level2
            if (step1Codes.contains(l2Sym.getCodeL1())) {
                l2Sym.getAllSymMap().forEach((symName, sym) -> {
                    if (inputList.contains(symName)) {
                        //将患病症状统计
                        l2Sym.getSufferSymMap().put(symName, sym);
                        //将该疾病的其他症状加入待计算列表里
                        l2Sym.getAllSymMap().values().forEach(s -> {
                            SymptomProbability symptomProbability = new SymptomProbability(s.getName(), s.getRate());
                            if (!rateMatrix.contains(symptomProbability) && !inputList.contains(s.getName())) {
                                rateMatrix.add(symptomProbability);
                            }
                        });
                    }
                });
                if (l2Sym.getSufferSymMap().size() > 0) {
                    L2symLess.add(l2Sym);
                }
            }
        });

        for (String namei : inputList) {
            for (SymptomProbability j : rateMatrix) {
                j.getRateList().add(calculatePs(L2symLess,namei,j.getName()));
            }
        }
        //计算每一个症状的概率。将上一步求出的概率相加
        for (SymptomProbability j : rateMatrix) {
            double rateSum=0;
            for (Double d:j.getRateList()){
                rateSum=rateSum+d;
            }
            j.setOurRate(rateSum);
//            j.setOurRate((1-Math.pow(rateSum-0.5,2)));
        }
        Collections.sort(rateMatrix,(l1,l2)-> Double.compare(l2.getOurRate(),l1.getOurRate()));
        print(rateMatrix);


        return L2symLess;
    }

    // 计算在症状j在一致症状i出现的情况下j出现的概率
    public static double calculatePs(ArrayList<L2Sym> l2symLess,String namei,String namej){
        double Cj=0;
        double Cij=0;
        for ( L2Sym l2Sym:l2symLess){
            Set<String> keySet=l2Sym.getAllSymMap().keySet();
            if (keySet.contains(namej)){
                Cj++;
                if (keySet.contains(namei)){
                    Cij++;
                }
            }
        }
        return Cij/Cj;
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
