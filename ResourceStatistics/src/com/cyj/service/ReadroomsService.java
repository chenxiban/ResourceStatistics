package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Readrooms;

public interface ReadroomsService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param rea
	 * @return
	 */
	public Boolean addOneReadrooms(Readrooms rea);

	/**
	 * 修改
	 */
	public Boolean updateReadrooms(Readrooms rea);

	/**
	 * 删除,根据id
	 */
	public Boolean deleteReadroomsById(int id);

	/**
	 * 查询集合 查询所有Readrooms信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);
	
	/**
	 * 查询 所有阅览室id
	 */
	public List<Readrooms> getAllReadrooms();
	
	/**
	 * 查询 根据id查询
	 */
	public Readrooms getById(int id);
	
	/**
	 * 查询 根据Srting id查询
	 */
	public List<Readrooms> getByStringId(String id);
	
}
