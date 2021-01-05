package cn.com.hangdun.pojo.mgs;

import java.util.List;

public class Mytask{
	private String rowNum="1";// 待办数量	必填
	private List<Mgs> Mgs; //待办列表
	public String getRowNum() {
		return rowNum;
	}
	public void setRowNum(String rowNum) {
		this.rowNum = rowNum;
	}
	public List<Mgs> getMgs() {
		return Mgs;
	}
	public void setMgs(List<Mgs> mgs) {
		Mgs = mgs;
	}
	@Override
	public String toString() {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("<?xml version='1.0' encoding='GBK'?>");
		sbuffer.append("<list type='Mgs' rowNum='");
		sbuffer.append(null==rowNum||"".equals(rowNum)?"0":rowNum);
		sbuffer.append("'>");
		for(Mgs mgs:Mgs){
			sbuffer.append(mgs.toString());
		}
		sbuffer.append("</list>");
		return sbuffer.toString();
	}
	
}
