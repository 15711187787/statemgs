package cn.com.hangdun.Mapper;

import java.util.ArrayList;
import java.util.HashMap;

import org.springframework.stereotype.Repository;

import cn.com.hangdun.pojo.hansight.EsUser;
import cn.com.hangdun.pojo.hansight.EsdataTemp;

@Repository
public interface EsUserMapper {

	ArrayList<EsUser> getUserByUsername(String username);

	ArrayList<EsUser> getUserByPid(String pid);
	
}
