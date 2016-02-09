package br.uece.ees.githistoryplugin.helper;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class DateHelper {
	
	private final SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	
	public Date createDate(int year, int month, int day) {
		Calendar cal = GregorianCalendar.getInstance();
		cal.set(Calendar.YEAR, year);
	    cal.set(Calendar.MONTH, month);
	    cal.set(Calendar.DATE, day);
	    
	    return cal.getTime();
	}
	
	public String format(Date date) {
		return format.format(date);
	}
	
	@SuppressWarnings("deprecation")
	public Date period(String period) {
		return new Date("01/".concat(period));
	}
}
