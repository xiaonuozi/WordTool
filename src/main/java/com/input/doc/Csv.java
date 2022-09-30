package com.input.doc;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class Csv {
    public Map<String, String> read(String csvFile){
        String line = "";
        String cvsSplitBy = ",";
        Map<String, String> ans = new HashMap<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFile), "UTF-8"))) {

            while ((line = br.readLine()) != null) {

                // use comma as separator
                String[] country = line.split(cvsSplitBy);
                ans.put(country[0],country[1]);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        return ans;
    }
}
