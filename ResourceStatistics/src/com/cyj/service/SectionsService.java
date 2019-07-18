package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Sections;

public interface SectionsService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param stu
	 * @return
	 */
	public Boolean addOneSections(Sections stu);

	/**
	 * 修改
	 */
	public Boolean updateSections(Sections stu);

	/**
	 * 删除,根据id
	 */
	public Boolean deleteSectionsById(int id);

	/**
	 * 查询集合 查询所有Sections信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);
	
	/**
	 * 查询 根据id查询
	 */
	public List<Sections> getById(String id);
	
}
