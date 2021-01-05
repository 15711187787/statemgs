package cn.com.hangdun.service.hansight;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.hangdun.Mapper.EsdataTempMapper;
import cn.com.hangdun.pojo.hansight.EsdataTemp;

@Service
public class EsdataTempService {
	
	@Autowired
	private EsdataTempMapper esdataTempMapper;
	
	//获取ES数据添加到mysql中
	public void insertEs(EsdataTemp esdataTemp) {
		esdataTempMapper.insertEs(esdataTemp);
	}
	
	//getByname根据ES编号获取信息
	public EsdataTemp getByNumber(String number) {
		return esdataTempMapper.getByNumber(number);
	}

	public ArrayList<EsdataTemp> esYesterday(String date) {
		// TODO Auto-generated method stub
		return esdataTempMapper.esYesterday(date);
	}

	public EsdataTemp esYesterdayOne(String date) {
		// TODO Auto-generated method stub
		return esdataTempMapper.esYesterdayOne(date);
	}

	public ArrayList<EsdataTemp> esdataAll() {
		// TODO Auto-generated method stub
		return esdataTempMapper.esdataAll();
	}

	@SuppressWarnings({ "unchecked", "unchecked" })
	public ArrayList<HashMap>  esDataEcharts() {
		// TODO Auto-ge erated method stub
		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		 ArrayList lists = new ArrayList<>();
		 ArrayList<HashMap> list = new ArrayList<>();
		 String color[]= {"#ff8500","#ea1717","#d600e8"};
		 String daysum[]= new String[7];
		 for (int i = 0; i < 3; i++) {
			 HashMap<String , Object> map = new HashMap<>(); 
//			 if(i==0)
//				 map.put("name", "提醒");
			 if(i==0)
				 map.put("name", "低");
			 if(i==1)
				 map.put("name", "中");
			 if(i==2)
				 map.put("name", "高");
				 
			 map.put("color", color[i]);
//			 map.put("stack","总量");
			 Integer [] arr = new Integer[7];
	         Calendar c = null;
	         for (int j=0;j<7;j++){
	             c=Calendar.getInstance();
	             c.add(Calendar.DAY_OF_MONTH, - j);
	             HashMap<String,Object> map1 = new HashMap();
	             map1.put("time",sdf.format(c.getTime()));
				 map1.put("level",i+1);
	             daysum[6-j]=sdf.format(c.getTime());
	             arr[6-j] =esdataTempMapper.esdataLevelSum(map1);
	         }
			 map.put("data",arr);
			 map.put("type","line");
			 list.add(map);
		 }
		 lists.add(list);
		 lists.add(daysum);
		 return lists;
	}

	public ArrayList getesLevelSum() {
//		ArrayList<HashMap> list = new ArrayList<>();
		ArrayList list  = new ArrayList<>();
		HashMap<String,Object> map = new HashMap<>();
		Integer sum [] =new Integer [4];
		for (int i = 1; i < 4; i++) {
			Integer levelsum =esdataTempMapper.esLevelSum(i);
			list.add(levelsum);
		}
		return list;
	}

	public void updateEs(EsdataTemp esdataTemp) {
		// TODO Auto-generated method stub
		esdataTempMapper.updateEs(esdataTemp);
	}

	public List<EsdataTemp>  getOne() {
		// TODO Auto-generated method stub
		return  esdataTempMapper.getOne();
	}

	public List<EsdataTemp> getTodoMsg() {
		// TODO Auto-generated method stub
		HashMap map = new HashMap<>();
		SimpleDateFormat sm = new SimpleDateFormat("yyyy-MM-dd");
		String enddate = sm.format(new Date());
		Calendar ca = Calendar.getInstance();
		ca.add(Calendar.DAY_OF_MONTH, -30);
		String startdate = sm.format(ca.getTime());
		map.put("startdate", startdate);
		map.put("enddate",enddate);
		System.out.println(startdate);
		System.out.println(enddate);
		return esdataTempMapper.getTodoMsg(map);
	}
	
	
}
