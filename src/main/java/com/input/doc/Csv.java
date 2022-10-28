package com.input.doc;

import java.io.*;
import java.util.HashMap;
import java.util.Map;
/*
* @author huangzilong
* date 22/9/30
**/
public class Csv {
    public Map<String, String> read(String csvFile) throws IOException {
        String line = "";
        String cvsSplitBy = ",";
        Map<String, String> ans = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"))) {
            int i = 0;
            while ((line = br.readLine()) != null) {
                if(i == 0){
                    i++;
                    continue;
                }
                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                ans.put(country[0],country[1]);

            }

        } catch (IOException e) {
            e.printStackTrace();
            throw e;
        }
        return ans;
    }
}
