package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Readrooms;
import com.cyj.service.ReadroomsService;
import com.jfinal.plugin.activerecord.Page;

public class ReadroomsServiceImpl implements ReadroomsService {

	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param rea
	 * @return
	 */
	public Boolean addOneReadrooms(Readrooms rea) {
		return rea.save();
	}

	/**
	 * 修改
	 */
	public Boolean updateReadrooms(Readrooms rea) {
		return rea.update();
	}

	/**
	 * 删除,根据id
	 */
	public Boolean deleteReadroomsById(int id) {
		return Readrooms.dao.deleteById(id);
	}

	/**
	 * 查询集合 查询所有Readrooms信息
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Readrooms> pages = Readrooms.dao.paginate(page, rows,
				"SELECT readrooms_id,readrooms_name,readrooms_remark", sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询 根据id查询
	 */
	public Readrooms getById(int id) {
		return Readrooms.dao.findById(id);
	}

	/**
	 * 查询 根据Srting id查询
	 */
	public List<Readrooms> getByStringId(String id) {
		return Readrooms.dao
				.find("SELECT readrooms_id,readrooms_name,readrooms_remark FROM readrooms WHERE readrooms_id IN ("
						+ id + ")");
	}

	/**
	 * 查询 所有阅览室id
	 */
	public List<Readrooms> getAllReadrooms() {
		return Readrooms.dao.find("SELECT readrooms_id,readrooms_name FROM readrooms");
	}

}
