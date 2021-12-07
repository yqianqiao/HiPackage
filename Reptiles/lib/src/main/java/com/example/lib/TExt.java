package com.example.lib;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Android Studio.
 * Author: yx
 * Date: 2021/6/24 22:41
 * Description: com.example.lib
 */
class TExt {

    public static void main(String[] args) {
//


        //0. 准备好正则
        String regex = "(https?|ftp|file)://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]";
        //1. 读取文档
        String data = readFile("F:\\test\\文档.txt");
        //2. 正则查找
        String needData = filterSpecialStr(regex, data);
        //3. 写到某个文件中
        writeFile("F:\\test\\needData.txt", needData);


        List<String> list = new ArrayList<>();

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("F:\\test\\needData.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {

                list.add(line);

//                sb.append(line).append("\r\n");
            }

        } catch (IOException e) {
            e.printStackTrace();
        }



        List<FormationBean> dataList = new ArrayList<>();

        for (int i = 0; i < list.size(); i++) {
            FormationBean bean = new FormationBean();
            if (i % 3 == 1) {
                if (list.get(i).contains(".jpeg") || list.get(i).contains(".png")) {
                    bean.imgUrl = list.get(i);
                }
            } else if (i % 3 == 2) {
                if (!list.get(i).contains(".jpeg") || !list.get(i).contains(".png")) {
                    bean.linkUrl = list.get(i);
                    dataList.add(bean);
                }
            } else if (i % 3 == 0) {

            }
        }

        System.out.println(dataList);
    }


    static class FormationBean {
        String imgUrl;
        String linkUrl;
    }


    private static String readFile(String pathName) {
        //读取到的文件内容放到这个sb里
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader(pathName))) {
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\r\n");
            }
            System.out.println("读取文件完成");
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    private static void writeFile(String pathName, String data) {
        try {
            //文件不存在的话新建，存在覆盖
            File file = new File(pathName);
            file.createNewFile();
            //The Java 7 try-with-resources syntax (Automatic Resource Management) is nice
            try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
                bw.write(data);
                bw.flush();
                System.out.println("文件写入完成");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String filterSpecialStr(String regex, String data) {
        //sb存放正则匹配的结果
        StringBuffer sb = new StringBuffer();
        //编译正则字符串
        Pattern p = Pattern.compile(regex);
        //利用正则去匹配
        Matcher matcher = p.matcher(data);
        //如果找到了我们正则里要的东西
        while (matcher.find()) {
            //保存到sb中，"\r\n"表示找到一个放一行，就是换行
            sb.append(matcher.group() + "\r\n");
        }
        return sb.toString();
    }
}
