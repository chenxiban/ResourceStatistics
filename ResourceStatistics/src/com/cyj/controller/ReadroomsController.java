package com.cyj.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cyj.entity.Readrooms;
import com.cyj.service.ReadroomsService;
import com.cyj.service.impl.ReadroomsServiceImpl;
import com.cyj.util.MyPoiHelp;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class ReadroomsController extends Controller {
	
	private static ReadroomsService readroomsService = new ReadroomsServiceImpl();
	
	/**
	 * 查询所有信息 http://localhost:8080/ResourceStatistics/rea/getAll
	 */
	public void getAll() {
		int page = Integer.parseInt(getPara("page"));
		int rows = Integer.parseInt(getPara("rows"));
		String readroomsNames = getPara("readroomsNames");
		String sql = "FROM readrooms WHERE 1=1";
		System.out.println("readroomsNames =====>" + readroomsNames);
		if (readroomsNames != null && readroomsNames != "") {
			sql += " and readrooms_name like '%" + readroomsNames + "%'";
		}
		System.out.println("sql========>" + sql);
		renderJson(readroomsService.getList(page, rows, sql));
	}
	
	/**
	 * 查询所有信息 http://localhost:8080/ResourceStatistics/rea/getAllReadroomsIds
	 */
	public void getAllReadroomsIds() {
		List<Readrooms> list=readroomsService.getAllReadrooms();
		Random random = new Random();
		int n = random.nextInt(list.size());
		System.out.println("list======>"+list.get(n).getReadroomsId());
		renderJson(list);
	}
	
	/**
	 * Excel导入数据 http://localhost:8080/ResourceStatistics/rea/upload 单文件上传
	 * 
	 * @param myfile
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("deprecation")
	public void upload() {
		UploadFile uploadFile = getFile("myfile","/file",200 * 1024 * 1024, "UTF-8");
		String fileName = uploadFile.getOriginalFileName();// 得到所上传的文件的全名称
		String title = getPara("desc");
		System.out.println("文件描述信息 title ======> " + title);
		System.out.println("表单中文件参数名称 fileName ======> " + fileName);

		File file = uploadFile.getFile();
		InputStream in = null;		
		try {
			in = new FileInputStream(file);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		}
		// 处理导入数据
		List<Map<Integer, String>> list = new ArrayList<Map<Integer, String>>();
		Workbook workBook = null;
		// 判断文件是否以xlsx后缀
		if (fileName.toLowerCase().endsWith("xlsx")) {
			try {
				workBook = new XSSFWorkbook(in);// 构造一个xlsx后缀的EXCEL文件对象,2010
				XSSFSheet sheet = (XSSFSheet) workBook.getSheetAt(0); // 获取到第一个sheet中数据
				System.out.println("数据共有几行 ======>"+(sheet.getLastRowNum()+1));
				for (int i = 0; i < (sheet.getLastRowNum()+1); i++) {// 第二行开始取值，第一行为标题行
					XSSFRow row = sheet.getRow(i);// 获取到第i列的行数据(表格行)
					Map<Integer, String> map = new HashMap<Integer, String>();
					for (int j = 0; j < row.getLastCellNum(); j++) {
						XSSFCell cell = row.getCell(j);// 获取到第j行的数据(单元格)
						cell.setCellType(XSSFCell.CELL_TYPE_STRING);
						map.put(j, cell.getStringCellValue());
					}
					list.add(map);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		System.out.println("集合长度 ======>"+list);
		Boolean boo = null;
		for (int i = 1; i < list.size(); i++) {
			Readrooms rea = new Readrooms()
			.set("readrooms_name", list.get(i).get(0))
			.set("readrooms_remark", list.get(i).get(1));
			boo = rea.save();
			System.out.println("操作结果 ======>" + boo);
		}
		if (boo) {
			renderJson(true);
		} else {
			renderJson(false);
		}

	}
	
	/**
	 * 下载文件 http://localhost:8080/ResourceStatistics/rea/downloadFile?ids=1,2,3,4,5,6
	 * 
	 * @throws IOException
	 */
	public void downloadFile() throws IOException {// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath() + "/download/file/导出的院系数据.xlsx";// 相对当前项目的路径
		String id = getPara("ids");
		File file = new File(mypath);// 文件对象
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		Workbook workBook = null;
		int count = 0;
		List<Readrooms> list = readroomsService.getByStringId(id);// 要导出的数据集合
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
		Sheet sheet = workBook.createSheet("readroomsData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(10);
		// 设置第3列的列宽为50个字符宽度
		sheet.setColumnWidth(2, 60*256);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("阅览室编号");
			cell = row1.createCell(1);
			cell.setCellValue("阅览室名称");
			cell = row1.createCell(2);
			cell.setCellValue("阅览室备注");
			cell = row1.createCell(3);
			for (int j = 0; j < list.size(); j++) {
				row1 = sheet.createRow(i++);// 创建数据行
				cell = row1.createCell(0);
				cell.setCellValue(list.get(j).getStr("readrooms_id"));
				cell = row1.createCell(1);
				cell.setCellValue(list.get(j).getStr("readrooms_name"));
				cell = row1.createCell(2);
				cell.setCellValue(list.get(j).getStr("readrooms_remark"));
				cell = row1.createCell(3);
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
	 * 下载文件模板 http://localhost:8080/ResourceStatistics/rea/downloadFileRea
	 * 
	 * @throws IOException
	 */
	public void downloadFileRea() throws IOException {// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath() + "/download/file/阅览室表需要的数据.xlsx";// 相对当前项目的路径
		File file = new File(mypath);// 文件对象
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		Workbook workBook = null;
		// 创建一个Excel文件
		try {
			workBook = new HSSFWorkbook();// 构造一个xls后缀的EXCEL文件对象,2007
		} catch (Exception e) {
			e.printStackTrace();
			workBook = new XSSFWorkbook();// 构造一个xlsx后缀的EXCEL文件对象,2010
		}
		Sheet sheet = workBook.createSheet("readroomsData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(25);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("阅览室名称(字符串类型默认长度50)");
			cell = row1.createCell(1);
			cell.setCellValue("阅览室备注(字符串类型默认长度2000)");
			cell = row1.createCell(2);
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
	
}
