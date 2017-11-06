package com.xjtushilei.querydiagnosis.core.sym;


import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xjtushilei.querydiagnosis.entity.icd10.L1;
import com.xjtushilei.querydiagnosis.entity.sym.L2Sym;
import com.xjtushilei.querydiagnosis.entity.sym.Sym;
import com.xjtushilei.querydiagnosis.utils.GsonUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import static com.xjtushilei.querydiagnosis.utils.FileUtils.getIcd10DataLevel1FromJson;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class DealData {
    public static void main(String[] args) throws IOException {

        org.apache.commons.io.FileUtils.writeStringToFile(new File("D://icd10-l2-all-syms.json"), GsonUtils.toString
                (getSymFromOriginalFile()), "utf-8");

        //        getIcd10DataLevel1FromJson();
    }

    /**
     * @return icd10 level2对应的症状
     */

    public static HashMap<String, L2Sym> getSymFromJson() {
        try {
            File file = ResourceUtils.getFile("classpath:data/icd10-l2-all-syms.json");
            String json= org.apache.commons.io.FileUtils.readFileToString(file,"utf-8");
            HashMap<String, L2Sym> map = new GsonBuilder().create().fromJson(json,new TypeToken<HashMap<String, L2Sym>>
                    (){}
                    .getType());
            return  map;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
    /**
     * @return icd10 level2对应的症状
     */

    public static HashMap<String, L2Sym> getSymFromOriginalFile() {
        //初始化填充L2Sym的Map，即返回结果
        HashMap<String, L2Sym> map = new HashMap<>();
        HashMap<String, L1> icd10 = getIcd10DataLevel1FromJson();
        icd10.forEach((k1, v1) -> {

            v1.getL2map().forEach((k2, v2) -> {
                map.put(v2.getCode(), new L2Sym(v1.getCode(),v1.getName(),v2.getCode(),v2.getName()));
            });

        });
        //获取症状数据，苏丽娟给的
        List<String> allOriginalSymList = null;
        try {
            File file = ResourceUtils.getFile("classpath:data/disease-symptom3.txt");
            allOriginalSymList = org.apache.commons.io.FileUtils.readLines(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        //将症状数据填充到map中
        for (int i = 0; i < allOriginalSymList.size(); i++) {
            String[] tabs = allOriginalSymList.get(i).split("\t");
            //没有症状的直接忽略
            if (tabs.length != 3) {
                continue;
            }
            String name = tabs[0].trim();
            String[] code = tabs[1].trim().split(",");
            //code中出现/直接忽略
            if (tabs[1].contains("/")) {
                continue;
            }
            for (int j = 0; j < code.length; j++) {

                //汉字或者其他不符合的直接过滤
                if (code[j].trim().length() < 3 || !"QAZWSXEDCRFVTGBYHNUJMIKOLP".contains(code[j].trim().substring(0, 1))) {
                    continue;
                }
                String l2code = getL2Code(code[j].trim().substring(0, 3), map.keySet());
                // 有些疾病未出现在l2编码范围中
                if (!map.containsKey(l2code)) {
                    continue;
                }
                HashMap<String, Sym> tempMap = map.get(l2code).getAllSymMap();
                String[] comma = tabs[2].trim().split(" ");
                for (int k = 0; k < comma.length; k++) {
                    String[] colon = comma[k].trim().split(":");
                    String symName = colon[0];
                    double symRate = Double.valueOf(colon[1]);
                    if (tempMap.containsKey(symName)){
                        // TODO
                        //症状会重复，但是其rate还不一样。
                    }
                    else {
                        tempMap.put(symName,new Sym(symName,symRate));
                    }
                }
            }
        }
        return map;
    }


    private static String getL2Code(String l3Code, Set<String> set) {

        for (String code:set) {
            if (between(l3Code, code)) {
                return code;
            }
        }
        return null;
    }

    private static boolean between(String l3code, String l2code) {

        int bianma_int = l3code.charAt(0) * 100 + Integer.valueOf(l3code.substring(1));
        int up = l2code.charAt(4) * 100 + Integer.valueOf(l2code.substring(5, 7));
        int low = l2code.charAt(0) * 100 + Integer.valueOf(l2code.substring(1, 3));
        if (bianma_int >= low && bianma_int <= up) {
            return true;
        }
        return false;
    }
}
