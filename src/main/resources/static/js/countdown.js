var countdown = 15;

function updateCountdown() {
    if (countdown <= 0) {
        clearInterval(countdownInterval);
        window.location.href = "/";
        return;
    }
    countdown--;
    document.getElementById('countdown').textContent = countdown;
}

var countdownInterval = setInterval(updateCountdown, 1000);