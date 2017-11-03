package com.xjtushilei.querydiagnosis.core.sym;

import com.xjtushilei.querydiagnosis.entity.sym.Diagnosis;
import com.xjtushilei.querydiagnosis.entity.sym.L2Sym;
import com.xjtushilei.querydiagnosis.entity.sym.Result;
import com.xjtushilei.querydiagnosis.entity.sym.SymptomProbability;
import org.javatuples.Pair;

import java.util.*;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;
import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class SymMethod {
    public static void main(String[] args) {
        //得到jingwei的诊断分类结果，包含概率
        List<HashMap<String, Object>> step1DiagnosisResult = diagnosisFirst();
        //        getSymptomRecommend(Arrays.asList("呕吐", "上吐下泻"));
        Result result= diagnosis(Arrays.asList("呕吐", "上吐下泻", "胃热疼痛", "稀便"), step1DiagnosisResult,6);
        print(result);
    }

    public static Result diagnosis(List<String> inputList, List<HashMap<String, Object>> step1DiagnosisResult, int
            returnMaxSyms) {
        //得到第一步的诊断结果，根据code进行限制
        HashSet<String> step1Codes = new HashSet<>();

        step1DiagnosisResult.forEach(map -> {
            step1Codes.add((String) map.get("code"));
        });
        //待处理的L2sym,只包含1个大类科室且患了该病
        ArrayList<L2Sym> L2symLess = new ArrayList<>();
        //目前所有特征的概率
        ArrayList<SymptomProbability> rateMatrix = new ArrayList<>();
        //得到只有1个level2的疾病们，并初始化特征矩阵
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
                j.getRateList().add(calculatePs(L2symLess, namei, j.getName()));
            }
        }
        //计算每一个症状的概率。将上一步求出的概率相加
        for (SymptomProbability j : rateMatrix) {
            double rateSum = 0;
            for (Double d : j.getRateList()) {
                rateSum = rateSum + d;
            }
            //            j.setOurRate(rateSum);
            //设置排序概率的公式，目前是n个症状的肚子的概率求平均，然后再和0.5进行比较。
            j.setOurRate((1 - Math.pow(rateSum / (j.getRateList().size()) - 0.5, 2)));
        }
        //得到最值得推荐的症状的概率，之后直接取top-n就行了
        Collections.sort(rateMatrix, (l1, l2) -> Double.compare(l2.getOurRate(), l1.getOurRate()));

        //计算每个疾病的概率
        for (L2Sym l2Sym : L2symLess) {
            double allHit = 0;
            for (String namei : inputList) {
                double Csi = 0;
                double Dsum = L2symLess.size(); //疾病的种数
                for (L2Sym l2Sym2 : L2symLess) {
                    if (l2Sym2.getAllSymMap().containsKey(namei) && l2Sym2.getNameL2().equals(l2Sym.getNameL2())) {
                        Csi++;
                    }
                }
                //                System.out.println(Csi+"\t"+Dsum);
                allHit = allHit + (1 - Csi / Dsum);
            }
            allHit = allHit / inputList.size();
//            System.out.println(allHit);
            l2Sym.setRate(allHit);
        }
        Collections.sort(L2symLess,(l1,l2)->Double.compare(l2.getRate(),l1.getRate()));


        //推荐的疾病症状
        List<Pair<String, Double>> recommendResult = new ArrayList<>();
        for (int i = 0; i < rateMatrix.size() && i < returnMaxSyms; i++) {
            SymptomProbability symptomProbability = rateMatrix.get(i);
            recommendResult.add(new Pair<>(symptomProbability.getName(), symptomProbability.getOurRate()));
        }
        //诊断的结果列表
        List<Diagnosis> diagnosis = new ArrayList<>();
        L2symLess.forEach(l2Sym -> {
            Diagnosis d = new Diagnosis(l2Sym.getCodeL1(), l2Sym.getNameL1(), l2Sym.getCodeL2(), l2Sym.getNameL2(), l2Sym
                    .getSufferSymMap().size(), l2Sym.getRate(), l2Sym.getSufferSymMap());
            diagnosis.add(d);

        });
        Result result = new Result();
        result.setCode(200);
        result.setMsg("推荐成功!");
        result.setDiagnosis(diagnosis);
        result.setRecommendResult(recommendResult);

        return result;
    }

    // 计算在症状j在一致症状i出现的情况下j出现的概率
    public static double calculatePs(ArrayList<L2Sym> l2symLess, String namei, String namej) {
        double Cj = 0;
        double Cij = 0;
        for (L2Sym l2Sym : l2symLess) {
            Set<String> keySet = l2Sym.getAllSymMap().keySet();
            if (keySet.contains(namej)) {
                Cj++;
                if (keySet.contains(namei)) {
                    Cij++;
                }
            }
        }
        return Cij / Cj;
    }


}
