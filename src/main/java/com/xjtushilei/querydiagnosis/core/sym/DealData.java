package com.xjtushilei.querydiagnosis.core.sym;


import com.xjtushilei.querydiagnosis.entity.icd10.L1;
import com.xjtushilei.querydiagnosis.entity.sym.Sym;
import com.xjtushilei.querydiagnosis.utils.FileUtils;
import org.apache.lucene.document.*;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;



/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class DealData {
    public static void main(String[] args) {
        getSyms();
        //        final int[] i = {0};
        //        getDealSymDataL1().forEach((l1,m2)->{
        //            i[0] = i[0] +m2.size();
        //        });
        //        System.out.println(i[0]);
        //        getDealSymDataL2();
    }

    /**
     * @return icd10 level1对应的症状
     */

    public static HashMap<String, HashMap<String, ArrayList<Sym>>> getDealSymDataL1() {
        HashMap<String, HashMap<String, ArrayList<Sym>>> result = new HashMap<>();
        HashMap<String, L1> m1 = FileUtils.getIcd10DataLevel1();
        HashMap<String, ArrayList<Sym>> mSymL2 = getDealSymDataL2();
        mSymL2.forEach((s2, symList) -> {
            m1.forEach((m1s, m1l1) -> {
                if (m1l1.getL2map().keySet().contains(s2)) {
                    if (result.containsKey(m1s)) {
                        result.get(m1s).put(s2, symList);
                    } else {
                        result.put(m1s, new HashMap<>());
                        result.get(m1s).put(s2, symList);
                    }
                }
            });
        });

        return result;
    }

    /**
     * @return icd10 level2对应的症状
     */

    public static HashMap<String, ArrayList<Sym>> getDealSymDataL2() {
        HashMap<String, ArrayList<Sym>> map = new HashMap<>();
        HashMap<String, L1> icd10 = FileUtils.getIcd10DataLevel1();
        icd10.forEach((k1, v1) -> {
            v1.getL2map().forEach((k2, v2) -> {
                map.put(v2.getCode(), new ArrayList<>());
            });
        });
        List<String> list = null;
        try {
            File file = ResourceUtils.getFile("classpath:data/disease-symptom.txt");
            list = org.apache.commons.io.FileUtils.readLines(file, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            String[] tabs = list.get(i).split("\t");
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
                String l2code = getL2Code(code[j].trim().substring(0, 3), map);
                // 有些疾病未出现在l2编码范围中
                if (!map.containsKey(l2code)) {
                    continue;
                }
                ArrayList tempList = map.get(l2code);
                String[] comma = tabs[2].trim().split(",");
                for (int k = 0; k < comma.length; k++) {
                    String[] colon = comma[k].trim().split(":");
                    String symName = colon[0];
                    float symRate = Float.valueOf(colon[1]);
                    tempList.add(new Sym(symName, symRate));
                }
            }
        }
        HashMap<String, ArrayList<Sym>> noRepeatMap = new HashMap<>();
        //开始计算每一个疾病的每个特征中权重，重复权重策略：+5
        for (Map.Entry<String, ArrayList<Sym>> entry : map.entrySet()) {
            HashMap<String, Float> temp = new HashMap<>();
            ArrayList<Sym> templist = entry.getValue();
            for (int i = 0; i < templist.size(); i++) {
                if (temp.containsKey(templist.get(i))) {
                    templist.get(i).setRate(templist.get(i).getRate() + 5);
                } else {
                    temp.put(templist.get(i).getName(), templist.get(i).getRate());
                }
            }
            ArrayList<Sym> noRepeatList = new ArrayList<>();
            temp.forEach((s, f) -> {
                noRepeatList.add(new Sym(s, f));
            });
            noRepeatMap.put(entry.getKey(), noRepeatList);
        }
        return noRepeatMap;
    }

    /**
     * 打印所有症状，到文件，方便查看
     */
    private static void getSyms() {
        StringBuffer stringBuffer = new StringBuffer();

        HashMap<String, HashMap<String, ArrayList<Sym>>> L1Syms = getDealSymDataL1();
        for (Map.Entry<String, HashMap<String, ArrayList<Sym>>> l1Syms : L1Syms.entrySet()) {
            for (Map.Entry<String, ArrayList<Sym>> l2Map : l1Syms.getValue().entrySet()) {
                for (Sym s : l2Map.getValue()) {
                    // 创建文档
                    Document doc = new Document();
                    doc.add(new StringField("l1code", l1Syms.getKey(), Field.Store.YES));
                    doc.add(new StringField("l2code", l2Map.getKey(), Field.Store.YES));
                    doc.add(new TextField("name", s.getName(), Field.Store.YES));
                    doc.add(new FloatField("rate", s.getRate(), Field.Store.YES));
                    stringBuffer.append(l1Syms.getKey() + "," + l2Map.getKey() + "," + s.getName() + "," + s.getRate() + "\n");

                }
            }
        }

        try {
            File file = new File(System.getProperties().getProperty("user.home") + "/sym_2_index.txt");
            org.apache.commons.io.FileUtils.writeStringToFile(file, stringBuffer.toString(), "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    private static String getL2Code(String l3Code, Map<String, ArrayList<Sym>> map) {

        for (Map.Entry<String, ArrayList<Sym>> entry : map.entrySet()) {
            if (between(l3Code, entry.getKey())) {
                return entry.getKey();
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
