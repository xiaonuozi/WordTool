package com.hlin.sensitive.test;

import java.io.File;
import com.input.doc.Csv;


public class InputTest {
    public static void main(String[] args){



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
