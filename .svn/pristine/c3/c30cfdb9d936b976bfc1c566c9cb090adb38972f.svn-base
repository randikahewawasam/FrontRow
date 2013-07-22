package com.frontrow.language;


import java.util.Hashtable;
import java.io.InputStream;


public class Language {
	private static Hashtable hashtable;
	private static String LANGUAGE_PROPERTIES = "";
	private static final String LANGUAGE_EN = "/resources/en.properties";
	//    private static final String LANGUAGE_EN = "../../../../resources/en.properties";
	private static final String LANGUAGE_AR = "/resources/ar.properties";
	private static final String LANGUAGE_FR = "/resources/fr.properties";
	private static final String LANGUAGE_RU = "/resources/ru.properties";
	private static final String DELIMITER_1 = "=";
	private static final String DELIMITER_2 = "|";
	private static final Hashtable languages;

	static {
		languages = new Hashtable();
		/*languages.put(Settings._EN, "English");
        languages.put(Settings._AR, Language.getNativeString("\\u0639\\u0631\\u0628\\u064a"));
        languages.put(Settings._FR, "Français");
        languages.put(Settings._RU, "Russian");*/
	}

	public static Hashtable getSupportedLanguages() {
		return languages;
	}

	/**
	 * This method loads the language property file and read its values to a hash table.
	 * This method should be called only one during application load
	 *
	 * @param parent the object of calling class
	 */
	public static synchronized void loadLanguageResource(Object parent) {

	}

	/**
	 * Returns the string loaded from property file corresponds to a given string id.
	 *
	 * @param sID Id of the string which is required to load from property file.
	 * @return Required string corresponds to the given string id.
	 */
	public static String getString(String sID) {

		return "";
	}

	public static String getNativeString(String sUnicode) {
		int i = 0;
		int buffindex = 0;
		char[] buf = new char[sUnicode.length()];
		int iLen = 0;
		char ch;
		char next;
		iLen = sUnicode.length();

		while (i < iLen) {
			ch = getNext(sUnicode, i++);
			if (ch == '\\') {
				if ((next = getNext(sUnicode, i++)) == 'u') {
					if ((iLen - i) >= 4) {
						buf[buffindex++] = processUnicode(sUnicode.substring(i, i + 4));
						i += 4;
					} else {
						buf[buffindex++] = '\\';
						buf[buffindex++] = 'u';
						while (i < iLen) buf[buffindex++] = sUnicode.charAt(i++);
						i = iLen;
					}
				} else if (next == -1) {
					return (new String(buf, 0, buffindex));
				} else {
					buf[buffindex++] = '\\';
					i--;
				}
			} else {
				buf[buffindex++] = ch;
			}
		}
		return (new String(buf, 0, buffindex));
	}

	/**
	 * Returns the next char from the string
	 */
	private static char getNext(String sUnicode, int i) {
		if (i < sUnicode.length())
			return sUnicode.charAt(i);
		else
			return (char) -1;
	}

	public static String getLanguageSpecificString(String sValue) {
		String eng = "";
		String arb = "";

		return "";
	}

	/**
	 * Converts the 4 digit code to the native char
	 */
	private static char processUnicode(String sUnicode) {
		char ch;
		int d = 0;
		loop:
			for (int i = 0; i < 4; i++) {
				ch = sUnicode.charAt(i);
				switch (ch) {
				case '0':
				case '1':
				case '2':
				case '3':
				case '4':
				case '5':
				case '6':
				case '7':
				case '8':
				case '9':
					d = (d << 4) + ch - '0';
					break;
				case 'a':
				case 'b':
				case 'c':
				case 'd':
				case 'e':
				case 'f':
					d = (d << 4) + 10 + ch - 'a';
					break;
				case 'A':
				case 'B':
				case 'C':
				case 'D':
				case 'E':
				case 'F':
					d = (d << 4) + 10 + ch - 'A';
					break;
				default:
					break loop;
				}
			}
		return (char) d;
	}

	public static String getUnicodeString(String sNative) {
		try {
			StringBuffer sUnicode = new StringBuffer("");
			char[] acNative = sNative.toCharArray();

			for (int i = 0; i < acNative.length; i++) {
				sUnicode.append("\\u");
				sUnicode.append(charToHex(acNative[i]));
			}
			return sUnicode.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public static String getCompressedUnicodeString(String sNative) {
		try {
			String prev = "";
			StringBuffer sUnicode = new StringBuffer("");
			char[] acNative = sNative.toCharArray();

			for (int i = 0; i < acNative.length; i++) {
				String str = charToHex(acNative[i]);
				String next = str.substring(0, 2);
				if (next.equals(prev)) {
					sUnicode.append(str.substring(2));
				} else {
					sUnicode.append("\\u");
					sUnicode.append(str);
					prev = next;
				}
			}
			return sUnicode.toString();
		} catch (Exception ex) {
			return "";
		}
	}

	public static String charToHex(char c) {
		// Returns hex String representation of char c
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return byteToHex(hi) + byteToHex(lo);
	}

	static public String byteToHex(byte b) {
		// Returns hex String representation of byte b
		char hexDigit[] =
			{
				'0', '1', '2', '3', '4', '5', '6', '7',
				'8', '9', 'a', 'b', 'c', 'd', 'e', 'f'
			};
		char[] array = {hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f]};
		return new String(array);
	}
}
