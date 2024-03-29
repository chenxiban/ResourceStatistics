package com.cyj.entity;

import java.text.SimpleDateFormat;
import java.util.List;

import com.cyj.entity.base.BaseConsumelogs;

/**
 * Generated by JFinal.
 */
@SuppressWarnings("serial")
public class Consumelogs extends BaseConsumelogs<Consumelogs> {

	public static final Consumelogs dao = new Consumelogs().dao();

	/**
	 * 格式化Consumelogs类中的日期格式
	 * 
	 * @param list
	 * @return
	 */
	public static List<Consumelogs> format(List<Consumelogs> list) {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for (Consumelogs s : list) {
			s.set("consumelogs_inTime",
					format.format(s.get("consumelogs_inTime")));
			s.set("consumelogs_outTime",
					format.format(s.get("consumelogs_outTime")));
		}
		return list;
	}

	/**
	 * 格式化Consumelogs类中的日期格式
	 */
	public Consumelogs format() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		this.set(
				"consumelogs_inTime",
				this.get("consumelogs_inTime") != null ? format.format(this
						.get("consumelogs_inTime")) : null);
		this.set(
				"consumelogs_outTime",
				this.get("consumelogs_outTime") != null ? format.format(this
						.get("consumelogs_outTime")) : null);
		return this;
	}

}
