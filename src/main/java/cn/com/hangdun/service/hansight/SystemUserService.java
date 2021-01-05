package cn.com.hangdun.service.hansight;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import cn.com.hangdun.Mapper.SystemUserMapper;
import cn.com.hangdun.pojo.hansight.SystemUser;

@Service
public class SystemUserService{

	@Autowired
	SystemUserMapper systemUser;
	
	public SystemUser getById(String pid) {
		// TODO Auto-generated method stub
		return systemUser.getById(pid);
	}
	
}
