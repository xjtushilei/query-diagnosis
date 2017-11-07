package com.xjtushilei.querydiagnosis.main;

import com.xjtushilei.querydiagnosis.entity.sym.Input;
import com.xjtushilei.querydiagnosis.entity.sym.Result;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;
import static com.xjtushilei.querydiagnosis.core.sym.SymMethod.diagnosis;
import static com.xjtushilei.querydiagnosis.utils.GsonUtils.print;

//import static com.xjtushilei.querydiagnosis.core.sym.SymMethod.diagnosis;

/**
 * @author shilei
 * @Date 2017/11/1.
 */
public class testV5 {
    public static void main(String[] args) {
        //得到jingwei的诊断分类结果，包含概率
        Input input = diagnosisFirst();


        /**
         *  数字是返回前几个推荐症状
         *  第二轮 只需要在Arrays.asList里增加新的症状就行了
         */
        Result result = diagnosis(input.getInput(), input, 6);
        print(result);

    }
}
