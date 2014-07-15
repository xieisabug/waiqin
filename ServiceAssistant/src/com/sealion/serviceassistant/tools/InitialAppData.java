package com.sealion.serviceassistant.tools;

import android.util.Xml;
import com.sealion.serviceassistant.entity.*;
import org.xmlpull.v1.XmlPullParser;

import java.io.InputStream;
import java.util.ArrayList;

/**
 * ��ʼ�����������
 */
public class InitialAppData {
    /**
     * �������ó�ʼ������
     * @param inputStream ����������ȡxml
     * @return ���ط�������
     * @throws Exception
     */
    public static ArrayList<CostEntity> getCostData(InputStream inputStream) throws Exception {
        ArrayList<CostEntity> items = null;
        CostEntity ceItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// ������һ���¼�
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
                    items = new ArrayList<CostEntity>();
                    break;
                case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
                    if ("cost".equals(parser.getName())) {
                        ceItem = new CostEntity();
                    }
                    if (ceItem != null) {
                        if ("id".equals(parser.getName())) {   // �жϿ�ʼ��ǩԪ���Ƿ���name
                            ceItem.setCost_id(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            ceItem.setName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
                    if ("cost".equals(parser.getName())) {
                        items.add(ceItem);
                        ceItem = null;
                    }
                    break;
            }
            event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
        }// end while
        return items;
    }

    /**
     * ����������������
     * @param inputStream ����������ȡxml
     * @return ������������
     * @throws Exception
     */
    public static ArrayList<Q_Type> getQTypeData(InputStream inputStream) throws Exception {
        ArrayList<Q_Type> items = null;
        Q_Type qtItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// ������һ���¼�
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
                    items = new ArrayList<Q_Type>();
                    break;
                case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
                    if ("q_type".equals(parser.getName())) {
                        qtItem = new Q_Type();
                    }
                    if (qtItem != null) {
                        if ("id".equals(parser.getName())) {   // �жϿ�ʼ��ǩԪ���Ƿ���name
                            qtItem.setType_id(Integer.parseInt(parser.nextText()));
                        } else if ("q_category_id".equals(parser.getName())) {
                            qtItem.setQ_category_id(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            qtItem.setName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
                    if ("q_type".equals(parser.getName())) {
                        items.add(qtItem);
                        qtItem = null;
                    }
                    break;
            }
            event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
        }// end while
        return items;
    }

    /**
     * ���������������
     * @param inputStream ����������ȡxml
     * @return �����������
     * @throws Exception
     */
    public static ArrayList<Q_Category> getQCategoryData(InputStream inputStream) throws Exception {
        ArrayList<Q_Category> items = null;
        Q_Category qtItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// ������һ���¼�
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
                    items = new ArrayList<Q_Category>();
                    break;
                case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
                    if ("q_category".equals(parser.getName())) {
                        qtItem = new Q_Category();
                    }
                    if (qtItem != null) {
                        if ("id".equals(parser.getName())) {   // �жϿ�ʼ��ǩԪ���Ƿ���name
                            qtItem.setCategory_id(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            qtItem.setName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
                    if ("q_category".equals(parser.getName())) {
                        items.add(qtItem);
                        qtItem = null;
                    }
                    break;
            }
            event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
        }// end while
        return items;
    }

    /**
     * ������Ʒ����
     * @param inputStream ����������ȡxml
     * @return ��Ʒ����
     * @throws Exception
     */
    public static ArrayList<ProductEntity> getProductData(InputStream inputStream) throws Exception {
        ArrayList<ProductEntity> items = null;
        ProductEntity pItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// ������һ���¼�
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
                    items = new ArrayList<ProductEntity>();
                    break;
                case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
                    if ("product".equals(parser.getName())) {
                        pItem = new ProductEntity();
                    }
                    if (pItem != null) {
                        if ("id".equals(parser.getName())) {   // �жϿ�ʼ��ǩԪ���Ƿ���name
                            pItem.setProductId(Integer.parseInt(parser.nextText()));
                        } else if ("name".equals(parser.getName())) {
                            pItem.setProductName(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
                    if ("product".equals(parser.getName())) {
                        if (items != null) {
                            items.add(pItem);
                        }
                        pItem = null;
                    }
                    break;
            }
            event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
        }// end while
        return items;
    }

    /**
     * ����˰������������
     * @param inputStream ����������ȡxml
     * @return ˰������������
     * @throws Exception
     */
    public static ArrayList<TaxBureauEntity> getTaxBureauData(InputStream inputStream) throws Exception {
        ArrayList<TaxBureauEntity> items = null;
        TaxBureauEntity tbItem = null;
        XmlPullParser parser = Xml.newPullParser();
        parser.setInput(inputStream, "UTF-8");
        int event = parser.getEventType();// ������һ���¼�
        while (event != XmlPullParser.END_DOCUMENT) {
            switch (event) {
                case XmlPullParser.START_DOCUMENT:// �жϵ�ǰ�¼��Ƿ����ĵ���ʼ�¼�
                    items = new ArrayList<TaxBureauEntity>();
                    break;
                case XmlPullParser.START_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؿ�ʼ�¼�
                    if ("tax_bureau".equals(parser.getName())) {
                        tbItem = new TaxBureauEntity();
                    }
                    if (tbItem != null) {
                        if ("bureau_id".equals(parser.getName())) {   // �жϿ�ʼ��ǩԪ���Ƿ���name
                            tbItem.setBureau_id(Integer.parseInt(parser.nextText()));
                        } else if ("bureau_name".equals(parser.getName())) {
                            tbItem.setBureau_name(parser.nextText());
                        } else if ("city".equals(parser.getName())) {
                            tbItem.setCity(parser.nextText());
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:// �жϵ�ǰ�¼��Ƿ��Ǳ�ǩԪ�ؽ����¼�
                    if ("tax_bureau".equals(parser.getName())) {
                        items.add(tbItem);
                        tbItem = null;
                    }
                    break;
            }
            event = parser.next();// ������һ��Ԫ�ز�������Ӧ�¼�
        }// end while
        return items;
    }
}
