package com.cyj.start;

import com.jfinal.core.JFinal;

/**
 * 项目启动的主类
 * 
 * @author Mr.chen
 * 
 */
public class StartApp {
	public static void main(String[] args) {
		/**
		 * 热部署方式启动， 8080表示项目访问端口号（可修改）； "/JFinalFirst"项目访问根目录（可修改）；
		 * 3表示热部署扫描时间为3秒，如果项目内容有变化就自动重启服务。
		 */
		JFinal.start("WebRoot", 8080, "/ResourceStatistics", 3);
	}
}
