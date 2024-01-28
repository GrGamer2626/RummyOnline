package me.grgamer2626.service.captcha;

import me.grgamer2626.utils.dto.captcha.CaptchaResponse;



public interface CaptchaService {
	
	CaptchaResponse validateToken(String captchaToken);
	
	String getSiteKey();
}
