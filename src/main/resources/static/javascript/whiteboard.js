
let firstClickWithSelectedLine = true;
let xLineStart = 0;
let yLineStart = 0;
let isDrawing = false;
let lastX, lastY;

function drawArrowDown(canvas, color) {
    let context = canvas.getContext('2d');
    context.imageSmoothingEnabled = false;
    let canvasWidth = canvas.width;
    let canvasHeight = canvas.height - 10;
    let arrowSize = 20;
    let arrowWidth = 23;
    context.strokeStyle = color;
    context.beginPath();
    context.moveTo(canvasWidth / 2 - arrowWidth / 2, canvasHeight - arrowSize);
    context.lineTo(canvasWidth / 2 + arrowWidth / 2, canvasHeight - arrowSize);
    context.lineTo(canvasWidth / 2, canvasHeight);
    context.closePath();
    context.fillStyle = color;
    context.fill();
}
function eraseSquareInCenter(canvas, size) {
    let context = canvas.getContext('2d');
    let x = canvas.width / 2 - size / 2;
    let y = canvas.height - size;
    context.clearRect(x, y, size, size);
}
document.addEventListener("DOMContentLoaded", function () {
    const canvas = document.getElementById("whiteboard");
    drawArrowDown(canvas, "black");
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
        let savedSettings = {
            lineWidth: context.lineWidth,
            strokeStyle: context.strokeStyle,
            fillStyle: context.fillStyle,
            font: context.font
        };
        context.clearRect(0, 0, canvas.width, canvas.height);
        drawArrowDown(canvas, 'black');
        context.lineWidth = savedSettings.lineWidth;
        context.strokeStyle = savedSettings.strokeStyle;
        context.fillStyle = savedSettings.fillStyle;
        context.font = savedSettings.font;
    }

    canvas.addEventListener("click", (event)=>{

       if (Math.abs(event.clientX - canvas.offsetLeft - (canvas.width/2)) < 40 &&
           Math.abs(event.clientY - canvas.offsetTop + window.scrollY - canvas.height) < 40){

           let savedSettings = {
               lineWidth: context.lineWidth,
               strokeStyle: context.strokeStyle,
               fillStyle: context.fillStyle,
               font: context.font
           };
           eraseSquareInCenter(canvas, 35);
           let imageData = context.getImageData(0, 0, canvas.width, canvas.height);
           canvas.height += 300;
           context.putImageData(imageData, 0, 0);
           drawArrowDown(canvas, 'black');
           context.lineWidth = savedSettings.lineWidth;
           context.strokeStyle = savedSettings.strokeStyle;
           context.fillStyle = savedSettings.fillStyle;
           context.font = savedSettings.font;
       }
    });
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
        context.lineWidth = 30;
        firstClickWithSelectedLine = true;
        addEventListenersOnDraw();
        rubberButton.classList.add("selected-border");
        panButton.classList.remove("selected-border");
        lineButton.classList.remove("selected-border");
        canvas.classList.add("cursor-cross-hair");
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
        canvas.classList.remove("cursor-cross-hair");
    }

    const lineButton = document.getElementById("line");

    lineButton.onclick = () => {
        firstClickWithSelectedLine = true;
        removeEventListenersOnDraw();
        canvas.addEventListener("click", lineClickHandler);
        panButton.classList.remove("selected-border");
        rubberButton.classList.remove("selected-border");
        lineButton.classList.add("selected-border");
        canvas.classList.add("cursor-cross-hair");
    }

});