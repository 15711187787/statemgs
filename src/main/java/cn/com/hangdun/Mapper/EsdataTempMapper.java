package cn.com.hangdun.Mapper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.springframework.stereotype.Repository;

import cn.com.hangdun.pojo.hansight.EsdataTemp;

@Repository
public interface EsdataTempMapper {
	public void insertEs(EsdataTemp esdataTemp);
	
	public EsdataTemp getByNumber(String number);

	public ArrayList<EsdataTemp> esYesterday(String date);

	public EsdataTemp esYesterdayOne(String date);

	public ArrayList<EsdataTemp> esdataAll();


	public Integer getdaySum(String time);

	public Integer esdataLevelSum(HashMap map);

	public Integer esLevelSum(int level);

	public void updateEs(EsdataTemp esdataTemp);

	public List<EsdataTemp> getOne();

	public List<EsdataTemp> getTodoMsg(HashMap map);

}
