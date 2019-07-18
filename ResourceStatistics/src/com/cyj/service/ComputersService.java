package com.cyj.service;

import java.util.List;

import com.cyj.entity.Computers;

public interface ComputersService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param com
	 * @return
	 */
	public Boolean addOneComputers(Computers com);

	/**
	 * 修改
	 */
	public Boolean updateComputers(Computers com);

	/**
	 * 删除,根据id
	 */
	public Boolean deleteComputersById(int id);

	/**
	 * 查询集合 查询所有Computers信息
	 */
	public List<Computers> getList();
	
	/**
	 * 查询 根据id查询
	 */
	public Computers getById(int id);
	
}
