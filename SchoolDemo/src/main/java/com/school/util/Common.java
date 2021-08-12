package com.school.util;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

public class Common {

	private static final Logger logger = LoggerFactory.getLogger(Common.class);

	public static Object invokeMethod(Object owner, String methodName, Object[] args) throws Exception {

		Class ownerClass = owner.getClass();

		Class[] argsClass = new Class[args.length];

		for (int i = 0, j = args.length; i < j; i++) {
			argsClass[i] = args[i].getClass();
		}

		Method method = ownerClass.getMethod(methodName, argsClass);

		return method.invoke(owner, args);
	}

	public static String converToMD5(String text) throws NoSuchAlgorithmException {
		String result = null;
		MessageDigest md = null;
		byte[] digest = null;
		final StringBuffer buffer;

		if (text != null) {
			md = MessageDigest.getInstance("MD5");
			md.update(text.getBytes());

			digest = md.digest();

			result = new String(digest);

			buffer = new StringBuffer();

			for (int i = 0; i < digest.length; ++i) {
				final byte b = digest[i];
				final int value = (b & 0x7F) + (b < 0 ? 128 : 0);
				buffer.append(value < 16 ? "0" : "");
				buffer.append(Integer.toHexString(value));
			}

			result = buffer.toString();
		}

		return result;
	}

