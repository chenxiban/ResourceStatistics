package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Consumelogs;
import com.cyj.service.ConsumelogsService;
import com.jfinal.plugin.activerecord.Page;

public class ConsumelogsServiceImpl implements ConsumelogsService {

	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param con
	 * @return
	 */
	public Boolean addOneConsumelogs(Consumelogs con) {
		return con.save();
	}

	/**
	 * 查询集合 查询所有Consumelogs信息
	 */
	public List<Consumelogs> getList() {
		return Consumelogs.dao
				.find("SELECT consumelogs_id,consumelogs_cardNo,readRoomId,consumelogs_inTime,consumelogs_outTime,consumelogs_status From Consumelogs");
	}

	/**
	 * 查询 根据id查询
	 */
	public Consumelogs getById(int id) {
		return Consumelogs.dao.findById(id);
	}

	/**
	 * 查询 根据卡号查询
	 */
	public List<Consumelogs> getByCardNo(String consumelogs_cardNo) {
		return Consumelogs.dao
				.find("select * from Consumelogs where consumelogs_cardNo ="
						+ consumelogs_cardNo);
	}

	/**
	 * 表连接分页查询,获得阅览室名称
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Consumelogs> pages = Consumelogs.dao
				.paginate(
						page,
						rows,
						"SELECT c.consumelogs_id,c.consumelogs_cardNo,c.readRoomId,c.consumelogs_inTime,c.consumelogs_outTime,c.consumelogs_status,r.readrooms_name ",
						sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询 根据String id查询
	 */
	public List<Consumelogs> getByStringId(String id) {
		return Consumelogs.dao
				.find("SELECT consumelogs_id,consumelogs_cardNo,readRoomId,consumelogs_inTime,consumelogs_outTime,consumelogs_status from consumelogs where consumelogs_id IN ("
						+ id + ")");
	}

	/**
	 * 查询集合 查询所有阅览室信息
	 */
	/*
	 * public List<Consumelogs> getRoomId(String data) { return Consumelogs.dao
	 * .find(
	 * "SELECT DISTINCT readRoomId FROM consumelogs WHERE consumelogs_inTime LIKE '%"
	 * + data + "%'"); }
	 */

	/**
	 * 获取INT 求RoomId的总数
	 */
	public List<Consumelogs> getByCount(int readId, String date) {
		return Consumelogs.dao
				.find("SELECT readRoomId, COUNT(readRoomId) AS statistics_peopleNums FROM Consumelogs WHERE readRoomId="
						+ readId
						+ " AND consumelogs_inTime LIKE '%"
						+ date
						+ "%' GROUP BY readRoomId");
	}

	/**
	 * 查询集合 根据String year,String month,String day查询所有Consumelogs信息
	 */
	public List<Consumelogs> getHighCharts(String sql) {
		return Consumelogs.dao
				.find("SELECT s.students_name AS name,COUNT(c.consumelogs_cardNo) AS data "
						+ sql + " GROUP BY c.consumelogs_cardNo");
	}

}
