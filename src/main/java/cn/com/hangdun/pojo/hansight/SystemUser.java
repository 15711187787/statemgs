package cn.com.hangdun.pojo.hansight;

public class SystemUser {
	String id;
	String login_name;
	String login_password;
	String real_name;
	int locked;
	int locked_time;
	int login_fail_time;
	String email;
	String mobile;
	String unit_id;
	String creator;
	String id_path;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getLogin_name() {
		return login_name;
	}
	public void setLogin_name(String login_name) {
		this.login_name = login_name;
	}
	public String getLogin_password() {
		return login_password;
	}
	public void setLogin_password(String login_password) {
		this.login_password = login_password;
	}
	public String getReal_name() {
		return real_name;
	}
	public void setReal_name(String real_name) {
		this.real_name = real_name;
	}
	public int getLocked() {
		return locked;
	}
	public void setLocked(int locked) {
		this.locked = locked;
	}
	public int getLocked_time() {
		return locked_time;
	}
	public void setLocked_time(int locked_time) {
		this.locked_time = locked_time;
	}
	public int getLogin_fail_time() {
		return login_fail_time;
	}
	public void setLogin_fail_time(int login_fail_time) {
		this.login_fail_time = login_fail_time;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getMobile() {
		return mobile;
	}
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}
	public String getUnit_id() {
		return unit_id;
	}
	public void setUnit_id(String unit_id) {
		this.unit_id = unit_id;
	}
	public String getCreator() {
		return creator;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getId_path() {
		return id_path;
	}
	public void setId_path(String id_path) {
		this.id_path = id_path;
	}

}
