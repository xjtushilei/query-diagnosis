package com.xjtushilei.querydiagnosis.lucene.search;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.ArrayList;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.queryparser.classic.ParseException;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

/**
 * @author shilei
 * @Date 2017/10/30.
 */
public class Search {
    public static void main(String[] args) {
        search("哭笑无常", 15);
    }

    public static ArrayList<String> search(String input, int resultNum) {
        ArrayList<String> result = new ArrayList<>();
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
            // 检索索引，获取符合条件的前10条记录
            TopDocs topDocs = searcher.search(query, resultNum);
            if (topDocs != null) {
                System.out.println("符合条件的记录为： " + topDocs.totalHits);
                for (int i = 0; i < topDocs.scoreDocs.length; i++) {
                    Document doc = searcher.doc(topDocs.scoreDocs[i].doc);
                    result.add(doc.get("name"));
                    System.out.println("name = " + doc.get("name"));
                    System.out.println("rate = " + doc.get("rate"));
                }
            }
            directory.close();
            directoryReader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return result;
    }
}
