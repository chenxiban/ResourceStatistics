package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Sections;
import com.cyj.service.SectionsService;
import com.jfinal.plugin.activerecord.Page;

public class SectionsServiceImpl implements SectionsService {
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param stu
	 * @return
	 */
	public Boolean addOneSections(Sections stu) {
		return stu.save();
	}

	/**
	 * 修改
	 */
	public Boolean updateSections(Sections stu) {
		return stu.update();
	}

	/**
	 * 删除,根据id
	 */
	public Boolean deleteSectionsById(int id) {
		return Sections.dao.deleteById(id);
	}

	/**
	 * 查询集合 查询所有Sections信息
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Sections> pages = Sections.dao.paginate(page, rows,
				"select sections_id,sections_name,sections_remark", sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询 根据id查询
	 */
	public List<Sections> getById(String id) {
		return Sections.dao
				.find("SELECT sections_id,sections_name,sections_remark FROM sections WHERE sections_id IN ("
						+ id + ")");
	}
}
