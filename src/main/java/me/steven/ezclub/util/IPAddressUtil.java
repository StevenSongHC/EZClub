package me.steven.ezclub.util;

import javax.servlet.http.HttpServletRequest;

public class IPAddressUtil {

	public static String getIPAddress(HttpServletRequest request) {
		String ip = request.getHeader("x-forwarded-for");
		if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("Proxy-Client-IP");
		}
		if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getHeader("WL-Proxy-Client_IP");
		}
		if (ip == null || ip.length() == 0 || "unknow".equalsIgnoreCase(ip)) {
			ip = request.getRemoteAddr();
		}
		return ip;
	}
	
}
