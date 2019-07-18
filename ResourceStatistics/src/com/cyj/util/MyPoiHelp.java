package com.cyj.util;

import java.util.List;

import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.usermodel.Workbook;

public class MyPoiHelp {
	/**
	 * 判断集合为空
	 * 
	 * @param list
	 * @return
	 */
	public static Boolean isEmpty(List<?> list) {
		if (list == null || list.isEmpty()) {
			System.err.println("需要转换的集合不能为空！！！");
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 单元格默认样式 文本剧中显示
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle defaultCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();// 创建一个单元格样式
		// 设置单元格填充样式
		style.setAlignment(HorizontalAlignment.CENTER);// 水平剧中显示
		style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直剧中显示
		return style;
	}

	/**
	 * 默认表头单元格样式
	 * 
	 * @param workbook
	 * @return
	 */
	public static CellStyle defaultHeaderCellStyle(Workbook workbook) {
		CellStyle style = workbook.createCellStyle();// 创建一个单元格样式
		// 设置单元格填充样式
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);// 可以填充单元格样式
		style.setFillForegroundColor(HSSFColor.LIGHT_TURQUOISE.index);// 填充背景色
		style.setAlignment(HorizontalAlignment.CENTER);// 水平剧中显示
		style.setVerticalAlignment(VerticalAlignment.CENTER);// 垂直剧中显示
		style.setBorderBottom(BorderStyle.SLANTED_DASH_DOT);// 双实线下边框
		// 设置单元格字体样式
		Font font = workbook.createFont();
		font.setFontHeightInPoints((short) 10);// 字体大小
		font.setFontName("华文中宋");// 字体
		font.setBold(true);// 粗体
		font.setColor(HSSFColor.BLACK.index);// 字体颜色
		style.setFont(font);// 设置单元格字体样式
		return style;
	}
}
