package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Memberships;
import com.cyj.service.MemberShipsService;
import com.jfinal.plugin.activerecord.Page;

public class MemberShipsServiceImpl implements MemberShipsService {

	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param stu
	 * @return
	 */
	public Boolean addOneMemberShips(Memberships stu) {
		return stu.save();
	}

	/**
	 * 修改
	 */
	public Boolean updateMemberShips(Memberships stu) {
		return stu.update();
	}

	/**
	 * 删除,根据id
	 */
	public Boolean deleteMemberShipsById(int id) {
		return Memberships.dao.deleteById(id);
	}

	/**
	 * 查询集合 查询所有MemberShips信息
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Memberships> pages = Memberships.dao
				.paginate(
						page,
						rows,
						"select memberships_id,memberships_department,memberships_specialty,memberships_degree",
						sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询 根据id查询
	 */
	public Memberships getById(int id) {
		return Memberships.dao.findById(id);
	}

	@Override
	public List<Memberships> getByStringId(String id) {
		return Memberships.dao
				.find("select memberships_id,memberships_department,memberships_specialty,memberships_degree from memberships where memberShips_id IN ("
						+ id + ")");
	}

}
