package com.swt;

import cn.hutool.core.exceptions.ExceptionUtil;
import com.hlin.sensitive.KWSeeker;
import com.hlin.sensitive.KWSeekerManage;
import com.hlin.sensitive.KeyWord;
import com.hlin.sensitive.SensitiveWordResult;
import com.input.doc.Csv;
import com.input.doc.OfficeWord;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class Main {
    public static void main(String[] args) throws UnsupportedLookAndFeelException, ClassNotFoundException, InstantiationException, IllegalAccessException, IOException {
        String lookAndFeel ="com.sun.java.swing.plaf.windows.WindowsLookAndFeel";
        UIManager.setLookAndFeel(lookAndFeel);
        // 1. 创建一个顶层容器（窗口）
        final JFrame jf = new JFrame("敏感词替换");          // 创建窗口
        jf.setSize(300, 200);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）

        // 2. 创建中间容器（面板容器）
        JPanel panel = new JPanel();                // 创建面板容器，使用默认的布局管理器

        final JTextArea textArea = new JTextArea(5, 25);
        textArea.setLineWrap(true);
        //查看当前的敏感词库，可能有乱码的情况
        File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
        String nowPath = directory.getCanonicalPath();
        Csv csv = new Csv();
        Map<String,String> map = csv.read(nowPath + "\\thesaurus.csv");
        for(String key : map.keySet()){
            textArea.append(key + ":" + map.get(key) + "\r\n");
        }
        JScrollPane scrollPane = new JScrollPane(textArea);
        panel.add(scrollPane);
        // 3. 创建一个基本组件（按钮），并添加到 面板容器 中
        JButton openBtn = new JButton("选择word");
        panel.add(openBtn);
        openBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                showFileOpenDialog(jf, textArea);
            }
        });
        // 3. 创建一个基本组件（按钮），并添加到 面板容器 中
        JButton startBtn = new JButton("开始替换");
        panel.add(startBtn);
        startBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startReplace(textArea);
            }
        });
        // 4. 把 面板容器 作为窗口的内容面板 设置到 窗口
        jf.setContentPane(panel);

        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
    }

    /*
     * 打开文件
     */
    private static void showFileOpenDialog(Component parent, JTextArea msgTextArea) {
        // 创建一个默认的文件选取器
        JFileChooser fileChooser = new JFileChooser();

        // 设置默认显示的文件夹为当前文件夹
        fileChooser.setCurrentDirectory(new File("."));

        // 设置文件选择的模式（只选文件、只选文件夹、文件和文件均可选）
        fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        // 设置是否允许多选
        fileChooser.setMultiSelectionEnabled(false);

        // 添加可用的文件过滤器（FileNameExtensionFilter 的第一个参数是描述, 后面是需要过滤的文件扩展名 可变参数）
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("zip(*.zip, *.rar)", "zip", "rar"));
        // 设置默认使用的文件过滤器
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("image(*.jpg, *.png, *.gif)", "jpg", "png", "gif"));

        // 打开文件选择框（线程将被阻塞, 直到选择框被关闭）
        int result = fileChooser.showOpenDialog(parent);

        if (result == JFileChooser.APPROVE_OPTION) {
            // 如果点击了"确定", 则获取选择的文件路径
            File file = fileChooser.getSelectedFile();

            // 如果允许选择多个文件, 则通过下面方法获取选择的所有文件
            // File[] files = fileChooser.getSelectedFiles();
            msgTextArea.setText("");
            msgTextArea.append(file.getPath());
        }
    }

    /*
    * 开始替换
    * */
    private static void startReplace( JTextArea msgTextArea) {
        String path = msgTextArea.getText().trim();
        if(path.equals("")){
            JOptionPane.showMessageDialog(null, "你必须先选择word文件", "错误", JOptionPane. ERROR_MESSAGE);
        }
        replace(path);
    }

    private static void replace(String path){
        try {
            File directory = new File(""); //实例化一个File对象。参数不同时，获取的最终结果也不同
            String nowPath = directory.getCanonicalPath();
            Csv csv = new Csv();
            Map<String,String> map = csv.read(nowPath + "\\thesaurus.csv");
            OfficeWord officeWord = new OfficeWord();
            java.util.List<String> list = officeWord.read(path);
            Set<KeyWord> kws1 = new HashSet<>();
            java.util.List<String> ans = new ArrayList<>(list.size());
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
            int num = 0;
            for(String i : list){
                List<SensitiveWordResult> res = kwSeekerManage.getKWSeeker(wordType1).findWords(i);
                num += res.size();
                for(SensitiveWordResult sensitiveWord : res){
                    i = i.replace(sensitiveWord.getWord(), map.get(sensitiveWord.getWord()));
                }
                ans.add(i);
            }
            int index = path.indexOf(".");
            String newPath = path.substring(0, index) + "（测试文件）"+path.substring(index);
            OfficeWord.write(ans, newPath);
            JOptionPane.showConfirmDialog(null,
                    "替换完成，替换敏感词数："+num, "替换完成", JOptionPane.YES_NO_OPTION);
        }catch (Exception e){
            e.printStackTrace();
            JOptionPane.showConfirmDialog(null,
                    e.getMessage(), "替换发生错误", JOptionPane.YES_NO_OPTION);
        }
    }
}
