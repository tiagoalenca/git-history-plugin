package br.uece.ees.githistoryplugin.helper;

public class TextHelper {
		
	public String capitalizationSplit(String field) {
		StringBuilder sb = new StringBuilder();
		char[] array = field.toCharArray();
		
		sb.append(array[0]);
		for(int i=1; i<array.length; ++i){
			char c = array[i];
			if(c >= 'A' && c <= 'Z') sb.append(' ');
			sb.append(c);
		}
		
		return sb.toString();
	}
}
