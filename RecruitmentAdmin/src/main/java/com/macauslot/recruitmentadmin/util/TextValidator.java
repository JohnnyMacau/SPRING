package com.macauslot.recruitmentadmin.util;

import org.apache.commons.lang3.StringUtils;

/**
 * 文本验证工具类
 */
public final class TextValidator {

	/**
	 * 私有化构造方法，不希望被创建对象
	 */
	private TextValidator() {
		super();
	}




	/**
	 * 验证英文的正则表达式
	 */
	private static final String REGEX_ENGLISH
		= "^[a-zA-Z ]+$";
	/**
	 * 验证英文中文的正则表达式
	 */
	private static final String REGEX_ENGLISH_CHINESE
		= "^[\\u4e00-\\u9fa5a-zA-Z ]+$";
	/**
	 * 验证电子邮箱的正则表达式
	 */
	private static final String REGEX_EMAIL
		= "^[^@^\\s]+@[^\\.@^\\s]+(\\.[^\\.@^\\s]+)+$";




	/**
	 * 验证中文的正则表达式
	 */
	private static final String REGEX_CHINESE
			= "^[\\u4e00-\\u9fa5]*$";

	//    var re = /[^\u4e00-\u9fa5]/;
	/**
	 * 验证国内身份证的正则表达式
	 */
	private static final String REGEX_ID
			= "(^\\d{15}$)|(^\\d{18}$)|(^\\d{17}(\\d|X|x)$)";
	/**
	 * 验证外国身份证的正则表达式
	 */
	private static final String REGEX_ID_FOREIGN
			= "^[A-Za-z0-9]+$";
//= "[A-Za-z0-9]";


/**
 * //TODO
 * 验证电话长度,  日期过去时间
 */

	/**
	 * 验证变更密码的正则表达式
	 */
	private static final String REGEX_PASSWORD
			= "^(?![^a-zA-Z]+$)(?!\\D+$)[a-zA-Z0-9]{6,}$";


	/**
	 * 验证密码格式
	 * @param password 密码
	 * @return
	 */
	public static boolean checkPassword(String password) {
		if ( StringUtils.isBlank(password)) {
			return true;
		}
		return password.matches(REGEX_PASSWORD);
	}


	/**
	 * 验证英文格式
	 * @param str 英文str
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkEnglish(String str) {
		if ( StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches(REGEX_ENGLISH);
	}



	/**
	 * 验证英文中文格式
	 * @param str 英文中文
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkEnglishChinese(String str) {
		if ( StringUtils.isBlank(str)) {
			return false;
		}
		return str.matches(REGEX_ENGLISH_CHINESE);
	}

	/**
	 * 验证电子邮箱格式
	 * @param email 电子邮箱
	 * @return 返回true时表示符合格式要求，返回false时表示不符合格式要求
	 */
	public static boolean checkEmail(String email) {
		if ( StringUtils.isBlank(email)) {
			return false;
		}
		return email.matches(REGEX_EMAIL);
	}


	public static boolean checkChinese(String str) {
//		if ( StringUtils.isBlank(str)) {
//			return false;
//		}
		return str.matches(REGEX_CHINESE);
	}

	public static boolean checkID(String idCardNumber) {
		if ( StringUtils.isBlank(idCardNumber)) {
			return false;
		}
		return idCardNumber.matches(REGEX_ID);
	}

	public static boolean checkForeignID(String idCardNumber) {
		if ( StringUtils.isBlank(idCardNumber)) {
			return false;
		}
		return idCardNumber.matches(REGEX_ID_FOREIGN);
	}


//	public static void main(String[] args) {
//		String str = " a";
//		System.err.println(checkEnglish(str));
//		System.err.println(checkEnglishChinese("z 中 "));
//		System.err.println(checkEmail("1@yahoo.com"));
//		System.err.println(checkChinese(""));
//		System.err.println(checkForeignID("a1"));
//	}



}
