package com.work.gongchenglion.unity;

/*
 * To Object
 * ���ݴ洢����֯��ʽ
 */

import java.util.ArrayList;

import com.work.gongchenglion.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class CustomeTableItem extends LinearLayout {
	private Context context = null;
	private boolean isRead = false;// �Ƿ�ֻ��
	private ArrayList<View> viewList = new ArrayList<View>();// �еı���б�
	private int[] headWidthArr = null;// ��ͷ���п�����

	public CustomeTableItem(Context context) {
		super(context);
	}

	public CustomeTableItem(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CustomeTableItem(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	/*
	 * rowType:�е���ʽ���ַ����⣬��ͬ��ʽ���в���Ҫ�ٴ����� itemCells:��Ԫ����Ϣ headWidthArr:ÿ�п��
	 * 
	 * isRead:�Ƿ�ֻ���������ֻ���������е����붼����Ч
	 */
	public void buildItem(Context context, ArrayList<ItemCell> itemCells,
			int[] headWidthArr, boolean isRead) {
		this.setOrientation(LinearLayout.VERTICAL);// ��һ�㲼�ִ�ֱ��LinearLayout����ķ���
		this.context = context;
		this.headWidthArr = headWidthArr.clone();

		this.addCell(itemCells);
	}

	//
	private void addCell(ArrayList<ItemCell> itemCells) {
		this.removeAllViews();

		LinearLayout secondLayout = new LinearLayout(context);
		secondLayout.setOrientation(LinearLayout.HORIZONTAL);
		secondLayout.setLayoutParams(new LinearLayout.LayoutParams(
				android.view.ViewGroup.LayoutParams.MATCH_PARENT,
				android.view.ViewGroup.LayoutParams.WRAP_CONTENT));

		this.addView(secondLayout);
		int cellIndex = 0;
		for (int i = 0; i < itemCells.size(); i++) {// ����itemCells�ڵ�����Cell
			ItemCell itemCell = itemCells.get(i);
			int endIndex = cellIndex + 1;// ��ռ����
			int width = getCellWidth(cellIndex, endIndex);// �п��
			cellIndex = endIndex;
			if (itemCell.getCellType() == CellTypeEnum.STRING) {
				EditText view = (EditText) getInputView();
				view.setText(itemCell.getCellValue());
				view.setWidth(width);
				this.setEditView(view);
				secondLayout.addView(view);
				viewList.add(view);
			} else if (itemCell.getCellType() == CellTypeEnum.DIGIT) {
				EditText view = (EditText) getInputView();
				view.setText(itemCell.getCellValue());
				view.setWidth(width);
				this.setEditView(view);
				this.setOnKeyBorad(view);
				secondLayout.addView(view);
				viewList.add(view);
			} else if (itemCell.getCellType() == CellTypeEnum.LABEL) {
				TextView view = (TextView) getLabelView();
				view.setText(itemCell.getCellValue());
				view.setWidth(width);
				secondLayout.addView(view);
				viewList.add(view);
			}
			if (i != itemCells.size() - 1) {// ��������
				LinearLayout v_line = (LinearLayout) getVerticalLine();
				v_line.setLayoutParams(new LinearLayout.LayoutParams(
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT,
						android.view.ViewGroup.LayoutParams.WRAP_CONTENT));
				secondLayout.addView(v_line);
			}
		}
	}

	public void refreshData(ArrayList<ItemCell> itemCells) {
		for (int i = 0; i < itemCells.size(); i++) {
			ItemCell itemCell = itemCells.get(i);
			if (itemCell.getCellType() == CellTypeEnum.LABEL) {
				TextView view = (TextView) viewList.get(i);
				view.setText(itemCell.getCellValue());
			} else if (itemCell.getCellType() == CellTypeEnum.DIGIT) {
				EditText view = (EditText) viewList.get(i);
				view.setText(itemCell.getCellValue());
				this.setEditView(view);
				this.setOnKeyBorad(view);
			} else if (itemCell.getCellType() == CellTypeEnum.STRING) {
				EditText view = (EditText) viewList.get(i);
				view.setText(itemCell.getCellValue());
				this.setEditView(view);
			}
		}
	}

	private View getVerticalLine() {
		return LayoutInflater.from(context).inflate(R.layout.atom_line_v_view,
				null);
	}

	private int getCellWidth(int cellStart, int cellEnd) {
		int width = 0;
		for (int i = cellStart; i < cellEnd; i++) {
			width = this.headWidthArr[i] + width;
		}
		return width;
	}

	private View getLabelView() {
		return LayoutInflater.from(context).inflate(R.layout.atom_text_view,
				null);
	}

	private View getInputView() {
		return LayoutInflater.from(context).inflate(R.layout.atom_edttxt_view,
				null);
	}

	private void setEditView(EditText edtText1) {
		if (this.isRead) {
			edtText1.setEnabled(false);
		} else {

		}
	}

	private void setOnKeyBorad(EditText edtText1) {
		// ���ּ���
		if (!this.isRead) {// ��ֻ��

		}
	}
}
