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

import org.apache.log4j.Logger;
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

import com.cyj.entity.Students;
import com.cyj.service.StudentsService;
import com.cyj.service.impl.StudentsServiceImpl;
import com.cyj.util.MyPoiHelp;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.upload.UploadFile;

public class StudentsController extends Controller {

	private final Logger logger = Logger.getLogger(StudentsController.class);

	private static StudentsService studentsService = new StudentsServiceImpl();

	/**
	 * 接受对象 ,不使用参数前缀
	 * http://localhost:8080/ResourceStatistics/stu/addStudents?students_id=1&
	 * students_name=佳佳&students_remark=是个大帅哥!!!
	 */
	public void addStudents() {
		// 所有参数不需要前缀,设置参数前缀为空字符串
		Students stu = getModel(Students.class, "");
		System.out.println("SectionsContorller stu ===> " + stu);
		Boolean boo = studentsService.addOneStudents(stu);
		logger.info("成功新增一个实体");
		renderText("操作结果=>" + boo + " -- " + stu);
	}

	/**
	 * http://localhost:8080/ResourceStatistics/stu/json 响应JSON数据
	 */
	public void json() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", 369);
		map.put("rows", "所有数据行");
		renderJson(map);
	}

	/**
	 * http://localhost:8080/ResourceStatistics/stu/toJsp 响应JSP页面
	 */
	public void toJsp() {
		Integer age = getParaToInt("age");
		setAttr("msg", "我被传到页面" + age);
		renderJsp("index.jsp");
	}

	/**
	 * http://localhost:8080/ResourceStatistics/stu/file 上传下载文件
	 */
	public void file() {
		File file = getFile("myfile").getFile();
		renderFile(file);
	}

	/**
	 * 接受对象 ,使用参数前缀 http://localhost:8080/ResourceStatistics/stu/updateStudents?
	 * student_name=佳佳&student_id=1
	 */
	public void updateStudents() {
		// 页面的modelName正好是Student类名的首字母小写,默认行为
		Students stu = getModel(Students.class, "");
		System.out.println("StudentController stu1 => " + stu);
		Boolean boo = studentsService.updateStudents(stu);
		renderJson(boo);
	}

	/**
	 * 接收要删除的学生id
	 * http://localhost:8080/ResourceStatistics/stu/deleById?student_id=1
	 */
	public void deleById() {
		int id = Integer.parseInt(getPara("id"));
		System.out.println("studentsController.id => " + id);
		Boolean boo = studentsService.deleteStudentsById(id);
		renderJson(boo);
	}

	/**
	 * 查询所有信息 http://localhost:8080/ResourceStatistics/stu/getAll
	 */
	public void getAll() {
		int page = Integer.parseInt(getPara("page"));
		int rows = Integer.parseInt(getPara("rows"));
		String studentNames = getPara("studentNames");
		String sexs = getPara("sexs");
		String sql = "from students where 1=1";
		System.out.println("studentNames =====>" + studentNames);
		if (studentNames != null && studentNames != "") {
			sql += " and students_name like '%" + studentNames + "%'";
		}
		if (sexs != null && sexs != "") {
			sql += " and students_sex ='" + sexs + "'";
		}
		System.out.println("sql========>" + sql);
		renderJson(studentsService.getList(page, rows, sql));
	}

	/**
	 * 查询所有信息 http://localhost:8080/ResourceStatistics/stu/getAlls?id=1,2
	 */
	public void getAlls() {
		String id = getPara("id");
		Map<String, Object> map = new HashMap<String, Object>();
		List<Students> stuList = studentsService.getByStringId(id);
		map.put("total", stuList.size());
		map.put("rows", stuList);
		renderJson(map);
	}

	/**
	 * 按照id查询学生表
	 * http://localhost:8080/ResourceStatistics/stu/getAllStudents?students_id=1
	 */
	public void getByIdStudents() {
		int id = Integer.parseInt(getPara("students_id"));
		System.out.println("StudentsController.students_id => " + id);
		Students teaList = studentsService.getById(id);
		renderJson(teaList);
	}

	/**
	 * Excel导入数据 http://localhost:8080/ResourceStatistics/stu/upload 单文件上传
	 * 
	 * @param myfile
	 * @return
	 * @throws FileNotFoundException
	 */
	@SuppressWarnings("deprecation")
	public void upload() {
		UploadFile uploadFile = getFile("myfile", "/file", 200 * 1024 * 1024,
				"UTF-8");
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
				System.out.println("数据共有几行 ======>"
						+ (sheet.getLastRowNum() + 1));
				for (int i = 0; i < (sheet.getLastRowNum() + 1); i++) {// 第二行开始取值，第一行为标题行
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
		System.out.println("集合长度 ======>" + list);
		Boolean boo = null;
		for (int i = 1; i < list.size(); i++) {
			Students stu = new Students()
					.set("students_cardNo", list.get(i).get(0))
					.set("students_name", list.get(i).get(1))
					.set("students_sex", list.get(i).get(2))
					.set("membershipId", ids)
					.set("students_status", list.get(i).get(4))
					.set("students_remark", list.get(i).get(5))
					.set("students_stuNo", list.get(i).get(6));
			System.out.println("Students ======>" + stu);
			boo = stu.save();
			System.out.println("操作结果 ======>" + boo);
		}

		if (boo) {
			renderJson(true);
		} else {
			renderJson(false);
		}

	}

	/**
	 * 下载文件 http://localhost:8080/ResourceStatistics/stu/downloadFile?ids=1,2
	 * 
	 * @throws IOException
	 */
	public void downloadFile() throws IOException {// 文件默认下载根路径下的/download目录下
		String mypath = PathKit.getWebRootPath()
				+ "/download/file/导出的学生数据.xlsx";// 相对当前项目的路径
		String id = getPara("ids");
		File file = new File(mypath);// 文件对象
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream os = new FileOutputStream(file);
		Workbook workBook = null;
		int count = 0;
		List<Students> list = studentsService.getByStringId(id);// 要导出的数据集合
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
		Sheet sheet = workBook.createSheet("studentData");
		// 设置表格默认列宽度为20个字节
		sheet.setDefaultColumnWidth(10);
		// 设置第3列的列宽为50个字符宽度
		sheet.setColumnWidth(6, 50 * 256);
		try {
			int i = 0;
			Row row1 = sheet.createRow(i++);// 创建表头行
			row1.setHeightInPoints((short) 18);// 设置表头行高度
			Cell cell = row1.createCell(0);
			cell.setCellType(CellType.STRING);// 设置单元格类型为文本
			cell.setCellStyle(MyPoiHelp.defaultHeaderCellStyle(workBook));// 设置表头默认样式
			cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置表头默认样式
			cell.setCellValue("学生编号");
			cell = row1.createCell(1);
			cell.setCellValue("卡号");
			cell = row1.createCell(2);
			cell.setCellValue("名字");
			cell = row1.createCell(3);
			cell.setCellValue("性别");
			cell = row1.createCell(4);
			cell.setCellValue("身份编号");
			cell = row1.createCell(5);
			cell.setCellValue("当前状态");
			cell = row1.createCell(6);
			cell.setCellValue("备注");
			cell = row1.createCell(7);
			cell.setCellValue("学号");
			cell = row1.createCell(8);
			for (int j = 0; j < list.size(); j++) {
				row1 = sheet.createRow(i++);// 创建数据行
				cell.setCellType(CellType.STRING);// 设置单元格类型为字符文本
				cell.setCellStyle(MyPoiHelp.defaultCellStyle(workBook));// 设置单元格默认样式
				cell = row1.createCell(0);
				cell.setCellValue(list.get(j).getInt("students_id"));
				cell = row1.createCell(1);
				cell.setCellValue(list.get(j).getStr("students_cardNo"));
				cell = row1.createCell(2);
				cell.setCellValue(list.get(j).getStr("students_name"));
				cell = row1.createCell(3);
				cell.setCellValue(list.get(j).getStr("students_sex"));
				cell = row1.createCell(4);
				cell.setCellValue(list.get(j).getInt("membershipId"));
				cell = row1.createCell(5);
				cell.setCellValue(list.get(j).getInt("students_status"));
				cell = row1.createCell(6);
				cell.setCellValue(list.get(j).getStr("students_remark"));
				cell = row1.createCell(7);
				cell.setCellValue(list.get(j).getStr("students_stuNo"));
				cell = row1.createCell(8);
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
