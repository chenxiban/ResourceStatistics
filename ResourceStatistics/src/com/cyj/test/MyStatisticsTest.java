package com.cyj.test;

import java.util.Calendar;
import java.util.List;
import java.util.Random;

import com.cyj.entity.Consumelogs;
import com.cyj.entity.Readrooms;
import com.cyj.entity.Statistics;
import com.cyj.service.ConsumelogsService;
import com.cyj.service.ReadroomsService;
import com.cyj.service.impl.ConsumelogsServiceImpl;
import com.cyj.service.impl.ReadroomsServiceImpl;

/**
 * 
 * 操作日统计记录表
 * 
 * @author 小佳佳 2018.10.06 17.00
 * 
 */
public class MyStatisticsTest implements Runnable {

	// 刷卡记录业务层
	private static ConsumelogsService consumelogsService = new ConsumelogsServiceImpl();
	// 阅览室业务层
	private static ReadroomsService readroomsService = new ReadroomsServiceImpl();
	private static Calendar now = Calendar.getInstance();

	public void run() {
		int year = now.get(Calendar.YEAR);// 获取今天的年
		int month = (now.get(Calendar.MONTH) + 1);// 获取今天的月
		int day = now.get(Calendar.DAY_OF_MONTH);// 获取今天的年日

		String months = "";
		// 处理月为单数时
		if ((now.get(Calendar.MONTH) + 1) < 10) {
			months += "0" + (now.get(Calendar.MONTH) + 1);
		} else {
			months += (now.get(Calendar.MONTH) + 1);
		}

		String days = "";
		// 处理日为单数时
		if (now.get(Calendar.DAY_OF_MONTH) < 10) {
			days += "0" + now.get(Calendar.DAY_OF_MONTH);
		} else {
			days += now.get(Calendar.DAY_OF_MONTH);
		}

		System.out.print("年: " + now.get(Calendar.YEAR) + "\t");
		System.out.print("月: " + (now.get(Calendar.MONTH) + 1) + "\t");
		System.out.println("日: " + now.get(Calendar.DAY_OF_MONTH));
		
		// 定义查询需要使用的date日期格式
		String data = year + "-" + months + "-" + days;
		System.out.println(data);
		try {
			// 防止多线程过于争抢资源
			Thread.sleep(new Random().nextInt(2000));
			// 查询所有ReadroomsId
			List<Readrooms> reaList=readroomsService.getAllReadrooms();
			List<Consumelogs> conList1= null;
			for (int i = 0; i < reaList.size(); i++) {
				// 根据ReadroomsId分组并且求使用次数getStatistics_peopleNums
				conList1 = consumelogsService.getByCount(reaList.get(i).getReadroomsId(),data);
				// 如果根据这个阅览室id查到的为空
				if (conList1==null || conList1.size()==0) {
					// 为日统计表添加记录包含,当天使用的阅览室,资源使用次数,年,月,日
					Statistics sta = new Statistics()
							.set("readRoomId", reaList.get(i).getReadroomsId())
							.set("statistics_peopleNums", 0)
							.set("statistics_year", year)
							.set("statistics_month", month)
							.set("statistics_day", day);
					Boolean boo = sta.save();
					System.out.println("日统计记录表添加,录入结果为:" + boo);
				} else {
					// 为日统计表添加记录包含,当天使用的阅览室,资源使用次数,年,月,日
					Statistics sta = new Statistics()
							.set("readRoomId", reaList.get(i).getReadroomsId())
							.set("statistics_peopleNums", conList1.get(0).getStatistics_peopleNums())
							.set("statistics_year", year)
							.set("statistics_month", month)
							.set("statistics_day", day);
					Boolean boo = sta.save();
					System.out.println("日统计记录表添加,录入结果为:" + boo);
				}
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

}
