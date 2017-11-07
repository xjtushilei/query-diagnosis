package com.xjtushilei.querydiagnosis.core.sym;


import com.xjtushilei.querydiagnosis.entity.sym.L3Sym;
import com.xjtushilei.querydiagnosis.entity.sym.Sym;
import com.xjtushilei.querydiagnosis.utils.GsonUtils;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class DealData {
    public static void main(String[] args) throws IOException {

        GsonUtils.print(getSymFromOriginalFile());

    }

    /**
     * @return icd10 level3对应的症状
     */

    public static HashMap<String, L3Sym> getSymFromOriginalFile() {
        HashMap<String, L3Sym> map = new HashMap<>();

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

            String name = tabs[0].trim();
            String l3code = tabs[1].trim();
            L3Sym l3Sym = new L3Sym(name, l3code);

            HashMap<String, Sym> tempMap = new HashMap<>();
            String[] comma = tabs[2].trim().split(" ");
            for (int k = 0; k < comma.length; k++) {
                String[] colon = comma[k].trim().split(":");
                String symName = colon[0];
                double symRate = Double.valueOf(colon[1]);
                tempMap.put(symName, new Sym(symName, symRate));
            }
            l3Sym.setAllSymMap(tempMap);
            map.put(l3Sym.getNameL3(), l3Sym);
        }
        return map;
    }


    private static String getL2Code(String l3Code, Set<String> set) {

        for (String code : set) {
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
