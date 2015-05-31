package com.work.gongchenglion.ui;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.ContentValues;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.work.gongchenglion.R;
import com.work.gongchenglion.app.AppContext;
import com.work.gongchenglion.app.BaseActivity;
import com.work.gongchenglion.unity.CellTypeEnum;
import com.work.gongchenglion.unity.CustomeTableViewAdapter;
import com.work.gongchenglion.unity.HeadItemCell;
import com.work.gongchenglion.unity.ItemCell;

public class ResultActivity extends BaseActivity {

	private Button back_btn;
	private LayoutInflater inflater;
	private ListView lv_node, lv_pole;
	private CustomeTableViewAdapter adapter_node, adapter_pole = null;
	private ArrayList<HashMap<String, Object>> lists_node = new ArrayList<HashMap<String, Object>>();
	private ArrayList<HashMap<String, Object>> lists_pole = new ArrayList<HashMap<String, Object>>();
	private ContentValues resultValues;
	private AppContext app;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.result);

		app = (AppContext) getApplication();
		back_btn = (Button) findViewById(R.id.back_btn);
		back_btn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				back();
				app.stop();
			}
		});

		/*
		 * 创建表格视图
		 */
		resultValues = app.getCoreValues();
		lv_node = (ListView) findViewById(R.id.listview_node);
		lv_pole = (ListView) findViewById(R.id.listview_pole);
		inflater = LayoutInflater.from(this);

		this.addDataNode();
		this.addPoleNode();

	}

	/*
	 * *************************************************************** /*
	 * 
	 * 添加数据核心步骤方法
	 */
	// 点表总方法
	private void addDataNode() {

		ViewGroup root_node = (ViewGroup) findViewById(R.id.linearlayout_head_node);

		int[] arrNodeHeadWidth = null;// 首行Cell的Width集合

		HashMap<String, HeadItemCell> headMap = new HashMap<String, HeadItemCell>();// 包含整个首行信息的泛型
		// 增加首行的內容
		this.cellAddHead(headMap, "节点ID");// 得到新的headMap
		this.cellAddHead(headMap, "节点力偏移X");
		this.cellAddHead(headMap, "节点力偏移Y");
		this.cellAddHead(headMap, "节点力X");
		this.cellAddHead(headMap, "节点力Y");
		arrNodeHeadWidth = this.addHead(headMap, arrNodeHeadWidth, root_node);
		// 添加主体内容
		this.addNodeContent();
		// 设置展示的样式
		adapter_node = new CustomeTableViewAdapter(this, lists_node, lv_node,
				false, arrNodeHeadWidth);

		// 调用父类的方法，监听内容是否改变
		adapter_node.notifyDataSetChanged();

	}

	// 杆表总方法
	private void addPoleNode() {

		ViewGroup root_pole = (ViewGroup) findViewById(R.id.linearlayout_head_pole);

		int[] arrPoleHeadWidth = null;// 首行Cell的Width集合

		HashMap<String, HeadItemCell> headMap = new HashMap<String, HeadItemCell>();// 包含整个首行信息的泛型
		// 增加首行的內容
		this.cellAddHead(headMap, "杆ID");// 得到新的headMap
		this.cellAddHead(headMap, "杆力");

		arrPoleHeadWidth = this.addHead(headMap, arrPoleHeadWidth, root_pole);
		// 添加主体内容
		this.addPoleContent();
		// 设置展示的样式
		adapter_pole = new CustomeTableViewAdapter(this, lists_pole, lv_pole,
				false, arrPoleHeadWidth);
		// 调用父类的方法，监听内容是否改变
		adapter_pole.notifyDataSetChanged();
	}

	/*
	 * 增加首行的內容
	 */

	// 添加首行的单个Cell
	private void cellAddHead(HashMap<String, HeadItemCell> headMap,
			String headName) {
		HeadItemCell itemCell = new HeadItemCell(headName, 100);
		headMap.put(headMap.size() + "", itemCell);// 加双引号将int强转String
	}

	// 增加整个首行
	private int[] addHead(HashMap<String, HeadItemCell> headMap,
			int[] arrHeadWidth, ViewGroup root) {

		arrHeadWidth = new int[headMap.size()];// 首行Cell的Width集合
		int width = 0;
		for (int i = 0; i < headMap.size(); i++) {
			HeadItemCell itemCell = headMap.get(i + "");// 提出单个Cell
			String name = itemCell.getCellValue();
			width = Dp2Px(this, itemCell.getWidth());

			setHeadName(name, width, root);// 设置文字的存储方式

			arrHeadWidth[i] = width;
			if (i != headMap.size() - 1) {// 添加竖线
				this.addVLine(root);
			}
		}
		return arrHeadWidth;
	}

	// 向headView添加TextView
	private void setHeadName(String name, int width, ViewGroup root) {
		TextView headView = (TextView) inflater.inflate(
				R.layout.atom_head_text_view, root, false);
		if (headView != null) {
			String viewName = "<b>" + name + "</b>";
			headView.setText(Html.fromHtml(viewName));
			headView.setWidth(width);
			root.addView(headView);
		}
	}

	/*
	 * 添加主体内容
	 */
	// 添加点主体数据
	private void addNodeContent() {

		int sum_node = app.getSum_node();

		for (int i = 0; i < sum_node; i++) {
			HashMap<String, Object> rowMap = new HashMap<String, Object>();// 一行的数据
			lists_node.add(rowMap);

			this.addRows(rowMap, i + "", CellTypeEnum.LABEL);
			this.addRows(rowMap, resultValues.get("node_Disp_X" + i + 1)
					.toString(), CellTypeEnum.LABEL);
			this.addRows(rowMap, resultValues.get("node_Disp_Y" + i + 1)
					.toString(), CellTypeEnum.LABEL);
			this.addRows(rowMap, resultValues.get("node_FORCE_X" + i + 1)
					.toString(), CellTypeEnum.LABEL);
			this.addRows(rowMap, resultValues.get("node_FORCE_Y" + i + 1)
					.toString(), CellTypeEnum.LABEL);
		}
	}

	// 添加杆主体数据
	private void addPoleContent() {
		int nm_of_element = app.getNm_of_element();

		for (int i = 0; i < nm_of_element; i++) {
			HashMap<String, Object> rowMap = new HashMap<String, Object>();// 一行的数据
			lists_pole.add(rowMap);
			this.addRows(rowMap, i + "", CellTypeEnum.LABEL);
			this.addRows(rowMap, resultValues.get("element_FORCE" + i + 1)
					.toString(), CellTypeEnum.LABEL);
		}
	}

	/*
	 * 功能单元
	 */
	// 添加一行中单个Cell的数据
	private void addRows(HashMap<String, Object> rowMap, String cellValue,
			CellTypeEnum cellType) {
		ItemCell itemCell = new ItemCell(cellValue, cellType);
		rowMap.put(rowMap.size() + "", itemCell);
	}

	private void addVLine(ViewGroup root) {
		LinearLayout v_line = (LinearLayout) getVerticalLine(root);
		v_line.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
		root.addView(v_line);
	}

	private View getVerticalLine(ViewGroup root) {
		return inflater.inflate(R.layout.atom_line_v_view, root, false);
	}

}
