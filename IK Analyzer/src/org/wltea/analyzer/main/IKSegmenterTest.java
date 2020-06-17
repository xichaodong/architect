package org.wltea.analyzer.main;

import org.wltea.analyzer.core.IKSegmenter;
import org.wltea.analyzer.core.Lexeme;
import java.io.IOException;
import java.io.StringReader;
public class IKSegmenterTest {
    static String text = "IK Analyzer是一个结合词典分词和文法分词的中文分词开源工具包。它使用了全新的正向迭代最细粒度切分算法。";
    public static void main(String[] args) throws IOException {
        IKSegmenter segmenter = new IKSegmenter(new StringReader(text), false);
        Lexeme next;
        System.out.print("非智能分词结果：");
        while((next=segmenter.next())!=null){
            System.out.print(next.getLexemeText()+" ");
        }
        System.out.println();
        System.out.println("----------------------------分割线------------------------------");
        IKSegmenter smartSegmenter = new IKSegmenter(new StringReader(text), true);
        System.out.print("智能分词结果：");
        while((next=smartSegmenter.next())!=null) {
            System.out.print(next.getLexemeText() + " ");
        }
    }
}