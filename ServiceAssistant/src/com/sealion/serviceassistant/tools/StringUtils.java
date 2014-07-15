package com.sealion.serviceassistant.tools;

import android.content.Context;
import android.text.InputFilter;
import android.text.Spanned;
import android.widget.EditText;
import android.widget.Toast;

/**
 * @author jack.lee titans.lee@gmail.com
 * @version V1.0 Copyright: Copyright (c)2012 Company: �����ж�ͨ�ż������޹�˾
 * @Title: StringUtils.java
 * @Package com.sealion.serviceassistant.tools
 * @Description: �����ַ����Ĺ�����
 * @date 2013-3-27 ����3:23:03
 */
public class StringUtils {

    /**
     * ��ʾ�������ݳ����涨���� *
     * @param context ����������
     * @param editText ��׿�еı༭����
     * @param max_length ��󳤶�
     * @param err_msg ������Ϣ
     */
    public static void lengthFilter(final Context context, EditText editText, final int max_length, final String err_msg) {
        InputFilter[] filters = new InputFilter[1];
        filters[0] = new InputFilter.LengthFilter(max_length) {
            @Override
            public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {//��ȡ�ַ�����(һ��������2���ַ�)

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
     * ��ȡ�ַ����ܳ��ȣ�����Ӣ�ĺ����ĵ��ַ���
     *
     * @param content ������ַ���
     * @return �ַ������ܳ���
     */
    public static int getCharacterNum(String content) {
        if (content.equals("")) {
            return 0;
        } else {
            return content.length() + getChineseNum(content);
        }
    }

    /**
     * �����ַ��������ĳ���
     *
     * @param s ��Ҫ������ַ���
     * @return �ַ��������ĳ���
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
