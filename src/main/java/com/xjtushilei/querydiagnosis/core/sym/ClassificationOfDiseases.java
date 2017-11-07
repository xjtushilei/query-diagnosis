package com.xjtushilei.querydiagnosis.core.sym;


import com.xjtushilei.querydiagnosis.entity.sym.Input;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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

    public static Input diagnosisFirst() {
        Input input = new Input();
        try {
            File file = ResourceUtils.getFile("classpath:data/test1.txt");
            List<String> list = org.apache.commons.io.FileUtils.readLines(file, "utf-8");
            input.setInput(Arrays.asList(list.get(0).trim().split(",")));
            input.setL3name(Arrays.asList(list.get(1).replaceAll("['\\[\\]]", "").trim().split(" ")));
            List<Double> rateList = new ArrayList<>();
            Arrays.asList(list.get(2).replaceAll("[\\[\\]]", "").trim().split(",")).forEach(s -> rateList.add(Double.parseDouble(s)));
            input.setL3rate(rateList);


        } catch (IOException e) {
            e.printStackTrace();
        }


        return input;
    }

}
