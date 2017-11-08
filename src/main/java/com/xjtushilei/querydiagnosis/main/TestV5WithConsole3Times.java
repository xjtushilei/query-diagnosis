package com.xjtushilei.querydiagnosis.main;

import com.xjtushilei.querydiagnosis.entity.sym.Input;
import com.xjtushilei.querydiagnosis.entity.sym.Result;
import org.apache.commons.lang3.tuple.ImmutablePair;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;
import static com.xjtushilei.querydiagnosis.core.sym.SymMethod.diagnosis;
import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;


/**
 * @author shilei
 * @Date 2017/11/7.
 */
public class TestV5WithConsole3Times {
    public static void main(String[] args) {

        //每一轮最多推荐几个症状
        int maxRecommendSym = 6;

        //得到jingwei的诊断分类结果，包含概率
        Input input = diagnosisFirst();


        //第0轮的输入，只有京伟的诊断结果中的症状。之后把用户的最新的选择一直往这个里面加进去就行了。
        List<String> newInput = new ArrayList<>();
        newInput.addAll(input.getInput());
        //所有用户没有选择的症状。第一次的时候是没有的。
        List<String> noUseInputList = new ArrayList<>();

        //第0轮结果
        Result result = diagnosis(newInput, input, maxRecommendSym, noUseInputList);

        System.out.println("输入提示：回复数字编号，多个请用空格分割。最后按一个enter键确认！");
        //第1轮的选择
        System.out.println("------------------------------------");
        List<ImmutablePair<String, Double>> recommendResult = result.getRecommendResult();
        for (int i = 0; i < recommendResult.size(); i++) {
            System.out.println(i + ":" + recommendResult.get(i).getLeft());
        }
        System.out.println(recommendResult.size() + ":" + "以上都没有");
        System.out.println("------------------------------------");
        System.out.println("Round 1 : 请选择以上几个症状您是否患有？");
        //获取用户输入
        Scanner sc = new Scanner(System.in);
        String userInput = sc.next();
        List<String> userInputNumList = Arrays.asList(userInput.trim().split(" "));

        int roundSum = 3;
        //如果用户选择了“以上都没有”，并且初始轮是不正常推荐，则终止推荐.用户选择了部分高频率症状，则多加一轮推荐。
        if (!result.isNormalRecommendation()) {
            System.out.println("初始的症状没有一个出现在疾病中!");
            if (userInputNumList.contains(String.valueOf(recommendResult.size()))) {
                System.out.println("很抱歉我们无法诊断您！");
                System.exit(0);
            } else {
                //循环伦数＋1
                System.out.println("round加1");
                roundSum++;
            }
        }
        for (int i = 0; i < recommendResult.size(); i++) {

            if (userInputNumList.contains(String.valueOf(i))) {
                newInput.add(recommendResult.get(i).getLeft());
            } else {
                noUseInputList.add(recommendResult.get(i).getLeft());
            }
        }
        //第1轮结果
        result = diagnosis(newInput, input, maxRecommendSym, noUseInputList);


        for (int roundnow = 2; roundnow <= roundSum; roundnow++) {
            //第x轮的选择
            System.out.println("------------------------------------");
            recommendResult = result.getRecommendResult();
            for (int i = 0; i < recommendResult.size(); i++) {
                System.out.println(i + ":" + recommendResult.get(i).getLeft());
            }
            System.out.println(recommendResult.size() + ":" + "以上都没有");
            System.out.println("------------------------------------");
            System.out.println("Round " + roundnow + " : 请选择以上几个症状您是否患有");
            //获取用户输入
            sc = new Scanner(System.in);
            userInput = sc.next();
            userInputNumList = Arrays.asList(userInput.trim().split(" "));
            for (int i = 0; i < recommendResult.size(); i++) {

                if (userInputNumList.contains(String.valueOf(i))) {
                    newInput.add(recommendResult.get(i).getLeft());
                } else {
                    noUseInputList.add(recommendResult.get(i).getLeft());
                }
            }
            //第x轮结果
            result = diagnosis(newInput, input, maxRecommendSym, noUseInputList);
        }


        //打印最后的结果
        print(result);

    }
}
