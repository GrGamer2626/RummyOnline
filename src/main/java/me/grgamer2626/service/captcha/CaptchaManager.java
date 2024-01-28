package me.grgamer2626.service.captcha;


import me.grgamer2626.utils.dto.captcha.CaptchaResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Service
public class CaptchaManager implements CaptchaService {
	
	private final RestTemplate restTemplate;
	private final String verifyUrl;
	private final String secretKey;
	private final String siteKey;
	
	@Autowired
	public CaptchaManager(RestTemplate restTemplate, @Value("${hcaptcha.secret-key}") String secretKey, @Value("${hcaptcha.site-key}") String siteKey) {
		this.restTemplate = restTemplate;
		this.verifyUrl = "https://api.hcaptcha.com/siteverify";
		this.secretKey = secretKey;
		this.siteKey = siteKey;
	}
	
	////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	
	@Override
	public CaptchaResponse validateToken(String captchaToken) {
		HttpHeaders headers =  new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
		
		MultiValueMap<String, String> map = new LinkedMultiValueMap<>();
		map.add("secret", secretKey);
		map.add("response", captchaToken);
		
		HttpEntity<MultiValueMap<String, String>> entity = new HttpEntity<>(map, headers);
		
		ResponseEntity<CaptchaResponse> response = restTemplate.exchange(verifyUrl, HttpMethod.POST, entity, CaptchaResponse.class);
		
		return response.getBody();
	}
	
	
	@Override
	public String getSiteKey() {
		return siteKey;
	}
}
