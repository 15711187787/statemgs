package cn.com.hangdun.pojo.mgs;

/**
 * 传给门户的我的待办实体
 * @author moban
 *
 */
public class Mgs {
	private String MgsFromDept="网信";//	信息来源部门	
	private String MgsFromSys="态势感知";//	信息来源系统	必填
	private String MgsType="业务部门承接";//	待办事项类型	
	private String MgsFunc="消息";//	功能	
	private String MgsMessage;//	待办留言	
	private String MgsFromName="态势感知";//	信息来源人	必填
	private String SentTime;//	待办消息时间	必填
	private String MgsUrgent;//	消息缓急程度:一般、急件、特急	
	private String MgsStatus="0";//	信息是否浏览过:1.未浏览;0.已浏览	
	private String MgsAccessory="0";//	附件个数,0为无附件	
	private String Title;//	提示信息（门户系统中显示的标题信息，若是任务则应为任务名称)	必填
	private String Url;//	应用系统处理地址	必填
//	private String Url="http://10.21.164.95:8188/es/loginJumpToDo?";//	应用系统处理地址	必填
	@Override
	public String toString() {
		StringBuffer sbuffer = new StringBuffer();
		sbuffer.append("<Mgs>");
		sbuffer.append("<MgsFromDept>"+this.MgsFromDept+"</MgsFromDept>");
		sbuffer.append("<MgsFromSys>"+this.MgsFromSys+"</MgsFromSys>");
		sbuffer.append("<MgsType>"+this.MgsType+"</MgsType>");
		sbuffer.append("<MgsFunc>"+this.MgsFunc+"</MgsFunc>");
		sbuffer.append("<MgsMessage>"+this.MgsMessage+"</MgsMessage>");
		sbuffer.append("<MgsFromName>"+this.MgsFromName+"</MgsFromName>");
		sbuffer.append("<SentTime>"+this.SentTime+"</SentTime>");
		sbuffer.append("<MgsUrgent>"+this.MgsUrgent+"</MgsUrgent>");
		sbuffer.append("<MgsStatus>"+this.MgsStatus+"</MgsStatus>");
		sbuffer.append("<Title>"+this.Title+"</Title>");
		sbuffer.append("<Url>"+this.Url+"</Url>");
		sbuffer.append("</Mgs>");
		return sbuffer.toString();
	}
	public String getMgsFromDept() {
		return MgsFromDept;
	}
	public void setMgsFromDept(String mgsFromDept) {
		MgsFromDept = mgsFromDept;
	}
	public String getMgsFromSys() {
		return MgsFromSys;
	}
	public void setMgsFromSys(String mgsFromSys) {
		MgsFromSys = mgsFromSys;
	}
	public String getMgsType() {
		return MgsType;
	}
	public void setMgsType(String mgsType) {
		MgsType = mgsType;
	}
	public String getMgsFunc() {
		return MgsFunc;
	}
	public void setMgsFunc(String mgsFunc) {
		MgsFunc = mgsFunc;
	}
	public String getMgsMessage() {
		return MgsMessage;
	}
	public void setMgsMessage(String mgsMessage) {
		MgsMessage = mgsMessage;
	}
	public String getMgsFromName() {
		return MgsFromName;
	}
	public void setMgsFromName(String mgsFromName) {
		MgsFromName = mgsFromName;
	}
	public String getSentTime() {
		return SentTime;
	}
	public void setSentTime(String sentTime) {
		SentTime = sentTime;
	}
	public String getMgsUrgent() {
		return MgsUrgent;
	}
	public void setMgsUrgent(String mgsUrgent) {
		MgsUrgent = mgsUrgent;
	}
	public String getMgsStatus() {
		return MgsStatus;
	}
	public void setMgsStatus(String mgsStatus) {
		MgsStatus = mgsStatus;
	}
	public String getMgsAccessory() {
		return MgsAccessory;
	}
	public void setMgsAccessory(String mgsAccessory) {
		MgsAccessory = mgsAccessory;
	}
	public String getTitle() {
		return Title;
	}
	public void setTitle(String title) {
		Title = title;
	}
	public String getUrl() {
		return Url;
	}
	public void setUrl(String url) {
		Url = url;
	}
}
