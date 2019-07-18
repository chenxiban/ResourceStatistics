package com.cyj.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.cyj.entity.Statistics;
import com.cyj.service.StatisticsService;
import com.jfinal.plugin.activerecord.Page;

public class StatisticsServiceImpl implements StatisticsService {

	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param sta
	 * @return
	 */
	public Boolean addOneStatistics(Statistics sta) {
		return sta.save();
	}

	/**
	 * 查询集合 查询所有Statistics信息
	 */
	public List<Statistics> getList() {
		return Statistics.dao.find("select * from Statistics");
	}

	/**
	 * 查询 根据id查询
	 */
	public Statistics getById(int id) {
		return Statistics.dao.findById(id);
	}

	/**
	 * 查询集合 查询所有Statistics信息
	 */
	public Map<String, Object> getList(int page, int rows, String sql) {
		Map<String, Object> map = new HashMap<String, Object>();
		Page<Statistics> pages = Statistics.dao
				.paginate(
						page,
						rows,
						"SELECT s.statistics_id,s.readRoomId,s.statistics_peopleNums,s.statistics_year,s.statistics_month,s.statistics_day,r.readrooms_name ",
						sql);
		map.put("total", pages.getTotalRow());
		map.put("rows", pages.getList());
		return map;
	}

	/**
	 * 查询集合 根据ids查询所有Statistics信息
	 */
	public List<Statistics> getByStringId(String ids) {
		return Statistics.dao
				.find("SELECT statistics_id,readRoomId,statistics_peopleNums,statistics_year,statistics_month,statistics_day FROM statistics WHERE statistics_id IN ("
						+ ids + ")");
	}

	/**
	 * 查询集合 根据sql查询所有Statistics信息
	 */
	public List<Statistics> getHighCharts(String sql) {
		return Statistics.dao
				.find("SELECT r.readrooms_name AS name,SUM(s.statistics_peopleNums) AS data  "
						+ sql + " GROUP BY s.readRoomId");
	}

}
