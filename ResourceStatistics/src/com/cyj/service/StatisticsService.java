package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Statistics;

public interface StatisticsService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param sta
	 * @return
	 */
	public Boolean addOneStatistics(Statistics sta);

	/**
	 * 查询集合 查询所有Statistics信息
	 */
	public List<Statistics> getList();
	
	/**
	 * 查询集合 根据String year,String month,String day查询所有Statistics信息
	 */
	public List<Statistics> getHighCharts(String sql);
	
	/**
	 * 查询集合 根据ids查询所有Statistics信息
	 */
	public List<Statistics> getByStringId(String ids);
	
	/**
	 * 查询 根据id查询
	 */
	public Statistics getById(int id);
	
	/**
	 * 查询集合 查询所有Statistics信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);
	
}
