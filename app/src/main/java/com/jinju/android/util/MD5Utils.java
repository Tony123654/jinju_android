package com.jinju.android.util;

import com.jinju.android.constant.AppConstant;

import java.security.MessageDigest;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by Libra on 2017/10/16.
 */

public class MD5Utils {
    /**
     * 数字MD5加密
     * @param pwd
     * @return
     */

    public static String numberMD5(String pwd) {
        //用于加密的字符
        char md5String[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9','a', 'b', 'c', 'd', 'e', 'f'};

        try {
            //使用平台的默认字符集将此 String 编码为 byte序列，并将结果存储到一个新的 byte数组中
            byte[] btInput = pwd.getBytes();

            //信息摘要是安全的单向哈希函数，它接收任意大小的数据，并输出固定长度的哈希值。
            MessageDigest mdInst = MessageDigest.getInstance("MD5");

            //MessageDigest对象通过使用 update方法处理数据， 使用指定的byte数组更新摘要
            mdInst.update(btInput);

            // 摘要更新之后，通过调用digest（）执行哈希计算，获得密文
            byte[] md = mdInst.digest();

            // 把密文转换成十六进制的字符串形式
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {   //  i = 0
                byte byte0 = md[i];  //95
                str[k++] = md5String[byte0 >>> 4 & 0xf];    //    5
                str[k++] = md5String[byte0 & 0xf];   //   F
            }

            //返回经过加密后的字符串
            return new String(str);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     *   约定 加密签名
     * @param datas
     * @return
     */
    public final static String getSignature(Map<String, Object> datas){

        Map<String, Object> resultMap = sortMapByKey(datas);
        StringBuffer stringBuffer = new StringBuffer();

        for (Map.Entry<String, Object> entry : resultMap.entrySet()) {
            stringBuffer.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
        }
        //去掉末尾&
        String str = stringBuffer.toString();
        //参数拼接
        String signStr1 = str.substring(0,str.length()-1);
        String signStr2;
        if (AppConstant.APP_WEB_URL.startsWith("https://www")) {//正式环境
            //秘钥
            signStr2 = signStr1 + AppConstant.TRUE_SECRET;//正式秘钥
        } else {
            signStr2  = signStr1 + AppConstant.TEST_SECRET;//测试秘钥
        }

        //MD5加密
        String signature = numberMD5(signStr2);
        return signature;
    }
    /**
     * 使用 Map按key进行排序
     * @param map
     * @return
     */
    public static Map<String, Object> sortMapByKey(Map<String, Object> map) {
        if (map == null || map.isEmpty()) {
            return null;
        }
        Map<String, Object> sortMap = new TreeMap<String, Object>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        // 升序排序
                        return obj1.compareTo(obj2);
                    }
                });
        sortMap.putAll(map);
        return sortMap;
    }

}
