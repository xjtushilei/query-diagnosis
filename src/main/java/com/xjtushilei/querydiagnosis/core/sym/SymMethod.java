package com.xjtushilei.querydiagnosis.core.sym;

import com.xjtushilei.querydiagnosis.entity.sym.*;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.ImmutableTriple;

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
        Result result = diagnosis(Arrays.asList("呕吐", "上吐下泻", "胃热疼痛", "稀便"), step1DiagnosisResult, 6);
        print(result);
    }

    public static Result diagnosis(List<String> inputList, List<HashMap<String, Object>> step1DiagnosisResult, int
            returnMaxSyms) {
        //得到第一步的诊断结果，根据code进行限制
        HashSet<String> step1Codes = new HashSet<>();
        //得到第一步的诊断结果，得到rate，方便后续进行计算疾病的概率从而进行排序
        HashMap<String, ImmutableTriple<String, String, Double>> step1Rate = new HashMap<>();

        step1DiagnosisResult.forEach((HashMap<String, Object> map) -> {
            step1Codes.add((String) map.get("code"));
            ((ArrayList<ImmutableTriple<String, String, Double>>) map.get("analysis")).forEach(t -> {
                step1Rate.put(t.getLeft(), t);
            });
        });
        //待处理的L2sym,只包含1个大类科室且患了该病
        ArrayList<L2Sym> L2symLess = new ArrayList<>();
        //目前所有特征的概率
        ArrayList<SymptomProbability> rateMatrix = new ArrayList<>();
        //得到只有1个level2的疾病们，并初始化特征矩阵
        HashMap<String, L2Sym> l2SymMap = DealData.getSymFromJson();
        assert l2SymMap != null;
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
        //计算每一个症状的概率
        for (String namei : inputList) {
            for (SymptomProbability j : rateMatrix) {
                j.getRateList().add(calculatePs(L2symLess, namei, j.getName()));
            }
        }
        //将上一步求出的n（症状的个数）个概率进行融合
        for (SymptomProbability j : rateMatrix) {
            //设置排序概率的公式，目前是n个症状的肚子的概率利用“概率加法公式”进行融合，然后再和0.5进行比较，再平方，再用1减。
            j.setOurRate(1 - Math.pow(calculatePlus(j.getRateList()) - 0.5, 2));
        }
        //得到最值得推荐的症状的概率，之后直接取top-n就行了
        rateMatrix.sort((l1, l2) -> Double.compare(l2.getOurRate(), l1.getOurRate()));


        //计算每个疾病的概率 公式是: 京伟给的该疾病的概率 *【症状命中个数+系数M*(n个患有的症状对该疾病的贡献率的和)】
        // 这个系数M我们之后进行训练.因为后面的每个症状对该疾病的贡献率没有，我们暂时取:M=0，
        //TODO 这里还有一些问题，目前是按照所有症状对疾病的贡献率都是1算的

        // 系数M
        double M = 0;
        for (L2Sym l2Sym : L2symLess) {
            double rate = 0;
            //n个患有的症状对该疾病的贡献率的和
            for (Sym sym : l2Sym.getSufferSymMap().values()) {
                rate = rate + sym.getRate();
            }
            //【症状命中个数+系数M*(n个患有的症状对该疾病的贡献率的和)】
            rate = (double) l2Sym.getSufferSymMap().size() + M * (rate);
            //京伟给的该疾病的概率 *【症状命中个数+系数M*(n个患有的症状对该疾病的贡献率的和)】
            if (step1Rate.containsKey(l2Sym.getCodeL2())) {
                rate = rate * step1Rate.get(l2Sym.getCodeL2()).getRight();
            }
            //若京伟没有给概率，将该疾病设置为0
            else {
                rate = rate * 0;
            }
            l2Sym.setRate(rate);
        }
        //得到疾病概率的排序
        L2symLess.sort((l1, l2) -> Double.compare(l2.getRate(), l1.getRate()));


        //放到result的推荐疾病症状列表
        List<ImmutablePair<String, Double>> recommendResult = new ArrayList<>();
        for (int i = 0; i < rateMatrix.size() && i < returnMaxSyms; i++) {
            SymptomProbability symptomProbability = rateMatrix.get(i);
            recommendResult.add(new ImmutablePair<>(symptomProbability.getName(), symptomProbability.getOurRate()));
        }
        //放到result的诊断结果列表
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

    // 计算症状j 在症状i出现的情况下症状j出现的概率
    private static double calculatePs(ArrayList<L2Sym> l2symLess, String namei, String namej) {
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

    //n个事件的概率加法公式计算
    private static double calculatePlus(List<Double> rateList) {
        double P = 0;
        for (int i = 0; i < rateList.size(); i++) {
            if (i == 0) {
                P = rateList.get(i);
            } else {
                P = P + rateList.get(i) - P * rateList.get(i);
            }
        }
        return P;
    }


}
