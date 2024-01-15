
let firstClickWithSelectedLine = true;
let xLineStart = 0;
let yLineStart = 0;
let isDrawing = false;
let lastX, lastY;
document.addEventListener("DOMContentLoaded", function () {
    const canvas = document.getElementById("whiteboard");
    let context = canvas.getContext("2d");
    context.strokeStyle = "black";
    context.lineWidth = 2;
    function line(context, xStart, yStart, xFinish, yFinish){
        context.beginPath();
        context.moveTo(xStart, yStart);
        context.lineTo(xFinish, yFinish);
        context.stroke();
        context.closePath();
    }
    function clearCanvas() {
        context.clearRect(0, 0, canvas.width, canvas.height);
    }
    function addEventListenersOnDraw(){
        canvas.addEventListener('mousedown', startDrawing);
        canvas.addEventListener('mousemove', drawLine);
        canvas.addEventListener('mouseup', stopDrawing);
        canvas.addEventListener('mouseout', stopDrawing);
    }
    function removeEventListenersOnDraw(){
        canvas.removeEventListener('mousedown', startDrawing);
        canvas.removeEventListener('mousemove', drawLine);
        canvas.removeEventListener('mouseup', stopDrawing);
        canvas.removeEventListener('mouseout', stopDrawing);
    }
    addEventListenersOnDraw();
    function lineClickHandler(event){
        context.lineWidth = 2;
        context.strokeStyle = "#000";
        if (firstClickWithSelectedLine){
            xLineStart = event.clientX - canvas.offsetLeft;
            yLineStart = event.clientY - canvas.offsetTop + window.scrollY;
            line(context, xLineStart, yLineStart, xLineStart+1, yLineStart+1);
            firstClickWithSelectedLine = false;
        }else{
            firstClickWithSelectedLine = true;
            line(context, xLineStart, yLineStart, event.clientX - canvas.offsetLeft, yLineStart = event.clientY- canvas.offsetTop + window.scrollY);
        }
    }

    function startDrawing(e) {
        isDrawing = true;
        [lastX, lastY] = [e.clientX - canvas.offsetLeft, e.clientY- canvas.offsetTop + window.scrollY];
    }

    function drawLine(e) {
        if (!isDrawing) return;

        let currentX = e.clientX - canvas.offsetLeft;
        let currentY = e.clientY - canvas.offsetTop + window.scrollY;

        context.beginPath();
        context.moveTo(lastX, lastY);
        context.lineTo(currentX, currentY);
        context.stroke();
        context.closePath();

        [lastX, lastY] = [currentX, currentY];
    }

    function stopDrawing() {
        isDrawing = false;
    }

    const clearButton = document.getElementById("clear");
    clearButton.onclick = () => {
        clearCanvas();
    }
    const rubberButton = document.getElementById("rubber");
    rubberButton.onclick = () => {
        canvas.removeEventListener("click", lineClickHandler);
        context.strokeStyle = "white";
        context.lineWidth = 14;
        firstClickWithSelectedLine = true;
        addEventListenersOnDraw();
        rubberButton.classList.add("selected-border");
        panButton.classList.remove("selected-border");
        lineButton.classList.remove("selected-border");
    }
    const panButton = document.getElementById("pan");
    panButton.onclick = () => {
        context.lineWidth = 2;
        context.strokeStyle = "#000";
        canvas.removeEventListener("click", lineClickHandler);
        console.log("click removed")
        firstClickWithSelectedLine = true;
        addEventListenersOnDraw();
        panButton.classList.add("selected-border");
        rubberButton.classList.remove("selected-border");
        lineButton.classList.remove("selected-border");
    }

    const lineButton = document.getElementById("line");

    lineButton.onclick = () => {
        firstClickWithSelectedLine = true;
        removeEventListenersOnDraw();
        canvas.addEventListener("click", lineClickHandler);
        panButton.classList.remove("selected-border");
        rubberButton.classList.remove("selected-border");
        lineButton.classList.add("selected-border");
    }

});