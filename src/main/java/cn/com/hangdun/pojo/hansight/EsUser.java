package cn.com.hangdun.pojo.hansight;

public class EsUser {
		String id ;
		//用户名
		String username;
		
		String password;
		//身份证
		String pid;
		String createtime;
		//姓名
		String name;
		
		
		
		
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getPassword() {
			return password;
		}
		public void setPassword(String password) {
			this.password = password;
		}
		public String getPid() {
			return pid;
		}
		public void setPid(String pid) {
			this.pid = pid;
		}
		public String getCreatetime() {
			return createtime;
		}
		public void setCreatetime(String createtime) {
			this.createtime = createtime;
		}
		public String getUsername() {
			return username;
		}
		public void setUsername(String username) {
			this.username = username;
		}
		@Override
		public String toString() {
			return "EsUser [id=" + id + ", username=" + username + ", password=" + password + ", pid=" + pid
					+ ", createtime=" + createtime + "]";
		}
}
