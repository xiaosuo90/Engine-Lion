package com.work.gongchenglion.unity;

public class ItemCell {
	private String cellValue = "";//��Ԫ���ֵ
	
	private CellTypeEnum cellType = CellTypeEnum.LABEL; //��Ԫ������
	
	private int colNum = 0;  //��Ԫ���к�
	
	private boolean isChange = false;//�Ƿ񱻱༭
	
	public ItemCell(String cellValue,CellTypeEnum cellType){
		this.cellValue = cellValue;
		this.cellType = cellType;
	}
	public void setColNum(int colNum){
		this.colNum = colNum;
	}
	public int getColNum(){
		return this.colNum;
	}
	public String getCellValue(){
		return cellValue;
	}
	public void setCellValue(String value){
		this.cellValue = value;
	}
	public CellTypeEnum getCellType(){
		return cellType;
	}
	public void setIsChange(boolean isChange){
		this.isChange = isChange;
	}
	public boolean getIsChange(){
		return this.isChange;
	}

}
