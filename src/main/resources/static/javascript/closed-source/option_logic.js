let startButton = document.getElementById("start");
let tasksWrapper = document.getElementById("tasks-wrapper");
let timerRunning;


const currentDate = new Date();
currentDate.setHours(currentDate.getHours() + 3);
currentDate.setMinutes(currentDate.getMinutes() + 55);
currentDate.setSeconds(currentDate.getSeconds() + 1);

function generateOption(){
    fetch(`/api/v1/client/options/autogenerate`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            localStorage.setItem("last_option", JSON.stringify(data))
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
}

function fillPage(){
    let option = JSON.parse(localStorage.getItem("last_option"));
    let num = 1;
    for(let taskObj of option){
        let task = document.createElement('div');
        task.classList.add("task");
        let taskQuestion = document.createElement('div');
        taskQuestion.classList.add("task-text");
        taskQuestion.innerText = taskObj.task;
        let taskLabel = document.createElement('div');
        taskLabel.innerText = "Задание №" + num;
        taskQuestion.classList.add("task-label");
        num ++;
        task.appendChild(taskQuestion);
        if(!taskObj.module.verifiable){
            console.log("not verifiable")
            let taskAnswer = document.createElement('div');
            taskAnswer.classList.add("answer");
            taskAnswer.textContent = taskObj.answer;
            task.appendChild(taskAnswer);
        }else{
            let inputAnswer = document.createElement('input');
            inputAnswer.classList.add("input-text");
            task.appendChild(inputAnswer);
        }
        task.appendChild(taskLabel);
        tasksWrapper.appendChild(task);
    }
}
let lastAttempt = localStorage.getItem("time_left");

if(lastAttempt === null){
    const endTime = currentDate.getTime();
    let timeLeft = endTime - new Date().getTime();
    localStorage.setItem("time_left", timeLeft);
    generateOption();
}else{

    //если время на таймере истекло генерируем новый вариант
    if (localStorage.getItem("time_left") < 0) {
        const endTime = currentDate.getTime();
        let timeLeft = endTime - new Date().getTime();
        localStorage.setItem("time_left", timeLeft);
        generateOption();
    //чтобы продолжить выполнение берем option и таймер из localStorage
    } else {
        fillPage(JSON.parse(localStorage.getItem("last_option")));
    }
}

function transformToDoubleDigits(digit){
    if(![0, 1, 2, 3, 4, 5, 6, 7, 8, 9].includes(digit)){
        return digit;
    }else if(digit === 0){
        return "00";
    }else{
        return "0" + digit;
    }
}

let timerInterval;

let timeLeft = localStorage.getItem("time_left");
function startTimer() {
    timerRunning = true;
    timerInterval = setInterval(updateTimer, 1000);
}
function pauseTimer() {
    timerRunning = false;
    clearInterval(timerInterval);
}
function updateTimer() {
    timeLeft -= 1000;
    localStorage.setItem("time_left", timeLeft);
    if (timeLeft <= 0) {
        clearInterval(timerInterval);
        document.getElementById('timer').innerHTML = 'Время вышло';
    } else {
        const hours = Math.floor((timeLeft % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
        const minutes = Math.floor((timeLeft % (1000 * 60 * 60)) / (1000 * 60));
        const seconds = Math.floor((timeLeft % (1000 * 60)) / 1000);
        document.getElementById('timer').innerHTML =
            `${hours}:${transformToDoubleDigits(minutes)}:${transformToDoubleDigits(seconds)}`;
    }
}

let startSvg = "<svg id=\"start-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"28\" height=\"28\">\n" +
    "  <path d=\"M 6 6 L 6 22 L 21 14 L 6 6\"/>\n" +
    "</svg>\n"
let pauseSvg = "<svg id=\"pause-svg\" xmlns=\"http://www.w3.org/2000/svg\" viewBox=\"0 0 24 24\" width=\"28\" height=\"28\">\n" +
    "                <path d=\"M6 19h4V5H6v14zm8-14v14h4V5h-4z\"/>\n" +
    "            </svg>"

startTimer();
startButton.addEventListener("click", ()=>{
    if (timerRunning){
        pauseTimer();
        startButton.innerHTML = startSvg;
    }else{
        startTimer();
        startButton.innerHTML = pauseSvg;
    }
});



