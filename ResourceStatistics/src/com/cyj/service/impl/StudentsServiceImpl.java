package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Students;
import com.cyj.service.StudentsService;
import com.jfinal.plugin.activerecord.Page;

public class StudentsServiceImpl implements StudentsService {

	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param stu
	 * @return
	 */
	public Boolean addOneStudents(Students stu) {
		return stu.save();
	}

	/**
	 * 修改
	 */
	public Boolean updateStudents(Students stu) {
		return stu.update();
	}

	/**
	 * 删除,根据id
	 */
	public Boolean deleteStudentsById(int id) {
		return Students.dao.deleteById(id);
	}

	/**
	 * 查询集合 查询所有Students信息
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Students> pages = Students.dao
				.paginate(
						page,
						rows,
						"select students_id,students_cardNo,students_name,students_sex,membershipId,students_status,students_remark,students_stuNo",
						sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询 根据id查询
	 */
	public Students getById(int id) {
		return Students.dao.findById(id);
	}

	/**
	 * 查询 根据id查询
	 */
	public List<Students> getByStringId(String id) {
		return Students.dao
				.find("SELECT students_id,students_cardNo,students_name,students_sex,membershipId,students_status,students_remark,students_stuNo FROM students WHERE students_id IN ("
						+ id + ")");
	}

	/**
	 * 查询 所有
	 */
	public List<Students> getList() {
		return Students.dao
				.find("SELECT students_id,students_cardNo,students_name,students_sex,membershipId,students_status,students_remark,students_stuNo FROM students");
	}

}
