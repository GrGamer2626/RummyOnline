package me.grgamer2626.service.captcha;


import me.grgamer2626.utils.dto.captcha.CaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class CaptchaManager implements CaptchaService {
	
	private final String verifyUrl;
	private final String secretKey;
	private final String siteKey;
	
	@Autowired
	public CaptchaManager(@Value("${hcaptcha.secret-key}") String secretKey, @Value("${hcaptcha.site-key}") String siteKey) {
		this.verifyUrl = "https://api.hcaptcha.com/siteverify";
		this.secretKey = secretKey;
		this.siteKey = siteKey;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public CaptchaResponse validateToken(String captchaToken) {

		return null;
	}
	
	
	@Override
	public String getSiteKey() {
		return siteKey;
	}
}
