package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Teachers;
import com.cyj.service.TeachersService;
import com.jfinal.plugin.activerecord.Page;

public class TeachersServiceImpl implements TeachersService {

	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param tea
	 * @return
	 */
	public Boolean addOneTeachers(Teachers tea) {
		return tea.save();
	}

	/**
	 * 修改
	 */
	public Boolean updateTeachers(Teachers tea) {
		return tea.update();
	}

	/**
	 * 删除,根据id
	 */
	public Boolean deleteTeachersById(int id) {
		return Teachers.dao.deleteById(id);
	}

	/**
	 * 查询集合 查询所有Teachers信息
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Teachers> pages = Teachers.dao
				.paginate(
						page,
						rows,
						"SELECT teachers_id,teachers_cardNo,teachers_name,teachers_sex,sectionID,teachers_status,teachers_remark",
						sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询 根据id查询
	 */
	public Teachers getById(int id) {
		return Teachers.dao.findById(id);
	}

	/**
	 * 查询 根据多个id查询
	 */
	public List<Teachers> getByStringId(String id) {
		return Teachers.dao
				.find("SELECT teachers_id,teachers_cardNo,teachers_name,teachers_sex,sectionID,teachers_status,teachers_remark FROM teachers WHERE teachers_id IN ("
						+ id + ")");
	}

	/**
	 * 查询 所有
	 */
	public List<Teachers> getList() {
		return Teachers.dao
				.find("SELECT teachers_id,teachers_cardNo,teachers_name,teachers_sex,sectionID,teachers_status,teachers_remark FROM teachers");
	}

}
