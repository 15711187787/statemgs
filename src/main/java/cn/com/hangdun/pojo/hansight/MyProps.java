package cn.com.hangdun.pojo.hansight;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="myprops")
public class MyProps {
	public static String  ip;
	public static String  clusterName;
	public static String  clusterNode;
	public static String  clusterPort;
	public static String  poolSize;
	public  String  url;
	public boolean  snf;
	public static String  index;
	public static String  type;
	public static int  day;
	public static int  level;

	public  String getUrl() {
		return url;
	}

	public  void setUrl(String url) {
		this.url = url;
	}

	public  int getLevel() {
		return level;
	}

	public  void setLevel(int level) {
		MyProps.level = level;
	}

	public  int getDay() {
		return day;
	}

	public  void setDay(int day) {
		MyProps.day = day;
	}

	public static String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getClusterName() {
		return clusterName;
	}

	public void setClusterName(String clusterName) {
		this.clusterName = clusterName;
	}

	public String getClusterNode() {
		return clusterNode;
	}

	public void setClusterNode(String clusterNode) {
		this.clusterNode = clusterNode;
	}

	public String getClusterPort() {
		return clusterPort;
	}

	public void setClusterPort(String clusterPort) {
		this.clusterPort = clusterPort;
	}

	public String getPoolSize() {
		return poolSize;
	}

	public void setPoolSize(String poolSize) {
		this.poolSize = poolSize;
	}

	public boolean getSnf() {
		return snf;
	}

	public void setSnf(boolean snf) {
		this.snf = snf;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
