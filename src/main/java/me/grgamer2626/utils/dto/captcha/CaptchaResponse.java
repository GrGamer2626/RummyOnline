package me.grgamer2626.utils.dto.captcha;

import java.util.Date;

public record CaptchaResponse(
		boolean success,
		String challenge_ts,
		String hostname) {
}
