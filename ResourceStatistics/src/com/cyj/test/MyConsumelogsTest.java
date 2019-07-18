package com.cyj.test;

import java.util.List;
import java.util.Random;
import java.util.Date;

import com.cyj.entity.Consumelogs;
import com.cyj.entity.Readrooms;
import com.cyj.entity.Students;
import com.cyj.entity.Teachers;
import com.cyj.service.ConsumelogsService;
import com.cyj.service.ReadroomsService;
import com.cyj.service.StudentsService;
import com.cyj.service.TeachersService;
import com.cyj.service.impl.ConsumelogsServiceImpl;
import com.cyj.service.impl.ReadroomsServiceImpl;
import com.cyj.service.impl.StudentsServiceImpl;
import com.cyj.service.impl.TeachersServiceImpl;
import com.jfinal.ext.kit.DateKit;

/**
 * 
 * 操作刷卡记录表
 * 
 * @author 小佳佳 2018.10.06 17.00
 * 
 */
public class MyConsumelogsTest implements Runnable {

	// 学生业务层
	private static StudentsService studentsService = new StudentsServiceImpl();
	// 老师业务层
	private static TeachersService teachersService = new TeachersServiceImpl();
	// 阅览室业务层
	private static ReadroomsService readroomsService = new ReadroomsServiceImpl();
	// 刷卡记录业务层
	private static ConsumelogsService consumelogsService = new ConsumelogsServiceImpl();
	private int a;

	public void run() {
		while (a < 12) {
			int flag = new Random().nextInt(2) + 1;
			Long status = null;
			try {
				this.task();
				a++;
				Thread.sleep(5000);
				//Thread.sleep(new Random().nextInt(2000));// 防止多线程过于争抢资源
				List<Readrooms> reaList = readroomsService.getAllReadrooms();// 获得阅览室集合
				Random random = new Random();
				int a = random.nextInt(reaList.size());
				reaList.get(a).getReadroomsId();// 随机阅览室id

				if (flag == 1) {
					List<Students> stuList = studentsService.getList();// 获得学生集合
					Random random1 = new Random();
					int b = random1.nextInt(stuList.size());
					stuList.get(b).getStudentsId();// 随机学生id
					stuList.get(b).getStudentsCardno();// 随机学生的卡号
					stuList.get(b).getStudentsStatus();// 随机学生的状态

					// 刷卡记录集合
					List<Consumelogs> conList = consumelogsService
							.getByCardNo(stuList.get(b).getStudentsCardno());
					for (int i = 0; i < conList.size(); i++) {
						if (conList.get(i).getConsumelogsStatus() != 0) {
							status = conList.get(i).getConsumelogsId();// 获得当前在阅览室的卡号id刷卡记录id
						}
					}

					System.out.println("status ======>" + status);

					if (stuList.get(b).getStudentsStatus() != 0) {// 若学生状态不为0则正在阅览室,修改学生状态,添加离开刷卡记录
						Students students = new Students().set("students_id",
								stuList.get(b).getStudentsId()).set(
								"students_status", 0);
						Boolean boo = students.update();
						System.out.println("students操作结果 ======>" + boo);

						// 0标识不在
						Consumelogs consumelogs = new Consumelogs()
								.set("consumelogs_id", status)
								.set("consumelogs_outTime", new Date())
								.set("consumelogs_status", 0);
						Boolean boo1 = consumelogs.update();
						System.out.println("consumelogs操作结果 ======>" + boo1);
					} else {// 若学生状态为0则正常,修改学生状态,添加刷卡记录
						Students students = new Students().set("students_id",
								stuList.get(b).getStudentsId()).set(
								"students_status",
								reaList.get(a).getReadroomsId());
						Boolean boo = students.update();
						System.out.println("students操作结果 ======>" + boo);

						// 1标识在
						Consumelogs consumelogs = new Consumelogs()
								.set("consumelogs_cardNo",
										stuList.get(b).getStudentsCardno())
								.set("readRoomId",
										reaList.get(a).getReadroomsId())
								.set("consumelogs_inTime", new Date())
								.set("consumelogs_status", 1);
						Boolean boo1 = consumelogs.save();
						System.out.println("consumelogs操作结果 ======>" + boo1);
					}
				} else {
					List<Teachers> teaList = teachersService.getList();// 获得老师集合
					Random random2 = new Random();
					int c = random2.nextInt(teaList.size());
					teaList.get(c).getTeachersId();// 随机老师id
					teaList.get(c).getTeachersCardno();// 随机老师的卡号
					teaList.get(c).getTeachersStatus();// 随机老师的状态

					// 刷卡记录集合
					List<Consumelogs> conList = consumelogsService
							.getByCardNo(teaList.get(c).getTeachersCardno());
					for (int i = 0; i < conList.size(); i++) {
						if (conList.get(i).getConsumelogsStatus() != 0) {
							status = conList.get(i).getConsumelogsId();// 获得当前在阅览室的卡号id刷卡记录id
						}
					}

					System.out.println("status ======>" + status);

					if (teaList.get(c).getTeachersStatus() != 0) {// 若老师状态不为0则正在阅览室,修改老师状态,添加离开刷卡记录
						Teachers teachers = new Teachers().set("teachers_id",
								teaList.get(c).getTeachersId()).set(
								"teachers_status", 0);
						Boolean boo = teachers.update();
						System.out.println("teachers操作结果 ======>" + boo);

						// 0标识不在
						Consumelogs consumelogs = new Consumelogs()
								.set("consumelogs_id", status)
								.set("consumelogs_outTime", new Date())
								.set("consumelogs_status", 0);
						Boolean boo1 = consumelogs.update();
						System.out.println("consumelogs操作结果 ======>" + boo1);
					} else {// 若老师状态为0则正常,修改老师状态,添加刷卡记录
						Teachers teachers = new Teachers().set("teachers_id",
								teaList.get(c).getTeachersId()).set(
								"teachers_status",
								reaList.get(a).getReadroomsId());
						Boolean boo = teachers.update();
						System.out.println("teachers操作结果 ======>" + boo);

						// 1标识在
						Consumelogs consumelogs = new Consumelogs()
								.set("consumelogs_cardNo",
										teaList.get(c).getTeachersCardno())
								.set("readRoomId",
										reaList.get(a).getReadroomsId())
								.set("consumelogs_inTime", new Date())
								.set("consumelogs_status", 1);
						Boolean boo1 = consumelogs.save();
						System.out.println("consumelogs操作结果 ======>" + boo1);
					}
				}

			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		a = 0;// 每分钟都会执行
	}

	private void task() {
		System.out.println("jfianl  老师或者学生任务信息调度======>"
				+ DateKit.toStr(new Date(), DateKit.timeStampPattern));
	}

}
