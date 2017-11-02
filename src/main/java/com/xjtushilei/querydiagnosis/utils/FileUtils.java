package com.xjtushilei.querydiagnosis.utils;

import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.xjtushilei.querydiagnosis.entity.icd10.L1;
import com.xjtushilei.querydiagnosis.entity.icd10.L2;
import com.xjtushilei.querydiagnosis.entity.icd10.L3;
import com.xjtushilei.querydiagnosis.entity.icd10.L4;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class FileUtils {

    public static void main(String[] args) throws IOException {
//        org.apache.commons.io.FileUtils.writeStringToFile(new File("D://icd10-6-L1.json"),GsonUtils.toString
//                (getIcd10DataLevel1FromXls()),"utf-8");
//        org.apache.commons.io.FileUtils.writeStringToFile(new File("D://icd10-6-L2.json"),GsonUtils.toString
//                (getIcd10DataLevel2FromXls()),"utf-8");
        getIcd10DataLevel1FromJson();
        getIcd10DataLevel2FromJson();
    }

    public static HashMap<String, L1> getIcd10DataLevel1FromJson() {
        try {
            File file = ResourceUtils.getFile("classpath:data/icd10-6-L1.json");
            String json= org.apache.commons.io.FileUtils.readFileToString(file,"utf-8");
            HashMap<String, L1> icd10L1 = new GsonBuilder().create().fromJson(json,new TypeToken<HashMap<String, L1>>
                    (){}
                    .getType());
            return  icd10L1;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }
    public static HashMap<String, L2> getIcd10DataLevel2FromJson() {
        try {
            File file = ResourceUtils.getFile("classpath:data/icd10-6-L2.json");
            String json= org.apache.commons.io.FileUtils.readFileToString(file,"utf-8");
            HashMap<String, L2> icd10L2 = new GsonBuilder().create().fromJson(json,new TypeToken<HashMap<String, L2>>(){}
                    .getType());
            return  icd10L2;

        } catch (IOException e) {
            e.printStackTrace();
            return  null;
        }
    }


    public static HashMap<String, L2> getIcd10DataLevel2FromXls() {
        HashMap<String, L2> icd10L2 = new HashMap<>();

        HashMap<String, L1> icd10L1 = getIcd10DataLevel1FromXls();
        icd10L1.forEach((s, l1) -> {
            l1.getL2map().forEach((s2, l2) -> {
                icd10L2.put(s2, l2);
            });
        });
        return icd10L2;
    }

    public static HashMap<String, L1> getIcd10DataLevel1FromXls() {

        HashMap<String, L1> icd10 = new HashMap<>();

        // 读excel
        Workbook wb = null;
        try {
            File file = ResourceUtils.getFile("classpath:data/ICD10-6位编码-树形-完整版.xls");

            wb = Workbook.getWorkbook(file);
        } catch (BiffException | IOException e) {
            e.printStackTrace();
        }
        Sheet sheet0 = wb.getSheet(0);

        for (int i = 1; i < sheet0.getRows(); i++) {
            String l1_code = sheet0.getCell(0, i).getContents().replace("\\t", "").trim();
            String l1_name = sheet0.getCell(1, i).getContents().replace("\\t", "").trim();
            String l2_code = sheet0.getCell(2, i).getContents().replaceAll("[\\+\\*]", "").replace("\\t", "").trim();
            String l2_name = sheet0.getCell(3, i).getContents().replaceAll("[\\+\\*]", "").replace("\\t", "").trim();
            String l3_code = sheet0.getCell(4, i).getContents().replaceAll("[\\+\\*]", "").replace("\\t", "").trim();
            String l3_name = sheet0.getCell(5, i).getContents().replaceAll("[\\+\\*]", "").replace("\\t", "").trim();
            String l4_code = sheet0.getCell(6, i).getContents().replaceAll("[\\+\\*]", "").replace("\\t", "");
            if (l4_code.length() > 3) {
                l4_code = l4_code.substring(0, 7).trim();
            }
            String l4_name = sheet0.getCell(7, i).getContents().replaceAll("[\\+\\*]", "").replace("\\t", "").trim();


            if (!l1_code.equals("") && !icd10.containsKey(l1_code)) {
                icd10.put(l1_code, new L1(l1_code, l1_name, new HashMap<>()));
            }
            if (!l2_code.equals("") && !icd10.get(l1_code).getL2map().containsKey(l2_code)) {
                icd10.get(l1_code).getL2map().put(l2_code, new L2(l2_code, l2_name, new HashMap<>()));

            }

            if (!l3_code.equals("") && !icd10.get(l1_code).getL2map().get(l2_code).getL3map().containsKey(l3_code)) {
                icd10.get(l1_code).getL2map().get(l2_code).getL3map().put(l3_code, new L3(l3_code, l3_name, new
                        HashMap<>()));

            }
            if (!l4_code.equals("") && !icd10.get(l1_code).getL2map().get(l2_code).getL3map().get(l3_code).getL4map()
                    .containsKey
                            (l4_code)) {
                icd10.get(l1_code).getL2map().get(l2_code).getL3map().get(l3_code).getL4map().put(l4_code, new L4
                        (l4_code, l4_name));
            }

        }
        return icd10;
    }


}
