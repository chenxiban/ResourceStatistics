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

import com.cyj.entity.Memberships;
import com.cyj.service.MemberShipsService;
import com.cyj.service.impl.MemberShipsServiceImpl;
import com.cyj.util.MyPoiHelp;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class MemberShipsController extends Controller {

	private static MemberShipsService memberShipsService = new MemberShipsServiceImpl();

	/**
	 * 接受对象 ,不使用参数前缀
	 * http://localhost:8080/ResourceStatistics/mem/addMysql?memberShips_name
	 * =Tom&age= 15&birthday=2017-09-22 09:08:56
	 */
	public void addMysql() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		Memberships stu = getModel(Memberships.class, "");
		System.out.println("MemberShipsController stu => " + stu);
		Boolean boo = memberShipsService.addOneMemberShips(stu);
		renderText("操作结果=>" + boo + " -- " + stu);
	}

	/**
	 * 接受对象 ,不使用参数前缀
	 * http://localhost:8080/ResourceStatistics/mem/updateMysql?memberships_id
	 * =4&memberships_department=北大傻鸟
	 */
	public void updateMysql() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		Memberships stu = getModel(Memberships.class, "");
		System.out.println("MemberShipsController stu => " + stu);
		Boolean boo = memberShipsService.updateMemberShips(stu);
		renderText("操作结果=>" + boo + " -- " + stu);
	}

	/**
	 * 根据id查询MemberShips表
	 * http://localhost:8080/ResourceStatistics/mem/getMemberShipsById
	 * ?memberships_id=1
	 */
	public void getMemberShipsById() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		int idValue = Integer.parseInt(getPara("memberships_id"));
		System.out.println("MemberShipsController idValue ===> " + idValue);
		Memberships sec = memberShipsService.getById(idValue);// 根据id查询
		renderText("操作结果=> -- " + sec);
	}

	/**
	 * 根据多个id查询MemberShips表
	 * http://localhost:8080/ResourceStatistics/mem/getMemberShipsByStringId
	 * ?memberships_id=1,2
	 */
	public void getMemberShipsByStringId() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		String idValue = getPara("memberships_id");
		System.out.println("MemberShipsController idValue ===> " + idValue);
		List<Memberships> sec = memberShipsService.getByStringId(idValue);
		renderText("操作结果=> -- " + sec);
	}

	/**
	 * 查询Sections表 http://localhost:8080/ResourceStatistics/mem/getMemberShips
	 */
	public void getMemberShips() {
		int page = Integer.parseInt(getPara("page"));
		int rows = Integer.parseInt(getPara("rows"));
		String memberships_department = getPara("memberships_department");// 院系
		String memberships_specialty = getPara("memberships_specialty");// 专业
		String memberships_degree = getPara("memberships_degree");// 学历
		String sql = "from MemberShips where 1=1";
		if (memberships_department != null && memberships_department != "") {
			sql += " and memberships_department like '%"
					+ memberships_department + "%'";
		}
		if (memberships_specialty != null && memberships_specialty != "") {
			sql += " and memberships_specialty like '%" + memberships_specialty
					+ "%'";
		}
		if (memberships_degree != null && memberships_degree != "") {
			sql += " and memberships_degree ='" + memberships_degree + "'";
		}
		System.out.println("sql========>" + sql);
		renderJson(memberShipsService.getList(page, rows, sql));
	}
	
	/**
	 * Excel导入数据 http://localhost:8080/ResourceStatistics/mem/upload 单文件上传
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
			Memberships mem = new Memberships()
			.set("memberships_department", list.get(i).get(0))
			.set("memberships_specialty", list.get(i).get(1))
			.set("memberships_degree", list.get(i).get(2));
			System.out.println("Memberships ======>"+mem);
			boo = mem.save();
			System.out.println("操作结果 ======>" + boo);
		}
		if (boo) {
			renderJson(true);
		} else {
			renderJson(false);
		}

	}

	/**
	 * 下载文件
	 * http://localhost:8080/ResourceStatistics/mem/downloadFile?ids=1,3,4,5,6,7
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
		List<Memberships> list = memberShipsService.getByStringId(id);// 要导出的数据集合
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
		Sheet sheet = workBook.createSheet("memberShipsData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(10);
		// 设置第3列的列宽为50个字符宽度
		sheet.setColumnWidth(1, 20 * 256);
		// 设置第3列的列宽为50个字符宽度
		sheet.setColumnWidth(2, 20 * 256);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("院系编号");
			cell = row1.createCell(1);
			cell.setCellValue("院系名称");
			cell = row1.createCell(2);
			cell.setCellValue("专业");
			cell = row1.createCell(3);
			cell.setCellValue("学历");
			cell = row1.createCell(4);
			for (int j = 0; j < list.size(); j++) {
				row1 = sheet.createRow(i++);// 创建数据行
				cell = row1.createCell(0);
				cell.setCellValue(list.get(j).getInt("memberships_id"));
				cell = row1.createCell(1);
				cell.setCellValue(list.get(j).getStr("memberships_department"));
				cell = row1.createCell(2);
				cell.setCellValue(list.get(j).getStr("memberships_specialty"));
				cell = row1.createCell(3);
				cell.setCellValue(list.get(j).getStr("memberships_degree"));
				cell = row1.createCell(4);
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
	 * 下载文件 http://localhost:8080/ResourceStatistics/mem/downloadMyStuFile?ids=1,2
	 * 
	 * @throws IOException
	 */
	public void downloadMyStuFile() throws IOException{// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath() + "/download/file/需要填写的数据.xlsx";// 相对当前项目的路径
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
		Sheet sheet = workBook.createSheet("studentData");
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
			cell.setCellValue("卡号(字符串类型默认长度20)");
			cell = row1.createCell(1);
			cell.setCellValue("名字(字符串类型默认长度20)");
			cell = row1.createCell(2);
			cell.setCellValue("性别(枚举类型只允许:男,女)");
			cell = row1.createCell(3);
			cell.setCellValue("备注(字符串类型默认长度2000)");
			cell = row1.createCell(4);
			cell.setCellValue("学号(字符串类型默认长度20)");
			cell = row1.createCell(5);
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
	 * 下载文件 http://localhost:8080/ResourceStatistics/mem/downloadMyMemFile?ids=1,2
	 * 
	 * @throws IOException
	 */
	public void downloadMyMemFile() throws IOException{// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath() + "/download/file/需要填写的数据.xlsx";// 相对当前项目的路径
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
		Sheet sheet = workBook.createSheet("MemberShipsData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(30);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("院系(字符串类型默认长度50)");
			cell = row1.createCell(1);
			cell.setCellValue("专业(字符串类型默认长度50)");
			cell = row1.createCell(2);
			cell.setCellValue("学历(字符串类型默认长度20)");
			cell = row1.createCell(3);
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