	public static Date stringToDate(String date, String format) {
		Date result = null;

		if (date != null || format != null) {
			try {
				SimpleDateFormat formatter = new SimpleDateFormat(format);
				result = formatter.parse(date);
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
		}

		return result;
	}

	public static String dateToString(Date date, String format) {
		String result = "";
		if (date != null || format != null) {
			try {
				SimpleDateFormat sdf = new SimpleDateFormat(format);
				result = sdf.format(date);
			} catch (Exception e) {
				logger.error(e.toString(), e);
			}
		}
		return result;
	}

	public static String InputStreamToString(InputStream is) {
		String str = null;
		try {
			BufferedInputStream bis = new BufferedInputStream(is);
			ByteArrayOutputStream buf = new ByteArrayOutputStream();
			int result = bis.read();
			while (result != -1) {
				buf.write((byte) result);
				result = bis.read();
			}
			str = buf.toString("UTF-8");
		} catch (Exception e) {
			logger.error(e.toString(), e);
		}
		return str;
	}

	public static Properties loadPropertyFile(String fileName) {
		Properties prop = new Properties();
		try {
			InputStream in = new BufferedInputStream(new FileInputStream(fileName));
			prop.load(in);
		} catch (Exception e) {
		}
		return prop;
	}

	public static String createRandomVcode(int length) {
		String vcode = "";
		for (int i = 0; i < length; i++) {
			vcode = vcode + (int) (Math.random() * 9);
		}
		return vcode;
	}

	public static Date addDays(Date date, int days) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days); // minus number would decrement the days
		return cal.getTime();
	}

	public static Date addMinutes(Date date, int minutes) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minutes);
		return cal.getTime();
	}

	public static Date addMonth(Date date, int months) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, months); // minus number would decrement the days
		return cal.getTime();
	}

	public static String objectToString(Object object) {
		if (object == null)
			return "";
		return object.toString();
	}

	public static List<String> getBeanFields(Object obj) throws IllegalArgumentException, IllegalAccessException {
		List<String> list = new ArrayList<String>();
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				field.setAccessible(true); // 设置这个可访问私有属性
				list.add(field.getName());
			}
		}

		return list;
	}

	public static List<Class> getBeanFieldTypes(Object obj) throws IllegalArgumentException, IllegalAccessException {
		List<Class> list = new ArrayList<Class>();
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				field.setAccessible(true); // 设置这个可访问私有属性
				list.add(field.getType());
			}
		}

		return list;
	}

	public static Class getFieldTypes(Object obj, String fieldName)
			throws IllegalArgumentException, IllegalAccessException {
		List<Class> list = new ArrayList<Class>();
		Field[] fields = obj.getClass().getDeclaredFields();
		if (fields != null && fields.length > 0) {
			for (Field field : fields) {
				field.setAccessible(true); // 设置这个可访问私有属性
				if (field.getName().compareTo(fieldName) == 0) {
					return field.getType();
				}
			}
		}
		return null;
	}

	public static boolean isInteger(String str) {
		try {
			Long.parseLong(str);
		} catch (Exception ex) {
			return false;
		}
		return true;
	}

	public static String getCnRequestParameter(HttpServletRequest req, String name) {
		String result = objectToString(req.getParameter(name));
		try {
			result = new String(result.getBytes("iso-8859-1"), "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return result;
	}

	public static String formatNumber(String input, int decimalLength) {
		String decimal = "";
		if (input.contains(".")) {
			decimal = input.substring(input.indexOf("."));
			decimal = padding(decimal, "R", decimalLength + 1, "0");
			input = input.substring(0, input.indexOf("."));
		}
		int commaCount = (input.length() - 1) / 3;
		String result = "";
		for (int i = 1; i < commaCount + 1; i++) {
			result = input.substring(input.length() - i * 3, input.length() - (i - 1) * 3) + result;
			result = "," + result;
		}
		result = input.substring(0, input.length() - commaCount * 3) + result + decimal;
		return result;
	}

	// suport negative and integer
	public static String formatNumber1(String input, int decimalLength) {
		String sign = "";
		if (input.startsWith("-")) {
			sign = "-";
			input = input.substring(1);
		}
		String decimal = "";
		if (input.contains(".")) {
			decimal = input.substring(input.indexOf("."));
			if (decimal.length() > decimalLength + 1) {
				decimal = "0" + decimal;
				decimal = String.valueOf(round(decimal, 2));
				decimal = decimal.substring(1);
			}
			if (decimal.length() < decimalLength + 1) {
				decimal = padding(decimal, "R", decimalLength + 1, "0");
			}
			input = input.substring(0, input.indexOf("."));
		} else {
			decimal = ".";
			for (int i = 0; i < decimalLength; i++) {
				decimal += "0";
			}
		}
		int commaCount = (input.length() - 1) / 3;
		String result = "";
		for (int i = 1; i < commaCount + 1; i++) {
			result = input.substring(input.length() - i * 3, input.length() - (i - 1) * 3) + result;
			result = "," + result;
		}
		result = sign + input.substring(0, input.length() - commaCount * 3) + result + decimal;
		return result;
	}
	

	public static String padding(String input, String type, int length, String paddingStr){
		String output = input;
		if(input==null) input = "";
		String padding = "";
		if(input.length()<length){
			for(int i=0; i<length-input.length(); i++){
				padding += paddingStr;
			}
		}
		//Left Padding
		if(type.compareToIgnoreCase("L")==0){
			output = padding + output;
		}
		//Right Padding
		if(type.compareToIgnoreCase("R")==0){
			output = output + padding;
		}
		return output;
	}
	

	public static String getRemoteIP(HttpServletRequest request) {
		if (request.getHeader("x-forwarded-for") == null) {
			return request.getRemoteAddr();
		}
		return request.getHeader("x-forwarded-for");
	}

	public static String getRealIpAddr(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
			// 多次反向代理后会有多个ip值，第一个ip才是真实ip
			if (ip.indexOf(",") != -1) {
				ip = ip.split(",")[0];
			}
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_CLIENT_IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("HTTP_X_FORWARDED_FOR");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getHeader("X-Real-IP");
		}
		if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}

	public static double round(String input, int decimalLength) {
		if (input.length() > decimalLength + 3) {
			input = input.substring(0, decimalLength + 3); // cut to avoid float number problem
		}
		float f = Float.parseFloat(input);
		return Math.round(f * 100) * 0.01;
	}

	public static File convertMultiPartToFile(MultipartFile file) throws IOException {
		File convFile = new File(file.getOriginalFilename());
		FileOutputStream fos = new FileOutputStream(convFile);
		fos.write(file.getBytes());
		fos.close();
		return convFile;
	}
}
