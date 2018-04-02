package com.jinju.android.util;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Libra on 2017/11/3.
 */

public class TextShowUtils {
    //带Html的颜色文本
    public static String colorHtmlText(String text) {
        int a = text.indexOf("[");//  [第一个出现的索引位置
        int b = text.indexOf("]");//  ]第一个出现的索引位置
        ArrayList<String> richInfoList = new ArrayList<String>();
        while (a != -1&& b != -1) {
            richInfoList.add(text.substring(a+1,b));

            a = text.indexOf("[", a + 1);//  [从这个索引往后开始第一个出现的位置
            b = text.indexOf("]", b + 1);//  ]从这个索引往后开始第一个出现的位置
        }

        text = text.replaceAll("\\[","");
        text = text.replaceAll("]","");

        for (int i=0;i < richInfoList.size();i++) {

            text = text.replace(richInfoList.get(i), "<font color='red'>" + richInfoList.get(i) + "</font>");
        }
        return text;
    }

    /**
     * 字符串中只有一个数字
     * @param text
     * @return
     */
    public static String oneColorHtmlText(String text) {

        String regEx="[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(text);
        String number =  m.replaceAll("").trim();//提取数字
        if (!TextUtils.isEmpty(number)) {
            String[] strArray=text.split(number);//分割
            return strArray[0]+"<font color='red'>" +number+ "</font>"+strArray[1];
        } else {
            return text;
        }

    }
}
