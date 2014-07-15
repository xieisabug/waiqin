package com.sealion.serviceassistant.widget;

import java.util.ArrayList;
import java.util.Collections;

import com.sealion.serviceassistant.R;

import android.content.Context;
import android.graphics.drawable.BitmapDrawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

/**
 * ����ʽ�˵�
 * ��ʼ�������˵���popMenu = new PopMenu(this);
 * �����ĿΪ��popMenu.addItems(typeArray);
 * ��ҪΪMenu����¼���Ҫ����OnItemClickListener��Ϊÿ��λ�õ�itemʵ���¼���
 * Ϊ�����˵��İ�ťע��OnClickListener�¼���popMenu.showAsDropDown(v)
 */
public class PopMenu {
    private ArrayList<String> itemList;
    private Context context;
    private PopupWindow popupWindow;
    private ListView listView;

    // private OnItemClickListener listener;

    public PopMenu(Context context) {
        this.context = context;

        itemList = new ArrayList<String>(5);

        View view = LayoutInflater.from(context).inflate(R.layout.popmenu, null);

        // ���� listview
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PopAdapter());

        //popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT - 15, LayoutParams.WRAP_CONTENT);
        popupWindow = new PopupWindow(view, context.getResources().getDimensionPixelSize(
                R.dimen.popmenu_width), LayoutParams.WRAP_CONTENT);

        // �����Ϊ�˵��������Back��Ҳ��ʹ����ʧ�����Ҳ�����Ӱ����ı�����������ģ�
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    // ���ò˵�����������
    public void setOnItemClickListener(OnItemClickListener listener) {
        // this.listener = listener;
        listView.setOnItemClickListener(listener);
    }

    // ������Ӳ˵���
    public void addItems(String[] items) {
        Collections.addAll(itemList, items);
    }

    // ������Ӳ˵���
    public void addItem(String item) {
        itemList.add(item);
    }

    // ����ʽ ���� pop�˵� parent ���½�
    public void showAsDropDown(View parent) {
        popupWindow.showAsDropDown(parent, 10,
                // ��֤�ߴ��Ǹ�����Ļ�����ܶ�����
                context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));

        // ʹ��ۼ�
        popupWindow.setFocusable(true);
        // ����������������ʧ
        popupWindow.setOutsideTouchable(true);
        // ˢ��״̬
        popupWindow.update();
    }

    public void showAtLocation(View parent) {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        // ʹ��ۼ�
        popupWindow.setFocusable(true);
        // ����������������ʧ
        popupWindow.setOutsideTouchable(true);
        // ˢ��״̬
        popupWindow.update();
    }

    /**
     *  ���ز˵�
     */
    public void dismiss() {
        popupWindow.dismiss();
    }

    public void ClearAllSelectItem() {
        for (int i = 0; i < listView.getChildCount(); i++) {
            View view = listView.getChildAt(i);
            ImageView iView = (ImageView) view.findViewById(R.id.select_item);
            iView.setBackgroundResource(0);
        }
    }

    // ������
    private final class PopAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return itemList.size();
        }

        @Override
        public Object getItem(int position) {
            return itemList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder;
            if (convertView == null) {
                convertView = LayoutInflater.from(context).inflate(R.layout.pomenu_item, null);
                holder = new ViewHolder();
                convertView.setTag(holder);
                holder.groupItem = (TextView) convertView.findViewById(R.id.textView);
                holder.iView = (ImageView) convertView.findViewById(R.id.select_item);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.groupItem.setText(itemList.get(position));

            return convertView;
        }

        private final class ViewHolder {
            TextView groupItem;
            ImageView iView;
        }
    }
}
