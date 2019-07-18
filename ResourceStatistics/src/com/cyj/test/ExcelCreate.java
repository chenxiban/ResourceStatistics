package com.cyj.test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.cyj.entity.Students;
import com.cyj.entity.base.BaseSections;
import com.cyj.entity.base.BaseStudents;
import com.jfinal.core.Controller;
import com.jfinal.kit.PathKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.Table;
import com.jfinal.plugin.activerecord.TableMapping;

/**
 * Workbook: EXCEL文件 Sheet: EXCEL文件中的一个sheet表格 Row: sheet表中的一行 Cell:
 * sheet表中的一个单元格
 * 
 */
@SuppressWarnings("unused")
public class ExcelCreate extends Controller {

	// 导出使用
	private String fileName;
	private InputStream inputStream;
	
	/**
	 * 创建EXCEL文件,并把集合数据写入
	 * 
	 * @param list
	 * @throws IOException
	 */
	public void createExcel(List<Students> list) throws IOException {

		int total = list.size();// 所有数据行数
		System.out.println("所有数据的行数=> " + total);
		// 如果需要下载的数据条数大于 65535 行
		// 分多个EXCEL文件,打包压缩.
		// 分多个sheet

		Workbook workbook = null;

		workbook = this.createWorkbook(list);
		String path = PathKit.getWebRootPath() + "/upload/file/导出的学生数据.xlsx";// 相对当前项目的路径
		String excelFileName = "Students.xlsx";// 文件名称
		File excel = new File(path);// 文件对象
		java.io.File file = new java.io.File(path);
		if (!file.exists()) {
			file.createNewFile();
		}
		FileOutputStream out = new FileOutputStream(file);

		workbook.write(out);
		out.flush();
		out.close();

	}

	public Workbook createWorkbook(List<Students> StudentsList) {
		Workbook workbook = new XSSFWorkbook();// 构造一个xlxs后缀的EXCEL文件对象
		// workbook=new XSSFWorkbook();//构造一个xlx后缀的EXCEL文件对象
		Sheet sheet = workbook.createSheet("StudentsData");// 创建一个sheet表,并设置表名称
		// 创建表头字段
		Row row = sheet.createRow(0);// 创建索引为0的Row对象
		String[] header = { "学生编号", "卡号", "名字", "性别", "身份编号", "当前状态", "备注",
		"学号" };
		int head_length = header.length;
		Cell[] cells = new HSSFCell[head_length];
		// 填入表头的值
		for (int i = 0; i < cells.length; i++) {
			cells[i] = row.createCell(i);// 创建一个单元格对象
			cells[i].setCellType(1);// 设置单元格类型为文本
			cells[i].setCellValue(header[i]);
		}

		for (int i = 0; i < StudentsList.size(); i++) {
			row = this.getRtData(sheet, i + 1, StudentsList, head_length);
		}
		return workbook;
	}

	private Row getRtData(Sheet sheet, int i, List<Students> StudentsList,
			int head_length) {
		Row row = sheet.createRow(i);
		// 设计cell的格式
		for (int j = 0; j < head_length; j++) {
			row.createCell(j);// 创建一个单元格对象
			row.getCell(j).setCellType(1);// 设置单元格类型为文本
		}
		Students Students = StudentsList.get(i - 1);// 从下标为0开始

		row.getCell(0).setCellValue(Students.getStudentsId());
		row.getCell(1).setCellValue(Students.getStudentsCardno());
		row.getCell(2).setCellValue(Students.getStudentsName());
		row.getCell(3).setCellValue(Students.getStudentsSex());
		row.getCell(4).setCellValue(Students.getMembershipId());
		row.getCell(5).setCellValue(Students.getStudentsStatus());
		row.getCell(6).setCellValue(Students.getStudentsRemark());
		row.getCell(7).setCellValue(Students.getStudentsStuno());

		return row;
	}

	public static void main(String[] args) throws IOException {
		/*System.out.println("-----");
		String id="1,2";
		List<Students> list = Students.dao
				.find("SELECT students_id,students_cardNo,students_name,students_sex,membershipId,students_status,students_remark,students_stuNo FROM students WHERE students_id IN ("
						+ id + ")");// 要导出的数据集合
		ExcelCreate create = new ExcelCreate();
		try {
			create.createExcel(list);
		} catch (IOException e) {
			e.printStackTrace();
		}*/
		
		
		
		// 获取文件
		/*UploadFile file = getFile("excel");
		String path = file.getSaveDirectory() + file.getFileName();
		// 处理导入数据
		List<Map<Integer,String>> list = new ArrayList<Map<Integer,String>>();
		hwb = new HSSFWorkbook(new FileInputStream(new File(path)));
		HSSFSheet sheet = hwb.getSheetAt(0); // 获取到第一个sheet中数据
		for(int i = 0;i<sheet.getLastRowNum() + 1; i++) {// 第二行开始取值，第一行为标题行
			HSSFRow row = sheet.getRow(i);// 获取到第i列的行数据(表格行)
			Map<Integer, String> map = new HashMap<Integer, String>();
			for(int j=0;j<row.getLastCellNum(); j++) {
				HSSFCell cell = row.getCell(j);// 获取到第j行的数据(单元格)
				cell.setCellType(HSSFCell.CELL_TYPE_STRING);
				map.put(j, cell.getStringCellValue());
				}
			list.add(map);}
			}
		}
		for(Map<Integer,String> map:list) { // 遍历取出的数据，并保存
			Member m = new Member();
			m.put("name", map.get(0));
			m.put("account", map.get(1));
			m.put("pwd", map.get(2));
			m.put("address", map.get(3));
			m.save();
		}*/
		
		/*FileInputStream in = new FileInputStream(new File("G:\\导出的学生数据.xlsx"));		
		Workbook wb = null;	
		// 创建一个Excel文件
		wb = new XSSFWorkbook(in);// 构造一个xlsx后缀的EXCEL文件对象,2010
		in.close();		
		wb.close();		
		// 创建工作表sheet		
		Sheet sheet = wb.getSheetAt(0);		
		// 获取sheet中数据的行数		
		int rows = sheet.getPhysicalNumberOfRows();		
		System.out.println("total rows" + rows);		
		boolean boo = sheet.getRow(1).getCell(0).getBooleanCellValue();		
		String st = sheet.getRow(1).getCell(1).getStringCellValue();		
		double num = sheet.getRow(1).getCell(2).getNumericCellValue();		
		Date date = sheet.getRow(1).getCell(3).getDateCellValue();		
		System.out.println(boo+":String:"+st+":double:"+num+":date:"+date);*/

	}

}
