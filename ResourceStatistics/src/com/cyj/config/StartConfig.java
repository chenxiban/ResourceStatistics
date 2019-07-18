package com.cyj.config;

import com.cyj.controller.ComputersController;
import com.cyj.controller.ConsumelogsController;
import com.cyj.controller.MemberShipsController;
import com.cyj.controller.ReadroomsController;
import com.cyj.controller.SectionsContorller;
import com.cyj.controller.StatisticsController;
import com.cyj.controller.StudentsController;
import com.cyj.controller.TeachersController;
import com.cyj.entity.Computers;
import com.cyj.entity.Consumelogs;
import com.cyj.entity.Memberships;
import com.cyj.entity.Readrooms;
import com.cyj.entity.Sections;
import com.cyj.entity.Statistics;
import com.cyj.entity.Students;
import com.cyj.entity.Teachers;
import com.cyj.interceptor.MyInterceptor;
import com.cyj.test.MyConsumelogsTest;
import com.cyj.test.MyStatisticsTest;
import com.jfinal.config.Constants;
import com.jfinal.config.Handlers;
import com.jfinal.config.Interceptors;
import com.jfinal.config.JFinalConfig;
import com.jfinal.config.Plugins;
import com.jfinal.config.Routes;
import com.jfinal.kit.PropKit;
import com.jfinal.plugin.activerecord.ActiveRecordPlugin;
import com.jfinal.plugin.activerecord.tx.Tx;
import com.jfinal.plugin.cron4j.Cron4jPlugin;
import com.jfinal.plugin.druid.DruidPlugin;
import com.jfinal.render.ViewType;
import com.jfinal.template.Engine;

/**
 * 
 * JFinal配置
 * 
 * @author 小佳佳 2018.09.27 17.00
 * 
 */
public class StartConfig extends JFinalConfig {
	/**
	 * 配置插件 数据库连接池等
	 */
	@Override
	public void configPlugin(Plugins me) {
		// 加载本地属性文件------------------------
		PropKit.use("db.properties");
		// ----------2--------配置数据源 其一 配置C3p0数据库连接池插件 (数据源使用一个即可)
		// ------------------------
		// C3p0Plugin plugin = PluginTools.getC3p0Plugin(this);
		// C3p0Plugin c3p0Plugin = new
		// C3p0Plugin(getProperty("jdbcUrl"),getProperty("user"),
		// getProperty("password").trim());
		// me.add(plugin);//配置C3p0数据库源插件
		// ----------2--------配置数据源 其二 配置阿里数据库连接池插件 (数据源使用一个即可)
		// ------------------------
		DruidPlugin druid = new DruidPlugin(PropKit.get("jdbc.url").trim(),
				PropKit.get("jdbc.username").trim(), PropKit.get(
						"jdbc.password").trim());
		me.add(druid);// 配置阿里巴巴数据库源插件
		// 配置ActiveRecord插件
		ActiveRecordPlugin activeRecord = new ActiveRecordPlugin(druid);
		activeRecord.setShowSql(true);// SQL语句log输出

		me.add(activeRecord);// 配置ActiveRecord支持插件,JFinal用来操作数据库的核心插件

		activeRecord.addMapping("students", "students_id", Students.class);
		activeRecord.addMapping("teachers", "teachers_id", Teachers.class);
		activeRecord
				.addMapping("statistics", "statistics_id", Statistics.class);
		activeRecord.addMapping("sections", "sections_id", Sections.class);
		activeRecord.addMapping("memberships", "memberships_id",
				Memberships.class);
		activeRecord.addMapping("consumelogs", "consumelogs_id",
				Consumelogs.class);
		activeRecord.addMapping("computers", "computers_id", Computers.class);
		activeRecord.addMapping("readrooms", "readrooms_id", Readrooms.class);
		Cron4jPlugin cp = new Cron4jPlugin();// 可以用task 集群调度处理
		cp.addTask("*/1 * * * *", new MyConsumelogsTest());// 每分钟调用一次
		cp.addTask("21 16 * * *", new MyStatisticsTest());// 每天23点调用一次
		me.add(cp);
	}

