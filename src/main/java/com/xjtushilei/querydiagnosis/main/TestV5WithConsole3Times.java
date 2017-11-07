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
        //第0轮结果
        Result result = diagnosis(newInput, input, maxRecommendSym);


        //第1轮的选择
        System.out.println("------------------------------------");
        List<ImmutablePair<String, Double>> recommendResult = result.getRecommendResult();
        for (int i = 0; i < recommendResult.size(); i++) {
            System.out.println(i + ":" + recommendResult.get(i).getLeft());
        }
        System.out.println("------------------------------------");
        System.out.println("请选择以上几个症状您是否患有？     回复数字编号，多个请用空格分割。最后按一个enter键确认！");
        //获取用户输入
        Scanner sc = new Scanner(System.in);
        String userInput = sc.next();
        List<String> userInputNumList = Arrays.asList(userInput.trim().split(" "));
        for (int i = 0; i < userInputNumList.size(); i++) {

            int index = Integer.parseInt(userInputNumList.get(i));
            newInput.add(recommendResult.get(index).getLeft());
        }
        //第1轮结果
        result = diagnosis(newInput, input, maxRecommendSym);


        //第2轮的选择
        System.out.println("------------------------------------");
        recommendResult = result.getRecommendResult();
        for (int i = 0; i < recommendResult.size(); i++) {
            System.out.println(i + ":" + recommendResult.get(i).getLeft());
        }
        System.out.println("------------------------------------");
        System.out.println("请选择以上几个症状您是否患有？     回复数字编号，多个请用空格分割。最后按一个enter键确认！");
        //获取用户输入
        sc = new Scanner(System.in);
        userInput = sc.next();
        userInputNumList = Arrays.asList(userInput.trim().split(" "));
        for (int i = 0; i < userInputNumList.size(); i++) {

            int index = Integer.parseInt(userInputNumList.get(i));
            newInput.add(recommendResult.get(index).getLeft());
        }
        //第2轮结果
        result = diagnosis(newInput, input, 6);


        //第3轮的选择
        System.out.println("------------------------------------");
        recommendResult = result.getRecommendResult();
        for (int i = 0; i < recommendResult.size(); i++) {
            System.out.println(i + ":" + recommendResult.get(i).getLeft());
        }
        System.out.println("------------------------------------");
        System.out.println("请选择以上几个症状您是否患有？     回复数字编号，多个请用空格分割。最后按一个enter键确认！");
        //获取用户输入
        sc = new Scanner(System.in);
        userInput = sc.next();
        userInputNumList = Arrays.asList(userInput.trim().split(" "));
        for (int i = 0; i < userInputNumList.size(); i++) {

            int index = Integer.parseInt(userInputNumList.get(i));
            newInput.add(recommendResult.get(index).getLeft());
        }
        //第3轮结果
        result = diagnosis(newInput, input, maxRecommendSym);


        //打印最后的结果
        print(result);

    }
}
