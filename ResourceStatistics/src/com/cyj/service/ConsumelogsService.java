package com.cyj.service;

import java.util.List;
import java.util.Map;

import com.cyj.entity.Consumelogs;

public interface ConsumelogsService {
	
	/**
	 * 新增一个实体 控制器调用方式 *
	 * 
	 * @param con
	 * @return
	 */
	public Boolean addOneConsumelogs(Consumelogs con);

	/**
	 * 查询集合 查询所有Consumelogs信息
	 */
	public List<Consumelogs> getList();
	
	/**
	 * 查询集合 查询所有阅览室信息
	 */
	//public List<Consumelogs> getRoomId(String data);
	
	/**
	 * 查询集合 分页多条件查询所有Students信息
	 */
	public Map<String, Object> getList(int page,int rows,String sql);
	
	/**
	 * 查询 根据id查询
	 */
	public Consumelogs getById(int id);
	
	/**
	 * 查询 根据String id查询
	 */
	public List<Consumelogs> getByStringId(String id);
	
	/**
	 * 查询集合 根据String year,String month,String day查询所有Consumelogs信息
	 */
	public List<Consumelogs> getHighCharts(String sql);
	
	/**
	 * 查询 根据卡号查询
	 */
	public List<Consumelogs> getByCardNo(String consumelogs_cardNo);
	
	/**
	 * 获取INT  求RoomId的总数
	 */
	public List<Consumelogs> getByCount(int readId,String date);
	
}
