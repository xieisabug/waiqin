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
 * 弹出式菜单
 * 初始化弹出菜单：popMenu = new PopMenu(this);
 * 添加项目为：popMenu.addItems(typeArray);
 * 需要为Menu添加事件：要创建OnItemClickListener，为每个位置的item实现事件。
 * 为弹出菜单的按钮注册OnClickListener事件：popMenu.showAsDropDown(v)
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

        // 设置 listview
        listView = (ListView) view.findViewById(R.id.listView);
        listView.setAdapter(new PopAdapter());

        //popupWindow = new PopupWindow(view, LayoutParams.FILL_PARENT - 15, LayoutParams.WRAP_CONTENT);
        popupWindow = new PopupWindow(view, context.getResources().getDimensionPixelSize(
                R.dimen.popmenu_width), LayoutParams.WRAP_CONTENT);

        // 这个是为了点击“返回Back”也能使其消失，并且并不会影响你的背景（很神奇的）
        popupWindow.setBackgroundDrawable(new BitmapDrawable());
    }

    // 设置菜单项点击监听器
    public void setOnItemClickListener(OnItemClickListener listener) {
        // this.listener = listener;
        listView.setOnItemClickListener(listener);
    }

    // 批量添加菜单项
    public void addItems(String[] items) {
        Collections.addAll(itemList, items);
    }

    // 单个添加菜单项
    public void addItem(String item) {
        itemList.add(item);
    }

    // 下拉式 弹出 pop菜单 parent 右下角
    public void showAsDropDown(View parent) {
        popupWindow.showAsDropDown(parent, 10,
                // 保证尺寸是根据屏幕像素密度来的
                context.getResources().getDimensionPixelSize(R.dimen.popmenu_yoff));

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }

    public void showAtLocation(View parent) {
        popupWindow.showAtLocation(parent, Gravity.BOTTOM, 0, 0);

        // 使其聚集
        popupWindow.setFocusable(true);
        // 设置允许在外点击消失
        popupWindow.setOutsideTouchable(true);
        // 刷新状态
        popupWindow.update();
    }

    /**
     *  隐藏菜单
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

    // 适配器
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
