package com.cyj.service.impl;

import java.util.List;

import com.cyj.entity.Computers;
import com.cyj.service.ComputersService;

public class ComputersServiceImpl implements ComputersService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param com
	 * @return
	 */
	public Boolean addOneComputers(Computers com) {
		return com.save();
	}

	/**
	 * 修改
	 */
	public Boolean updateComputers(Computers com) {
		return com.update();
	}

	/**
	 * 删除,根据id
	 */
	public Boolean deleteComputersById(int id) {
		return Computers.dao.deleteById(id);
	}

	/**
	 * 查询集合 查询所有Computers信息
	 */
	public List<Computers> getList() {
		return Computers.dao.find("select * from Computers");
	}
	
	/**
	 * 查询 根据id查询
	 */
	public Computers getById(int id) {
		return Computers.dao.findById(id);
	}
	
}
