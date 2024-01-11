
let lineWidth = 2;
let strokeStyle = "#000";
let rubberSelected = false;

document.addEventListener("DOMContentLoaded", function () {
    const canvas = document.getElementById("whiteboard");
    const context = canvas.getContext("2d");
    let drawing = false;
    function clearCanvas() {
        context.clearRect(0, 0, canvas.width, canvas.height);
    }
    canvas.addEventListener("mousedown", startDrawing);
    canvas.addEventListener("mouseup", stopDrawing);
    canvas.addEventListener("mousemove", draw);

    function startDrawing(e) {
        drawing = true;
        draw(e);
    }

    function stopDrawing() {
        drawing = false;
        context.beginPath();
    }

    function draw(e) {
        if (!drawing) return;

        context.lineWidth = lineWidth;
        context.lineCap = "round";
        context.strokeStyle = strokeStyle;

        context.lineTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
        context.stroke();
        context.beginPath();
        context.moveTo(e.clientX - canvas.offsetLeft, e.clientY - canvas.offsetTop);
    }

    const clearButton = document.getElementById("clear");
    clearButton.onclick = () => {
        clearCanvas();
    }

    const rubberButton = document.getElementById("rubber");

    rubberButton.onclick = () => {
        if (rubberSelected){
            return;
        }
        rubberSelected = !rubberSelected;
        lineWidth = 14;
        strokeStyle = "white";
    }

    const panButton = document.getElementById("pan");
    panButton.onclick = () => {
        if(!rubberSelected){
            return;
        }
        rubberSelected = !rubberSelected;
        lineWidth = 2;
        strokeStyle = "#000";
    }

});