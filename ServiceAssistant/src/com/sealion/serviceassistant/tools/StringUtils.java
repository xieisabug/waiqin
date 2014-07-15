package com.sealion.serviceassistant.tools;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0 Copyright: Copyright (c)2012 Company: 湖南中恩通信技术有限公司
 * @Title: StringUtils.java
 * @Package com.sealion.serviceassistant.tools
 * @Description: 关于字符串的工具类
 * @date 2013-3-27 下午3:23:03
 */
public class StringUtils {

    /**
     * 提示输入内容超过规定长度 *
     * @param context 程序上下文
     * @param editText 安卓中的编辑文字
     * @param max_length 最大长度
     * @param err_msg 错误信息
     */
    public static void lengthFilter(final Context context, EditText editText, final int max_length, final String err_msg) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(max_length) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {//获取字符个数(一个中文算2个字符)

                int destLen = dest.toString().length();
                int sourceLen = source.toString().length();
                if (destLen + sourceLen > max_length) {
                    Toast.makeText(context, err_msg, Toast.LENGTH_SHORT).show();
                    return "";
                }
                return source;
            }
        };
        editText.setFilters(filters);
    }

    /**
     * 获取字符串总长度（包括英文和中文的字符）
     *
     * @param content 计算的字符串
     * @return 字符串的总长度
     */
    public static int getCharacterNum(String content) {
        if (content.equals("")) {
            return 0;
        } else {
            return content.length() + getChineseNum(content);
        }
    }

    /**
     * 计算字符串的中文长度
     *
     * @param s 需要计算的字符串
     * @return 字符串的中文长度
     */
    public static int getChineseNum(String s) {
        int num = 0;
        char[] myChar = s.toCharArray();
        for (int i = 0; i < myChar.length; i++) {
            if ((char) (byte) myChar[i] != myChar[i]) {
                num++;
            }
        }
        return num;
    }

}
