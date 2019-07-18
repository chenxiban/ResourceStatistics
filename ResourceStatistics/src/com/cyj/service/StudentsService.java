package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Students;

public interface StudentsService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param stu
	 * @return
	 */
	public Boolean addOneStudents(Students stu);

	/**
	 * 修改
	 */
	public Boolean updateStudents(Students stu);

	/**
	 * 删除,根据id
	 */
	public Boolean deleteStudentsById(int id);
	
	/**
	 * 查询 所有
	 */
	public List<Students> getList();

	/**
	 * 查询集合 分页多条件查询所有Students信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);
	
	/**
	 * 查询 根据id查询
	 */
	public Students getById(int id);
	
	/**
	 * 查询 根据id查询
	 */
	public List<Students> getByStringId(String id);
	
}
