package com.hlin.sensitive.test;

import java.io.File;
import java.util.List;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.input.doc.Csv;
import com.input.doc.OfficeWord;


public class InputTest {
    public static void main(String[] args){
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
    public void testCsv(){
        try {
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            String nowPath = directory.getCanonicalPath();
            Csv csv = new Csv();
            csv.read(nowPath + "\\thesaurus.csv");

        }catch (Exception e){

        }
    }
}
