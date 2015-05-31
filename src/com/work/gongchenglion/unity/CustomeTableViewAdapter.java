package com.work.gongchenglion.unity;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;

import com.work.gongchenglion.R;

public class CustomeTableViewAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater inflater;
	private ArrayList<HashMap<String,Object>> lists;
	private ListView listView = null;
	private boolean isReadOnly = false;
	private int[] arrHeadWidth = null;
	
	public CustomeTableViewAdapter(Context context, ArrayList<HashMap<String,Object>> lists
			,ListView listView,boolean isReadOnly
			,int[] arrHeadWidth) {
		super();
		this.context = context;
		this.lists = lists;
		inflater = LayoutInflater.from(context);
		this.listView = listView;
		this.isReadOnly = isReadOnly;
		this.arrHeadWidth = arrHeadWidth;
		this.listView.setAdapter(this);
	}

	@Override
	public int getCount() {
		return lists.size();
	}

	@Override
	public Object getItem(int arg0) {
		return arg0;
	}

	@Override
	public long getItemId(int arg0) {
		return arg0;
	}
	@Override
	public View getView(int index, View view, ViewGroup arg2) {
		HashMap<String, Object> map = lists.get(index);
		String type = (String)map.get("rowtype");
		
		ArrayList<ItemCell> itemCells = new ArrayList<ItemCell>();
		for(int i=0;i<map.size()-1;i++){
			ItemCell itemCell = (ItemCell)map.get(i+"");
			itemCells.add(itemCell);
		}
		//性能优化后需要放开注释
		//getTag 获得View绑定的Object，这里是一个CustomeTableItem的对象
		if(view == null){
			view = inflater.inflate(R.layout.customel_list_item, null);//获取自定义View
			CustomeTableItem itemCustom = (CustomeTableItem)view.findViewById(R.id.custome_item);
			//向自定义View添加数据，添加显示
			itemCustom.buildItem(context,itemCells,arrHeadWidth,isReadOnly);
			view.setTag(itemCustom);
		}else{
			CustomeTableItem itemCustom = (CustomeTableItem)view.getTag();
			itemCustom.refreshData(itemCells);
		}
		if(index%2 == 0){
			view.setBackgroundColor(Color.argb(250 ,  255 ,  255 ,  255 )); 
		}else{
			view.setBackgroundColor(Color.argb(250 ,  224 ,  243 ,  250 ));    
		}
		return view;
	}

}