	/**
	 * C3P0数据源 C3P0数据库连接池插件
	 * 
	 * @param config
	 * @return
	 */
	/*
	 * public static C3p0Plugin getC3p0Plugin(JFinalConfig config) { C3p0Plugin
	 * c3p0Plugin = new C3p0Plugin(config.getProperty("jdbcUrl"),
	 * config.getProperty("user"), config.getProperty("password") .trim());
	 * return c3p0Plugin; }
	 */

	/**
	 * 配置项目设置
	 */
	@Override
	public void configConstant(Constants me) {
		me.setDevMode(true);// 开发模式.在开发模式下 ，JFinal会输出请求信息
		me.setViewType(ViewType.JSP);// 默认视图是jsp //Final支持 JSP
										// 、FreeMarkerarker、Velocity三种常用视图 。
		me.setBaseUploadPath("/upload");// 默认文件上传缓存目录/upload,文件会默认上传到
										// 当前项目下的/upload 目录中
		me.setBaseDownloadPath("/download");// 配置文件下载路径
		// 默认文件下载缓存路径/download,这个源码中貌似没有使用
	}

	/**
	 * 配置路由
	 */
	@Override
	public void configRoute(Routes me) {
		// "/stu"为请求该控制器的url前缀
		me.add("/sec", SectionsContorller.class);
		me.add("/mem", MemberShipsController.class);
		me.add("/com", ComputersController.class);
		me.add("/con", ConsumelogsController.class);
		me.add("/rea", ReadroomsController.class);
		me.add("/sta", StatisticsController.class);
		me.add("/stu", StudentsController.class);
		me.add("/tea", TeachersController.class);
		// me.add(new FrontRoutes());//前端路由
		// me.add(new AdminRoutes());//后端路由
	}

	@Override
	public void configEngine(Engine me) {

	}

	@Override
	/**
	 * 全局拦截器 ，全局拦截器将拦截所有action请求
	 */
	public void configInterceptor(Interceptors me) {
		me.add(new MyInterceptor());// 服务器异常友好提示拦截器
		me.add(new Tx());// 声明式事务拦截器,被该拦截器拦截的所有方法开启事务.最简单的事务配置方式
		/*
		 * me.add(new TxByActionKeyRegex("/stu/testTran"));// 正则Controller事务拦截器
		 * me.add(new TxByActionKeyRegex("/stu/testTran*"));// 正则Controller事务拦截器
		 * 
		 * me.add(new TxByMethodRegex("*update*"));// 正则方法事务拦截器,被该正则表达式匹配的方法开启事务
		 * me.add(new TxByMethodRegex("(*add*|*save*|*delete*|*update*)")); //
		 * 正则方法事务拦截器,被该正则表达式匹配的方法开启事务
		 */}

	@Override
	public void configHandler(Handlers me) {

	}

	/**
	 * 会在系统关闭前回调afterJFinalStart方法
	 * 
	 * 项目启动成功后执行.类似于Servlet中的项目环境监听器 可以用来实现后台自动执行线程任务的调度
	 */
	@Override
	public void afterJFinalStart() {
		System.out.println("JFinal 小佳佳的项目ResourceStatistics启动成功 *** ***");
		super.afterJFinalStart();
	}

	/**
	 * 项目关闭前执行.类似于Servlet中的项目环境监听器 可以用来实现关闭项目前的善后工作
	 * 
	 * 会在系统关闭前回调beforeJFinalStop方法 这两个方法可以很便地在项目启动后与关闭前让开发者有机会进行额外操作,
	 * 如在系统启动后创建调度线程或在系统关闭前写回缓存
	 * 
	 */
	@Override
	public void beforeJFinalStop() {
		System.out.println("JFinal 小佳佳的项目ResourceStatistics即将关闭 ... ...");
		super.beforeJFinalStop();
	}

}
