
let firstClickWithSelectedLine = true;
let xLineStart = 0;
let yLineStart = 0;
let isDrawing = false;
let lastX, lastY;

let savedSettings = {
    lineWidth: 2,
    strokeStyle: "black",
    fillStyle: "black"
};

function saveContextStyle(context){
    savedSettings.lineWidth = context.lineWidth;
    savedSettings.strokeStyle = context.strokeStyle;
    savedSettings.fillStyle = context.fillStyle;
}

function applySavedContextSettings(context){
    context.lineWidth = savedSettings.lineWidth;
    context.strokeStyle = savedSettings.strokeStyle;
    savedSettings.fillStyle = context.fillStyle;
}

let canvasStateStack = [];

function saveCanvasState(canvas){
    let context = canvas.getContext("2d");
    let imageData = context.getImageData(0, 0, canvas.width, canvas.height - 30);
    canvasStateStack.push(imageData);
}

function returnLastCanvasState(canvas){
    let imageData = canvasStateStack.pop();
    if (imageData !== undefined) {
        let context = canvas.getContext("2d");
        context.clearRect(0, 0, canvas.width, canvas.height);
        context.putImageData(imageData, 0, 0);
        drawArrowDown(canvas, "black");
    }
}

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
    let canvasPanel = document.getElementById("canvas_panel");
    let canvasRect = canvas.getBoundingClientRect();

    //получаем margin нашего canvas
    const CANVAS_MARGIN = parseInt(window.getComputedStyle(canvas).marginTop);
    console.log(CANVAS_MARGIN);
    //получаем отступ элемента, в который обернута наша панель
    const TOP_OFFSET = canvasPanel.getBoundingClientRect().top;


    function computeY(event){
        // Получаем текущее значение прокрутки по вертикали (в пикселях)
        let SCROLL = canvasPanel.scrollTop;
        console.log(SCROLL);
        //отнимаем и прибавляем, учитывая тот факт, что отсчет y в канвасе ведется сверху вниз
        return event.clientY - CANVAS_MARGIN - TOP_OFFSET + SCROLL;
    }

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
        saveContextStyle(context);
        context.clearRect(0, 0, canvas.width, canvas.height);
        drawArrowDown(canvas, 'black');
        applySavedContextSettings(context);
    }

    canvas.addEventListener("click", (event)=>{

       if (Math.abs(event.clientX - canvasRect.left - (canvas.width/2)) < 40 &&
           Math.abs(computeY(event) - canvas.height) < 40){
           saveContextStyle(context);
           eraseSquareInCenter(canvas, 35);
           let imageData = context.getImageData(0, 0, canvas.width, canvas.height);
           canvas.height += 300;
           context.putImageData(imageData, 0, 0);
           drawArrowDown(canvas, 'black');
           applySavedContextSettings(context);
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
            saveCanvasState(canvas);
            xLineStart = event.clientX - canvasRect.left;
            yLineStart = computeY(event);
            line(context, xLineStart, yLineStart, xLineStart+1, yLineStart+1);
            firstClickWithSelectedLine = false;
        }else{
            firstClickWithSelectedLine = true;

            let currentY = computeY(event);

            line(context, xLineStart, yLineStart, event.clientX - canvasRect.left, yLineStart = currentY);
        }
    }

    function startDrawing(e) {
        if(e.button !== 2){
            saveCanvasState(canvas);
            isDrawing = true;

            let currentY = computeY(event);

            [lastX, lastY] = [e.clientX - canvasRect.left, currentY];
        }
    }

    function drawLine(e) {
        if (!isDrawing) return;

        let currentX = e.clientX - canvasRect.left;
        let currentY = computeY(e);
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
        saveCanvasState(canvas);
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

    const backButton = document.getElementById("back");

    const contextStyleSafeCanvasStatementReturn = (canvas, context) =>{
        firstClickWithSelectedLine = true;
        saveContextStyle(context);
        returnLastCanvasState(canvas, context);
        applySavedContextSettings(context);
    }
    backButton.addEventListener("click", ()=>{
        contextStyleSafeCanvasStatementReturn(canvas, context);
    })
    canvas.addEventListener('contextmenu', function(event) {
        event.preventDefault();
        contextStyleSafeCanvasStatementReturn(canvas, context);
    });
});