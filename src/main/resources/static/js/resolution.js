function showResolution() {
    var windowWidth = window.screen.width;
    var windowHeight = window.screen.height;

    document.getElementById("resolution").innerHTML = "Screen resolution: " + windowWidth + " x " + windowHeight;
}

showResolution();