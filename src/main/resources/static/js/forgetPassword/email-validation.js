document.addEventListener("DOMContentLoaded", () => {
	const form = document.querySelector('form');


	const emailInput = document.getElementById('email');
	const emailError = document.getElementById('email-error');


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
		if (!validateEmail()) {
			e.preventDefault();
		}
	});

	emailInput.addEventListener('input', validateEmail);

});