package me.grgamer2626.utils;

import jakarta.servlet.http.HttpServletRequest;

public interface ApplicationUrl {
	
	default String createApplicationUrl(HttpServletRequest request) {
		return "http://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
	}
}
