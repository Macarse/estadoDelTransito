package com.appspot.estadodeltransito.util;

public class UnicodeUtils {

	/**
	 * Converts unicode text to strings with unicode characters.
	 * For example, the text "\u00dc", read by Java as "\\u00dc",
	 * is converted to "Ü"
	 * @param s String to be converted
	 * @return Converted string
	 */
	public static String translateToUnicodeCharacters(String s) {
		int i=0,len=s.length(); char c; StringBuffer sb = new StringBuffer(len);
		while (i<len) {
			c = s.charAt(i++);
			if (c=='\\') {
				if (i<len) {
					c = s.charAt(i++);
					if (c=='u') {
						c = (char) Integer.parseInt(s.substring(i,i+4),16);
						i += 4;
					} // add other cases here as desired...
				}} // fall through: \ escapes itself, quotes any character but u
			sb.append(c);
		}
		return sb.toString();
	}
}
