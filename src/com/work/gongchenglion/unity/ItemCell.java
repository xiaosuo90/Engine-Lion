package com.work.gongchenglion.unity;

public class ItemCell {
	private String cellValue = "";//单元格的值
	
	private CellTypeEnum cellType = CellTypeEnum.LABEL; //单元格类型
	
	private int colNum = 0;  //单元格列号
	
	private boolean isChange = false;//是否被编辑
	
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
