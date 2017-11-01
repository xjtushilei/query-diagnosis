package com.xjtushilei.querydiagnosis.core.lucene.index;

import com.xjtushilei.querydiagnosis.entity.sym.Sym;
import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field.Store;
import org.apache.lucene.document.FloatField;
import org.apache.lucene.document.StringField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.IndexWriterConfig.OpenMode;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.*;

import static com.xjtushilei.querydiagnosis.core.sym.DealData.getDealSymDataL1;

/**
 * 创建索引
 *
 * @author shilei
 * @Date 2017/10/30.
 */
public class IndexCreate {

    public static void main(String[] args) {
        index();
    }

    public static void index() {
        // 指定分词技术，这里使用的是标准分词
        Analyzer analyzer = new StandardAnalyzer();

        // indexWriter的配置信息
        IndexWriterConfig indexWriterConfig = new IndexWriterConfig(analyzer);

        // 索引的打开方式：没有则创建，有则打开
        indexWriterConfig.setOpenMode(OpenMode.CREATE_OR_APPEND);

        Directory directory = null;
        IndexWriter indexWriter = null;
        try {
            // 索引在硬盘上的存储路径
            directory = FSDirectory.open(Paths.get(System.getProperties().getProperty("user.home")
                    + "/luceneIndex/icd10_sym"));
            //indexWriter用来创建索引文件
            indexWriter = new IndexWriter(directory, indexWriterConfig);
            indexWriter.deleteAll();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            //添加需要索引的文档
            HashMap<String, HashMap<String, ArrayList<Sym>>> L1Syms = getDealSymDataL1();
            for (Map.Entry<String, HashMap<String, ArrayList<Sym>>> l1Syms : L1Syms.entrySet()) {
                for (Map.Entry<String, ArrayList<Sym>> l2Map : l1Syms.getValue().entrySet()) {
                    for (Sym s : l2Map.getValue()) {
                        // 创建文档
                        Document doc = new Document();
                        doc.add(new StringField("l1code", l1Syms.getKey(), Store.YES));
                        doc.add(new StringField("l2code", l2Map.getKey(), Store.YES));
                        doc.add(new TextField("name", s.getName(), Store.YES));
                        doc.add(new FloatField("rate", s.getRate(), Store.YES));
                        indexWriter.addDocument(doc);
                    }
                }
            }
            // 将indexWrite操作提交，如果不提交，之前的操作将不会保存到硬盘
            // 但是这一步很消耗系统资源，索引执行该操作需要有一定的策略
            indexWriter.commit();
            System.out.println("目前索引一共：" + indexWriter.numDocs());
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭资源
            try {
                indexWriter.close();
                directory.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}