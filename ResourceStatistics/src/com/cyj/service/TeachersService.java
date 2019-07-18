package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Teachers;

public interface TeachersService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param tea
	 * @return
	 */
	public Boolean addOneTeachers(Teachers tea);

	/**
	 * 修改
	 */
	public Boolean updateTeachers(Teachers tea);

	/**
	 * 删除,根据id
	 */
	public Boolean deleteTeachersById(int id);
	
	/**
	 * 查询 所有
	 */
	public List<Teachers> getList();

	/**
	 * 查询集合 查询所有Teachers信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);
	
	/**
	 * 查询 根据id查询
	 */
	public Teachers getById(int id);
	
	/**
	 * 查询 根据id查询
	 */
	public List<Teachers> getByStringId(String id);
	
}
