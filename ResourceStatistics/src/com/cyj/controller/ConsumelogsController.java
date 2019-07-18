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

import com.cyj.entity.Consumelogs;
import com.cyj.service.ConsumelogsService;
import com.cyj.service.impl.ConsumelogsServiceImpl;
import com.cyj.util.MyPoiHelp;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;

public class ConsumelogsController extends Controller {

	private static ConsumelogsService consumelogsService = new ConsumelogsServiceImpl();

	private static Calendar now = Calendar.getInstance();

	/**
	 * 查询所有信息 http://localhost:8080/ResourceStatistics/con/getAllConsumelogs
	 */
	public void getAllConsumelogs() {
		int page = Integer.parseInt(getPara("page"));
		int rows = Integer.parseInt(getPara("rows"));
		String consumelogs_cardNo = getPara("consumelogs_cardNo");
		String readRoomId = getPara("readRoomId");
		String startTime = getPara("startTime");
		String endTime = getPara("endTime");
		String startTimes = getPara("startTimes");
		String endTimes = getPara("endTimes");
		String consumelogs_status = getPara("consumelogs_status");
		String sql = "";
		if (readRoomId != null && readRoomId != "") {
			sql = " FROM consumelogs c INNER JOIN readrooms r ON c.readRoomId="
					+ readRoomId + " AND r.readrooms_id=" + readRoomId
					+ " WHERE 1=1 ";
		} else {
			sql = " FROM consumelogs c INNER JOIN readrooms r ON c.readRoomId=r.readrooms_id WHERE 1=1 ";
		}
		// 表连接分页查询,获得阅览室名称

		System.out.println("consumelogs_cardNo =====>" + consumelogs_cardNo);
		// 根据卡号查询
		if (consumelogs_cardNo != null && consumelogs_cardNo != "") {
			sql += " and c.consumelogs_cardNo like '%" + consumelogs_cardNo
					+ "%'";
		}
		// 根据状态查询
		if (consumelogs_status != null && consumelogs_status != "") {
			if (consumelogs_status.equals("离开")) {
				sql += " and c.consumelogs_status = 0";
			} else {
				sql += " and c.consumelogs_status = 1";
			}
		}
		// 进入时间的区间
		if (startTime != null && startTime != "" && endTime != null
				&& endTime != "") {
			sql += " and c.consumelogs_inTime BETWEEN '" + startTime
					+ "' AND '" + endTime + "'";
		}
		// 离开时间的区间
		if (startTimes != null && startTimes != "" && endTimes != null
				&& endTimes != "") {
			sql += " and c.consumelogs_outTime BETWEEN '" + startTimes
					+ "' AND '" + endTimes + "'";
		}
		System.out.println("sql========>" + sql);
		renderJson(consumelogsService.getList(page, rows, sql));
	}

	/**
	 * 下载文件 http://localhost:8080/ResourceStatistics/con/downloadFile?ids=1,2
	 * 
	 * @throws IOException
	 */
	public void downloadFile() throws IOException {// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath()
				+ "/download/file/导出的资源统计数据.xlsx";// 相对当前项目的路径
		String id = getPara("ids");
		File file = new File(mypath);// 文件对象
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		Workbook workBook = null;
		int count = 0;
		List<Consumelogs> list = consumelogsService.getByStringId(id);// 要导出的数据集合
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
		Sheet sheet = workBook.createSheet("consumelogsData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(10);
		// 设置第2列的列宽为50个字符宽度
		sheet.setColumnWidth(2, 20 * 256);
		// 设置第3列的列宽为50个字符宽度
		sheet.setColumnWidth(3, 20 * 256);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("编号");
			cell = row1.createCell(1);
			cell.setCellValue("卡号");
			cell = row1.createCell(2);
			cell.setCellValue("资源编号");
			cell = row1.createCell(3);
			cell.setCellValue("进入时间");
			cell = row1.createCell(4);
			cell.setCellValue("离开时间");
			cell = row1.createCell(5);
			cell.setCellValue("当前状态");
			cell = row1.createCell(6);
			for (int j = 0; j < list.size(); j++) {
				row1 = sheet.createRow(i++);// 创建数据行
				cell.setCellType(CellType.STRING);// 设置单元格类型为字符文本
				cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置单元格默认样式
				cell = row1.createCell(0);
				cell.setCellValue(list.get(j).getInt("consumelogs_id"));
				cell = row1.createCell(1);
				cell.setCellValue(list.get(j).getStr("consumelogs_cardNo"));
				cell = row1.createCell(2);
				cell.setCellValue(list.get(j).getInt("readRoomId"));
				cell = row1.createCell(3);
				cell.setCellValue(list.get(j).getStr("consumelogs_inTime"));
				cell = row1.createCell(4);
				cell.setCellValue(list.get(j).getStr("consumelogs_outTime"));
				cell = row1.createCell(5);
				cell.setCellValue(list.get(j).getInt("consumelogs_status"));
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
	 * http://localhost:8080/ResourceStatistics/con/getHighChartsData?years=
	 * 2018&month=10&day=8
	 */
	public void getHighChartsData() {
		int year = now.get(Calendar.YEAR);// 获取今天的年
		int months = (now.get(Calendar.MONTH) + 1);// 获取今天的月

		String monthstr = "";
		// 处理月为单数时
		if (months < 10) {
			monthstr += "0" + months;
		} else {
			monthstr += months;
		}

		// 定义查询需要使用的date日期格式
		System.out.println("今天的年为:" + year + ",今天的月为:" + months);
		String data = year + "-" + monthstr;
		System.out.println(data);

		String years = getPara("years");
		String month = getPara("month");
		String dateString = null;
		if (years != null && years != "" && month != null && month != "") {
			dateString = years + "-" + month;
		}

		System.out.println("前台获得的,年:" + years + "月:" + month);
		String sql = "FROM consumelogs c INNER JOIN students s ON c.consumelogs_cardNo=s.students_cardNo WHERE 1=1 ";
		if (dateString != null && dateString != "") {
			sql += " and c.consumelogs_inTime like '%" + dateString + "%'";
		} else {
			sql += " and c.consumelogs_inTime like '%" + data + "%'";
		}
		System.out.println("sql========>" + sql);
		List<Consumelogs> list = consumelogsService.getHighCharts(sql);
		System.out.println(list);
		renderJson(list);
	}

}
