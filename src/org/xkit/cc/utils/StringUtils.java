package org.xkit.cc.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class StringUtils {
	private static final String DATE_FORMATTER = "yyyy-mm-dd";

	private static final String DATE_FORMATTER_ZH_CN = "yyyy年mm月dd日";

	private static DateFormat formatter = new SimpleDateFormat(DATE_FORMATTER);

	private static DateFormat formatterZhCN = new SimpleDateFormat(
			DATE_FORMATTER_ZH_CN);

	private static Calendar calendar = Calendar.getInstance();

	public static boolean notBlank(String s) {
		return (null != s && !"".equalsIgnoreCase(s.trim()));
	}

	public static java.util.Date stringToDate(String s) {
		try {
			return (formatter.parse(s));
		} catch (ParseException e) {
		}

		try {
			return (formatterZhCN.parse(s));
		} catch (ParseException e) {
			return new java.util.Date();
		}
	}

	public static int stringToYear(String s) {
		calendar.setTime(stringToDate(s));
		return calendar.get(Calendar.YEAR);
	}

}
