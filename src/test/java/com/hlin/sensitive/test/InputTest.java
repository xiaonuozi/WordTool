package com.hlin.sensitive.test;

import java.io.File;
import java.util.*;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.hlin.sensitive.KWSeeker;
import com.hlin.sensitive.KWSeekerManage;
import com.hlin.sensitive.KeyWord;
import com.hlin.sensitive.SensitiveWordResult;
import com.input.doc.Csv;
import com.input.doc.OfficeWord;
import org.junit.Test;


public class InputTest {
    public static void main(String[] args){




    }
    @Test
    public void testCsv(){
        try {
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            String nowPath = directory.getCanonicalPath();
            Csv csv = new Csv();
            Map<String, String> map = csv.read(nowPath + "\\thesaurus.csv");
            for(Map.Entry<String, String> entry : map.entrySet()){
                System.out.println("Key = " + entry.getKey() + ", Value = " + entry.getValue());
            }
        }catch (Exception e){

        }
    }
    @Test
    public void testWord(){
        try {
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            String nowPath = directory.getCanonicalPath();
            OfficeWord officeWord = new OfficeWord();
            List<String> list = officeWord.read(nowPath + "\\石老师3200 李彪（朱）9.28.docx");
            for(String i : list){
                System.out.println(i);
            }

        }catch (Exception e){
            System.out.println(ExceptionUtil.stacktraceToOneLineString(e));
        }
    }
    @Test
    public void testReplace(){
        try {
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            String nowPath = directory.getCanonicalPath();
            Csv csv = new Csv();
            Map<String,String> map = csv.read(nowPath + "\\thesaurus.csv");
            OfficeWord officeWord = new OfficeWord();
            List<String> list = officeWord.read(nowPath + "\\1.docx");
            Set<KeyWord> kws1 = new HashSet<>();
            List<String> ans = new ArrayList<>(list.size());
            for (String key : map.keySet()) {
                kws1.add(new KeyWord(key));
            }
            // 根据敏感词,初始化敏感词搜索器
            KWSeeker kwSeeker1 = new KWSeeker(kws1);
            // 搜索器组,构建敏感词管理器,可同时管理多个搜索器，map的key为自定义搜索器标识
            Map<String, KWSeeker> seekers = new HashMap<String, KWSeeker>();
            String wordType1 = "sensitive-word-1";
            seekers.put(wordType1, kwSeeker1);
            KWSeekerManage kwSeekerManage = new KWSeekerManage(seekers);

            for(String i : list){
                List<SensitiveWordResult> res = kwSeekerManage.getKWSeeker(wordType1).findWords(i);
                for(SensitiveWordResult sensitiveWord : res){
                    i = i.replace(sensitiveWord.getWord(), map.get(sensitiveWord.getWord()));
                }
                ans.add(i);
            }
            for(String i : ans){
                System.out.println(i);
            }
        }catch (Exception e){
            System.out.println(ExceptionUtil.stacktraceToOneLineString(e));
        }
    }

    @Test
    public void testWrite(){
        try {
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            String nowPath = directory.getCanonicalPath();
            OfficeWord officeWord = new OfficeWord();
            List<String> list = officeWord.read(nowPath + "\\1.docx");
            OfficeWord.write(list, nowPath + "\\test.docx");
        }catch (Exception e){
            e.printStackTrace();
        }

    }
}
