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

import com.cyj.entity.Teachers;
import com.cyj.service.TeachersService;
import com.cyj.service.impl.TeachersServiceImpl;
import com.cyj.util.MyPoiHelp;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class TeachersController extends Controller {
	
	public static TeachersService teachersService=new TeachersServiceImpl();
	
	/**
	 * 接受对象 ,不使用参数前缀
	 * http://localhost:8080/ResourceStatistics/tea/addTeachers?teachers_id=1&
	 * teachers_name=佳佳&teachers_remark=是个大帅哥!!!
	 */
	public void addTeachers() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		Teachers tea = getModel(Teachers.class, "");
		System.out.println("TeachersController tea ===> " + tea);
		Boolean boo = teachersService.addOneTeachers(tea);
		renderText("操作结果=>" + boo + " -- " + tea);
	}
	
	/**
	 * 修改老师表
	 * http://localhost:8080/ResourceStatistics/tea/updateTeachers?teachers_id=1&
	 * teachers_name=佳佳&teachers_remark=是个大帅哥!!!
	 */
	public void updateTeachers() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		Teachers tea = getModel(Teachers.class, "");
		System.out.println("TeachersController tea ===> " + tea);
		Boolean boo = teachersService.updateTeachers(tea);
		renderText("操作结果=>" + boo + " -- " + tea);
	}
	
	/**
	 * 删除老师表
	 * http://localhost:8080/ResourceStatistics/tea/delTeachers?teachers_id=1
	 */
	public void delTeachers() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		int id = Integer.parseInt(getPara("teachers_id"));
		System.out.println("TeachersController.teachers_id => " + id);
		Boolean boo = teachersService.deleteTeachersById(id);
		renderText("删除结果=>" + boo);
	}
	
	/**
	 * 查询老师表所有信息
	 * http://localhost:8080/ResourceStatistics/tea/getAllTeachers
	 */
	public void getAllTeachers() {
		int page = Integer.parseInt(getPara("page"));
		int rows = Integer.parseInt(getPara("rows"));
		String teachersNames = getPara("teachersNames");
		String sexs = getPara("sexs");
		String sql = "from teachers where 1=1";
		System.out.println("studentNames =====>" + teachersNames);
		if (teachersNames != null && teachersNames != "") {
			sql += " and teachers_Name like '%" + teachersNames + "%'";
		}
		if (sexs != null && sexs != "") {
			sql += " and teachers_sex ='" + sexs + "'";
		}
		System.out.println("sql========>" + sql);
		renderJson(teachersService.getList(page, rows, sql));
	}
	
	/**
	 * 按照id查询老师表
	 * http://localhost:8080/ResourceStatistics/tea/getAllTeachers?teachers_id=1
	 */
	public void getByIdTeachers() {
		int id = Integer.parseInt(getPara("teachers_id"));
		System.out.println("TeachersController.teachers_id => " + id);
		Teachers teaList = teachersService.getById(id);
		renderText("查询结果=>" + teaList);
	}
	
	/**
	 * Excel导入数据 http://localhost:8080/ResourceStatistics/tea/upload 单文件上传
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
		int ids = Integer.parseInt(getPara("ids"));
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
			Teachers tea = new Teachers()
			.set("teachers_cardNo", list.get(i).get(0))
			.set("teachers_name", list.get(i).get(1))
			.set("teachers_sex", list.get(i).get(2))
			.set("sectionID", ids)
			.set("teachers_status", list.get(i).get(4))
			.set("teachers_remark", list.get(i).get(5));
			System.out.println("Teachers ======>"+tea);
			boo = tea.save();
			System.out.println("操作结果 ======>" + boo);
		}
		if (boo) {
			renderJson(true);
		} else {
			renderJson(false);
		}

	}
	
	/**
	 * 下载文件 http://localhost:8080/ResourceStatistics/tea/downloadFile?ids=1,2,3
	 * 
	 * @throws IOException
	 */
	public void downloadFile() throws IOException {// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath() + "/download/file/导出的老师数据.xlsx";// 相对当前项目的路径
		String id = getPara("ids");
		File file = new File(mypath);// 文件对象
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		Workbook workBook = null;
		int count = 0;
		List<Teachers> list = teachersService.getByStringId(id);// 要导出的数据集合
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
		Sheet sheet = workBook.createSheet("teachersData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(10);
		// 设置第3列的列宽为50个字符宽度
		sheet.setColumnWidth(6, 50*256);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("老师编号");
			cell = row1.createCell(1);
			cell.setCellValue("卡号");
			cell = row1.createCell(2);
			cell.setCellValue("名字");
			cell = row1.createCell(3);
			cell.setCellValue("性别");
			cell = row1.createCell(4);
			cell.setCellValue("科室编号");
			cell = row1.createCell(5);
			cell.setCellValue("当前状态");
			cell = row1.createCell(6);
			cell.setCellValue("备注");
			cell = row1.createCell(7);
			for (int j = 0; j < list.size(); j++) {
				row1 = sheet.createRow(i++);// 创建数据行
				cell.setCellType(CellType.STRING);// 设置单元格类型为字符文本
				cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置单元格默认样式
				cell = row1.createCell(0);
				cell.setCellValue(list.get(j).getInt("teachers_id"));
				cell = row1.createCell(1);
				cell.setCellValue(list.get(j).getStr("teachers_cardNo"));
				cell = row1.createCell(2);
				cell.setCellValue(list.get(j).getStr("teachers_name"));
				cell = row1.createCell(3);
				cell.setCellValue(list.get(j).getStr("teachers_sex"));
				cell = row1.createCell(4);
				cell.setCellValue(list.get(j).getInt("sectionID"));
				cell = row1.createCell(5);
				cell.setCellValue(list.get(j).getInt("teachers_status"));
				cell = row1.createCell(6);
				cell.setCellValue(list.get(j).getStr("teachers_remark"));
				cell = row1.createCell(7);
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
}
