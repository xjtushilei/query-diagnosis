package com.xjtushilei.querydiagnosis.core.lucene.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import static com.xjtushilei.querydiagnosis.core.sym.ClassificationOfDiseases.diagnosisFirst;


/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class Search {
    public static void main(String[] args) {

        List<HashMap<String, Object>> diagnosisResult = diagnosisFirst();

        System.out.println(search("急性肠胃炎发烧38啦……打针左氧氟沙星。要不要退烧药,从37到38度了这会已经39了", diagnosisResult, 2));
    }

    /**
     * @param input              用户输入的语言的list
     * @param diagnosisResult    上一部的大类分类结果
     * @param everyTermResultNum 每一个特征词预期结果数量
     * @return 所有特征集合
     */
    public static LinkedHashMap<String, Float> searchAll(List<String> input, List<HashMap<String, Object>>
            diagnosisResult, int everyTermResultNum) {
        LinkedHashMap<String, Float> result = new LinkedHashMap<>();
        input.forEach(in -> {
            search(in, diagnosisResult, everyTermResultNum).forEach((s, f) -> {
                if (!result.containsKey(s)) {
                    result.put(s, f);
                }
            });
        });
        List<Map.Entry<String, Float>> infos = new ArrayList<>(result.entrySet());
        Collections.sort(infos, (e1, e2) -> {
            return Float.compare(e2.getValue(), e1.getValue());
        });
        //申明新的有序 map,根据放入的数序排序
        LinkedHashMap<String, Float> lhm = new LinkedHashMap<String, Float>();
        //遍历比较过后的map,将结果放到LinkedHashMap

        for (Map.Entry<String, Float> entry : infos) {
            lhm.put(entry.getKey(), entry.getValue());
        }
        return lhm;
    }

    /**
     * @param input           用户输入的语言
     * @param diagnosisResult 上一部的大类分类结果
     * @param resultNum       预期结果数量
     * @return 查询结果
     */
    public static LinkedHashMap<String, Float> search(String input, List<HashMap<String, Object>> diagnosisResult, int
            resultNum) {
        if ("".equals(input)) {
            return new LinkedHashMap<>();
        }
        List<String> l1Code = new ArrayList<>();

        LinkedHashMap<String, Float> result = new LinkedHashMap<>();
        //索引存放的位置
        Directory directory;
        try {
            // 索引硬盘存储路径
            directory = FSDirectory.open(Paths.get(System.getProperties().getProperty("user.home")
                    + "/luceneIndex/icd10_sym"));
            // 读取索引
            DirectoryReader directoryReader = DirectoryReader.open(directory);
            // 创建索引检索对象
            IndexSearcher searcher = new IndexSearcher(directoryReader);
            // 分词技术
            Analyzer analyzer = new StandardAnalyzer();
            // 创建Query
            QueryParser parser = new QueryParser("name", analyzer);
            Query query = parser.parse(input);// 查询content为广州的

            //针对L1的code进行限制，只返回我们范围内的症状
            BooleanQuery bquery = new BooleanQuery();
            diagnosisResult.forEach(m -> {
                l1Code.add((String) m.get("code"));
            });
            l1Code.forEach(s -> bquery.add(new TermQuery(new Term("l1code", s)), BooleanClause.Occur.SHOULD));

            bquery.add(query, BooleanClause.Occur.MUST);

            // 检索索引，获取符合条件的前n条记录
            TopDocs topDocs = searcher.search(bquery, resultNum);
            if (topDocs != null) {
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);

                    if (result.size() > resultNum) {
                        return result;
                    } else if (result.containsKey(doc.get("name"))) {
                        continue;
                    } else {
                        result.put(doc.get("name"), topDocs.scoreDocs[i].score);
                    }

                }
            }
            directory.close();
            directoryReader.close();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
