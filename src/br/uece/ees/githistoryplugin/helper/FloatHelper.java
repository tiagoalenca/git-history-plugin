package br.uece.ees.githistoryplugin.helper;

import java.text.NumberFormat;

public class FloatHelper {

	private NumberFormat defaultFormat = NumberFormat.getPercentInstance();
	
	public String percentFormat(Float percent){
		defaultFormat.setMinimumFractionDigits(2);
		return defaultFormat.format(percent);
	}
}
