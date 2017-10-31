package com.xjtushilei.querydiagnosis.utils;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.springframework.util.ResourceUtils;

import java.io.*;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class CsvUtils {

    public static CSVParser getCsvFromResource(String filename) {
        CSVParser records = null;
        try {
            File file = ResourceUtils.getFile("classpath:" + filename);
            Reader in = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8");
            records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;

    }

    public static CSVParser getCsvFromFile(String filename) {
        CSVParser records = null;
        try {
            File file = new File(filename);
            Reader in = new InputStreamReader(new FileInputStream(file.getAbsolutePath()), "UTF-8");
            records = CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return records;

    }
}
