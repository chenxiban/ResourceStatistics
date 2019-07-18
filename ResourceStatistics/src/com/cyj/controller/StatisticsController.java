package com.cyj.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cyj.entity.Statistics;
import com.cyj.service.StatisticsService;
import com.cyj.service.impl.StatisticsServiceImpl;
import com.cyj.util.MyPoiHelp;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

public class StatisticsController extends Controller {

	private static StatisticsService statisticsService = new StatisticsServiceImpl();

	private static Calendar now = Calendar.getInstance();

	/**
	 * 接受对象 ,不使用参数前缀 http://localhost:8080/ResourceStatistics/sta/getAllSta
	 * 
	 */
	public void getAllSta() {
		int page = Integer.parseInt(getPara("page"));
		int rows = Integer.parseInt(getPara("rows"));
		String readRoomId = getPara("readRoomId");
		String years = getPara("years");
		String month = getPara("month");
		String day = getPara("day");
		System.out.println("年:" + years + "月:" + month + "日:" + day);
		String sql = "";
		if (readRoomId != null && readRoomId != "") {
			sql = "FROM statistics s INNER JOIN readrooms r ON s.readRoomId="
					+ readRoomId + " AND r.readrooms_id = " + readRoomId
					+ " WHERE 1=1";
		} else {
			sql = "FROM statistics s INNER JOIN readrooms r ON s.readRoomId=r.readrooms_id WHERE 1=1";
		}
		if (years != null && years != "") {
			sql += " and statistics_year like '%" + years + "%'";
		}
		if (month != null && month != "") {
			sql += " and statistics_month like '%" + month + "%'";
		}
		if (day != null && day != "") {
			sql += " and statistics_day like '%" + day + "%'";
		}
		System.out.println("sql========>" + sql);
		renderJson(statisticsService.getList(page, rows, sql));
	}

	/**
	 * 下载文件 http://localhost:8080/ResourceStatistics/sta/downloadFile?ids=1,2
	 * 
	 * @throws IOException
	 */
	public void downloadFile() throws IOException {// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath()
				+ "/download/file/导出的日统计记录表数据.xlsx";// 相对当前项目的路径
		String id = getPara("ids");
		File file = new File(mypath);// 文件对象
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		Workbook workBook = null;
		int count = 0;
		List<Statistics> list = statisticsService.getByStringId(id);// 要导出的数据集合
		// 判断集合为空则不创建Excel表格
		if (MyPoiHelp.isEmpty(list)) {
			return;
		}
		count = list.size();
		System.out.println("查询到的数据一共有:" + count + "条");
		System.out.println("查询到的数据为:" + list);
		// 创建一个Excel文件
		try {
			workBook = new HSSFWorkbook();// 构造一个xls后缀的EXCEL文件对象,2007
		} catch (Exception e) {
			e.printStackTrace();
			workBook = new XSSFWorkbook();// 构造一个xlsx后缀的EXCEL文件对象,2010
		}
		Sheet sheet = workBook.createSheet("statisticsData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(10);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("编号ID");
			cell = row1.createCell(1);
			cell.setCellValue("阅览室ID");
			cell = row1.createCell(2);
			cell.setCellValue("资源使用次数");
			cell = row1.createCell(3);
			cell.setCellValue("使用年");
			cell = row1.createCell(4);
			cell.setCellValue("使用月");
			cell = row1.createCell(5);
			cell.setCellValue("使用日");
			cell = row1.createCell(6);
			for (int j = 0; j < list.size(); j++) {
				row1 = sheet.createRow(i++);// 创建数据行
				cell.setCellType(CellType.STRING);// 设置单元格类型为字符文本
				cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置单元格默认样式
				cell = row1.createCell(0);
				cell.setCellValue(list.get(j).getInt("statistics_id"));
				cell = row1.createCell(1);
				cell.setCellValue(list.get(j).getInt("readRoomId"));
				cell = row1.createCell(2);
				cell.setCellValue(list.get(j).getInt("statistics_peopleNums"));
				cell = row1.createCell(3);
				cell.setCellValue(list.get(j).getInt("statistics_year"));
				cell = row1.createCell(4);
				cell.setCellValue(list.get(j).getInt("statistics_month"));
				cell = row1.createCell(5);
				cell.setCellValue(list.get(j).getInt("statistics_day"));
				cell = row1.createCell(6);
			}
			workBook.write(os);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (file.exists()) {
			renderFile(file);
		} else {
			renderNull();
		}
	}

	/**
	 * 显示图表
	 * http://localhost:8080/ResourceStatistics/sta/getHighChartsData?years=
	 * 2018&month=10&day=8
	 */
	public void getHighChartsData() {
		int year = now.get(Calendar.YEAR);// 获取今天的年
		int months = (now.get(Calendar.MONTH) + 1);// 获取今天的月
		System.out.println("今天的年为:" + year + ",今天的月为:" + months);
		String years = getPara("years");
		String month = getPara("month");
		String day = getPara("day");
		System.out.println("前台获得的,年:" + years + "月:" + month + "日:" + day);
		String sql = "FROM statistics s INNER JOIN readrooms r ON s.readRoomId=r.readrooms_id WHERE 1=1 ";
		if (years != null && years != "") {
			sql += " and statistics_year like '%" + years + "%'";
		} else {
			sql += " and statistics_year like '%" + year + "%'";
		}
		if (month != null && month != "") {
			sql += " and statistics_month like '%" + month + "%'";
		} else {
			sql += " and statistics_month like '%" + months + "%'";
		}
		if (day != null && day != "") {
			sql += " and statistics_day like '%" + day + "%'";
		}
		System.out.println("sql========>" + sql);
		List<Statistics> list=statisticsService.getHighCharts(sql);
		System.out.println(list);
		renderJson(list);
	}

}
