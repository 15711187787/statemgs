package cn.com.hangdun.Mapper;

import org.springframework.stereotype.Repository;

import cn.com.hangdun.pojo.hansight.SystemUser;

@Repository
public interface SystemUserMapper {
	public SystemUser getById(String pid);
}
