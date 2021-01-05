package cn.com.hangdun.service.hansight;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.hangdun.Mapper.EsUserMapper;
import cn.com.hangdun.pojo.hansight.EsUser;

@Service
public class EsUserService {
	@Autowired
	public EsUserMapper esUserMapper;

	public ArrayList<EsUser> getUserByUsername(String username) {
		// TODO Auto-generated method stub
		return esUserMapper.getUserByUsername(username);
	}

	public ArrayList<EsUser> getUserByPid(String pid) {
		// TODO Auto-generated method stub
		return esUserMapper.getUserByPid(pid);
	}
}
