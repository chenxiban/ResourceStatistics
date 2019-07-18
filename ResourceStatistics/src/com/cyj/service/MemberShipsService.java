package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Memberships;

public interface MemberShipsService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param stu
	 * @return
	 */
	public Boolean addOneMemberShips(Memberships stu);

	/**
	 * 修改
	 */
	public Boolean updateMemberShips(Memberships stu);

	/**
	 * 删除,根据id
	 */
	public Boolean deleteMemberShipsById(int id);

	/**
	 * 查询集合 分页多条件查询所有MemberShips信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);

	/**
	 * 查询 根据id查询
	 */
	public Memberships getById(int id);
	
	/**
	 * 查询 根据id查询
	 */
	public List<Memberships> getByStringId(String id);
	
}
