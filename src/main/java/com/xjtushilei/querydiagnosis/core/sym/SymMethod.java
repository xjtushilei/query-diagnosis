package com.xjtushilei.querydiagnosis.core.sym;

import com.xjtushilei.querydiagnosis.entity.sym.*;
import org.apache.commons.lang3.tuple.ImmutablePair;

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
        Input input = diagnosisFirst();
        //        getSymptomRecommend(Arrays.asList("呕吐", "上吐下泻"));
        Result result = diagnosis(input.getInput(), input, 6, Arrays.asList());
        print(result);
    }

    /**
     * @param inputList      用户输入的症状
     * @param input          京伟的的诊断结果
     * @param returnMaxSyms  最大推荐的症状个数
     * @param noUseInputList 用户在所有轮中没有选择的症状
     * @return 诊断结果
     */
    public static Result diagnosis(List<String> inputList, Input input, int
            returnMaxSyms, List<String> noUseInputList) {
        Result result = new Result();


        HashMap<String, Double> diseaseRateMap = new HashMap<>();
        for (int i = 0; i < input.getL3name().size(); i++) {
            diseaseRateMap.put(input.getL3name().get(i), input.getL3rate().get(i));
        }

        //待处理的L3sym
        ArrayList<L3Sym> L3symLess = new ArrayList<>();
        //目前所有特征的概率
        ArrayList<SymptomProbability> rateMatrix = new ArrayList<>();
        //所有level3的疾病们
        HashMap<String, L3Sym> l3SymMap = DealData.getSymFromOriginalFile();
        assert l3SymMap != null;
        final int[] countFlag = {0};
        l3SymMap.values().forEach(l3Sym -> {
            //根据第一部的结果，删除掉无关的level3
            if (input.getL3name().contains(l3Sym.getNameL3())) {
                l3Sym.getAllSymMap().forEach((symName, sym) -> {
                    //                    System.out.println(symName);
                    if (inputList.contains(symName)) {
                        //将患病症状统计
                        l3Sym.getSufferSymMap().put(symName, sym);
                        countFlag[0] = countFlag[0] + 1;
                    }
                    //将该疾病的其他症状加入待计算列表里
                    l3Sym.getAllSymMap().values().forEach(s -> {
                        SymptomProbability symptomProbability = new SymptomProbability(s.getName(), s.getRate());
                        if (!rateMatrix.contains(symptomProbability) && !inputList.contains(s.getName())) {
                            rateMatrix.add(symptomProbability);
                        }
                    });
                });
                L3symLess.add(l3Sym);
            }
        });
        if (countFlag[0] > 0) {
            //计算每一个症状的概率
            for (String namei : inputList) {
                for (SymptomProbability j : rateMatrix) {
                    j.getRateList().add(calculatePs(L3symLess, namei, j.getName()));
                }
            }
            //将上一步求出的n（症状的个数）个概率进行融合
            for (SymptomProbability j : rateMatrix) {
                //设置排序概率的公式，目前是n个症状的肚子的概率利用“概率加法公式”进行融合，然后再和0.5进行比较，再平方，再用1减。
                j.setOurRate(1 - Math.pow(calculatePlus(j.getRateList()) - 0.5, 2));
            }
        }
        // 如果没有匹配到症状，则直接利用苏丽娟给的概率，选出最常见的几个症状。
        else {
            result.setNormalRecommendation(false);
            for (SymptomProbability j : rateMatrix) {
                j.setOurRate(j.getRate());
            }
        }
        //得到最值得推荐的症状的概率，之后直接取top-n就行了
        rateMatrix.sort((l1, l2) -> Double.compare(l2.getOurRate(), l1.getOurRate()));


        //计算每个疾病的概率 公式是: 京伟给的该疾病的概率 *【症状命中个数+系数M*(n个患有的症状对该疾病的贡献率的和)】
        // 这个系数M我们之后进行训练.因为后面的每个症状对该疾病的贡献率没有，我们暂时取:M=0，
        //TODO 这里还有一些问题，目前是按照所有症状对疾病的贡献率都是1算的

        // 系数M
        double M = 0;
        for (L3Sym l3Sym : L3symLess) {
            double rate = 0;
            //n个患有的症状对该疾病的贡献率的和
            for (Sym sym : l3Sym.getSufferSymMap().values()) {
                rate = rate + sym.getRate();
            }
            //【症状命中个数+系数M*(n个患有的症状对该疾病的贡献率的和)】
            rate = (double) l3Sym.getSufferSymMap().size() + M * (rate);
            //京伟给的该疾病的概率 *【症状命中个数+系数M*(n个患有的症状对该疾病的贡献率的和)】
            if (diseaseRateMap.containsKey(l3Sym.getNameL3())) {
                rate = rate * diseaseRateMap.get(l3Sym.getNameL3());
            }
            //若京伟没有给概率，将该疾病设置为0
            else {
                rate = rate * 0;
            }
            l3Sym.setRate(rate);
        }
        //得到疾病概率的排序
        L3symLess.sort((l1, l2) -> Double.compare(l2.getRate(), l1.getRate()));


        //放到result的推荐疾病症状列表
        List<ImmutablePair<String, Double>> recommendResult = new ArrayList<>();
        for (int i = 0; i < rateMatrix.size() && recommendResult.size() < returnMaxSyms; i++) {

            SymptomProbability symptomProbability = rateMatrix.get(i);
            //如果该症状上一次用户没有选，则下次不推荐.
            if (noUseInputList.contains(symptomProbability.getName())) {
                continue;
            }
            recommendResult.add(new ImmutablePair<>(symptomProbability.getName(), symptomProbability.getOurRate()));
        }
        //放到result的诊断结果列表
        List<Diagnosis> diagnosis = new ArrayList<>();
        L3symLess.forEach(l3Sym -> {
            Diagnosis d = new Diagnosis(l3Sym.getCodeL3(), l3Sym.getNameL3(), l3Sym.getSufferSymMap().size(), l3Sym
                    .getRate(), l3Sym.getSufferSymMap());
            diagnosis.add(d);

        });

        result.setCode(200);
        result.setMsg("推荐成功!");
        result.setDiagnosis(diagnosis);
        result.setRecommendResult(recommendResult);

        return result;
    }

    // 计算症状j 在症状i出现的情况下症状j出现的概率
    private static double calculatePs(ArrayList<L3Sym> l3symLess, String namei, String namej) {
        double Cj = 0;
        double Cij = 0;
        for (L3Sym l3Sym : l3symLess) {
            Set<String> keySet = l3Sym.getAllSymMap().keySet();
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
