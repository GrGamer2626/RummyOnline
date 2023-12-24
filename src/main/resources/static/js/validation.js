document.addEventListener("DOMContentLoaded", () => {
	const form = document.querySelector('form');

	const nickNameInput = document.getElementById('nick-name');
	const nickNameError = document.getElementById('nick-name-error');

	const emailInput = document.getElementById('email');
	const emailError = document.getElementById('email-error');

	const passwordInput = document.getElementById('password');
	const passwordError = document.getElementById('password-error');

	const confirmPasswordInput = document.getElementById('confirm-password');
	const confirmPasswordError = document.getElementById('confirm-password-error');

	function validateNickName() {
		const nickNameValue = nickNameInput.value;
		const length = nickNameValue.length;
		const nickNameRegex = /^[a-zA-Z0-9._-]+$/;

		const messages = [];

		if(length === 0) {
			showError(nickNameError, "Nick Name is required!");
			return false;

		}else {
			if (length < 5) {
				messages.push("Nick Name is too short!");
			}
			if (length > 30) {
				messages.push("Nick Name is too long!");
			}
			if (!nickNameRegex.test(nickNameValue)) {
				messages.push("Nick name contains illegal characters!");
			}
		}
		if (messages.length) {
			showError(nickNameError, messages);
			return false;
		}
		clearError(nickNameError);
		return true;
	}

	function validateEmail() {
		const emailValue = emailInput.value;
		const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;

		if(emailValue.length === 0) {
			showError(emailError, "Email is required!")
			return false;

		}else if (!emailRegex.test(emailValue)) {
			showError(emailError, "Invalid email address");
			return false;
		}

		clearError(emailError);
		return true;
	}

	function validatePassword() {
		const passwordValue = passwordInput.value;
		const length = passwordValue.length;
		const passwordRegexReq = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[!@#$%^&*_+=|~?-]).*$/;
		const passwordRegexAllowed = /^[a-zA-Z0-9!@#$%^&*_+=|~?-]+$/;

		const messages = [];
		if(length === 0) {
			showError(passwordError, "Password is required!");
			return false;

		}else {
			if (!passwordRegexAllowed.test(passwordValue)) {
				messages.push("The password contains illegal characters!");
			}
			if (!passwordRegexReq.test(passwordValue)) {
				messages.push("The password must contain at least one uppercase letter, lowercase letter, number and a special character!");
			}
			if (length < 8) {
				messages.push("The password must have at least 8 characters!");
			}
			if (length > 64) {
				messages.push("The password is too long!");
			}
		}

		if(messages.length) {
			showError(passwordError, messages);
			return false;
		}

		clearError(passwordError);
		return true;
	}

	function validateConfirmPassword() {
		const confirmPasswordValue = confirmPasswordInput.value;
		const passwordValue = passwordInput.value;

		if(confirmPasswordValue.length === 0) {
			showError(confirmPasswordError, "Confirm password is required!");
			return false;

		}else if (confirmPasswordValue !== passwordValue) {
			showError(confirmPasswordError, "Password and confirmation must be identical!");
			return false;
		}

		clearError(confirmPasswordError);
		return true;
	}

	function showError(input, message) {
		if(Array.isArray(message)) {
			input.innerHTML = message.map(msg => `<div class="message">${msg}</div>`).join('');
			return;
		}
		input.innerHTML = `<div class="message">${message}</div>`;
	}

	function clearError(input) {
		input.textContent  = '';
	}

	form.addEventListener('submit', e => {
		if (!validateNickName() || !validateEmail() || !validatePassword() || !validateConfirmPassword()) {
			e.preventDefault();
		}
	});

	nickNameInput.addEventListener('input', validateNickName);
	emailInput.addEventListener('input', validateEmail);
	passwordInput.addEventListener('input', validatePassword);
	confirmPasswordInput.addEventListener('input', validateConfirmPassword);
});