package cn.com.hangdun.pojo.hansight;

public class EsdataTemp {
	//主键
	private int id;
	//索引名称
	private String index;
	//告警级别
	private String level;
	//创建时间
	private String starttime;
	//告警名称
	private String name;
	//告警详情
	private String content;
	//告警编号
	private String number;
	//插入时间
	private String inserttime;
	//事件id
	private String inid;
	//处置状态
	private String status;
	
	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	@Override
	public String toString() {
		return "EsdataTemp [id=" + id + ", index=" + index + ", level=" + level + ", starttime=" + starttime + ", name="
				+ name + ", content=" + content + ", number=" + number + ", inserttime=" + inserttime + ", inid=" + inid
				+ ", status=" + status + "]";
	}

	public String getInid() {
		return inid;
	}

	public void setInid(String inid) {
		this.inid = inid;
	}

	public String getInserttime() {
		return inserttime;
	}

	public void setInserttime(String inserttime) {
		this.inserttime = inserttime;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getLevel() {
		return level;
	}

	public void setLevel(String level) {
		this.level = level;
	}

	public String getStarttime() {
		return starttime;
	}

	public void setStarttime(String starttime) {
		this.starttime = starttime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	
}
