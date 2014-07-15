package com.sealion.serviceassistant.tools;

import android.util.Xml;
import com.sealion.serviceassistant.entity.*;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * 初始化程序的数据
 */
public class InitialAppData {
    /**
     * 解析费用初始化数据
     * @param inputStream 输入流，读取xml
     * @return 返回费用数据
     * @throws Exception
     */
    public static ArrayList<CostEntity> getCostData(InputStream inputStream) throws Exception {
        ArrayList<CostEntity> items = null;
        CostEntity ceItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// 产生第一个事件
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    items = new ArrayList<CostEntity>();
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("cost".equals(parser.getName())) {
                        ceItem = new CostEntity();
                    }
                    if (ceItem != null) {
                        if ("id".equals(parser.getName())) {   // 判断开始标签元素是否是name
                            ceItem.setCost_id(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            ceItem.setName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    if ("cost".equals(parser.getName())) {
                        items.add(ceItem);
                        ceItem = null;
                    }
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }// end while
        return items;
    }

    /**
     * 解析问题类型数据
     * @param inputStream 输入流，读取xml
     * @return 问题类型数据
     * @throws Exception
     */
    public static ArrayList<Q_Type> getQTypeData(InputStream inputStream) throws Exception {
        ArrayList<Q_Type> items = null;
        Q_Type qtItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// 产生第一个事件
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    items = new ArrayList<Q_Type>();
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("q_type".equals(parser.getName())) {
                        qtItem = new Q_Type();
                    }
                    if (qtItem != null) {
                        if ("id".equals(parser.getName())) {   // 判断开始标签元素是否是name
                            qtItem.setType_id(Integer.parseInt(parser.nextText()));
                        } else if ("q_category_id".equals(parser.getName())) {
                            qtItem.setQ_category_id(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            qtItem.setName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    if ("q_type".equals(parser.getName())) {
                        items.add(qtItem);
                        qtItem = null;
                    }
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }// end while
        return items;
    }

    /**
     * 解析问题类别数据
     * @param inputStream 输入流，读取xml
     * @return 问题类别数据
     * @throws Exception
     */
    public static ArrayList<Q_Category> getQCategoryData(InputStream inputStream) throws Exception {
        ArrayList<Q_Category> items = null;
        Q_Category qtItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// 产生第一个事件
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    items = new ArrayList<Q_Category>();
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("q_category".equals(parser.getName())) {
                        qtItem = new Q_Category();
                    }
                    if (qtItem != null) {
                        if ("id".equals(parser.getName())) {   // 判断开始标签元素是否是name
                            qtItem.setCategory_id(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            qtItem.setName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    if ("q_category".equals(parser.getName())) {
                        items.add(qtItem);
                        qtItem = null;
                    }
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }// end while
        return items;
    }

    /**
     * 解析产品数据
     * @param inputStream 输入流，读取xml
     * @return 产品数据
     * @throws Exception
     */
    public static ArrayList<ProductEntity> getProductData(InputStream inputStream) throws Exception {
        ArrayList<ProductEntity> items = null;
        ProductEntity pItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// 产生第一个事件
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    items = new ArrayList<ProductEntity>();
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("product".equals(parser.getName())) {
                        pItem = new ProductEntity();
                    }
                    if (pItem != null) {
                        if ("id".equals(parser.getName())) {   // 判断开始标签元素是否是name
                            pItem.setProductId(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            pItem.setProductName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    if ("product".equals(parser.getName())) {
                        if (items != null) {
                            items.add(pItem);
                        }
                        pItem = null;
                    }
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }// end while
        return items;
    }

    /**
     * 解析税务机关类别数据
     * @param inputStream 输入流，读取xml
     * @return 税务机关类别数据
     * @throws Exception
     */
    public static ArrayList<TaxBureauEntity> getTaxBureauData(InputStream inputStream) throws Exception {
        ArrayList<TaxBureauEntity> items = null;
        TaxBureauEntity tbItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// 产生第一个事件
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// 判断当前事件是否是文档开始事件
                    items = new ArrayList<TaxBureauEntity>();
                    break;
                case XmlPullParser.START_TAG:// 判断当前事件是否是标签元素开始事件
                    if ("tax_bureau".equals(parser.getName())) {
                        tbItem = new TaxBureauEntity();
                    }
                    if (tbItem != null) {
                        if ("bureau_id".equals(parser.getName())) {   // 判断开始标签元素是否是name
                            tbItem.setBureau_id(Integer.parseInt(parser.nextText()));
                        } else if ("bureau_name".equals(parser.getName())) {
                            tbItem.setBureau_name(parser.nextText());
                        } else if ("city".equals(parser.getName())) {
                            tbItem.setCity(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// 判断当前事件是否是标签元素结束事件
                    if ("tax_bureau".equals(parser.getName())) {
                        items.add(tbItem);
                        tbItem = null;
                    }
                    break;
            }
            event = parser.next();// 进入下一个元素并触发相应事件
        }// end while
        return items;
    }
}
