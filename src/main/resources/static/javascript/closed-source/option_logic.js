let startButton = document.getElementById("start");
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
let timerRunning;
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let scrollLeft = document.getElementById("scroll_left");
let scrollRight = document.getElementById("scroll_right");
let inputWrap = document.getElementById("input-wrap");
let answerWrapper = document.getElementById("answer_wrapper");
let answerText = document.getElementById("_answer");
let answer = document.getElementById("answer");
let showAnswer = document.getElementById("show_answer");
let input = document.getElementById("input-answer");
let answerIsShow = false;

let listTasksToSubmit = [];
let currentTaskOrder = 1;
let arrOfNavigationButtons = [];
let taskNum = document.getElementById("task-num");
let taskText = document.getElementById("task-text");
if (arrowLeft !== null){
    arrowLeft.addEventListener("mouseover", ()=>{
        container.classList.add("rotate-1-left");
    });
    arrowLeft.addEventListener("mouseout", ()=>{
        container.classList.remove("rotate-1-left");
    });
}
arrowRight.addEventListener("click", ()=>{
    if(currentTaskOrder !== arrOfNavigationButtons.length){
        arrOfNavigationButtons[currentTaskOrder].click();
    }
});
arrowLeft.addEventListener("click", () =>{
    if(currentTaskOrder !== 1){
        arrOfNavigationButtons[currentTaskOrder - 2].click();
    }
});

if (arrowRight !== null){
    arrowRight.addEventListener("mouseover", ()=>{
        container.classList.add("rotate-1-right");
    });
    arrowRight.addEventListener("mouseout", ()=>{
        container.classList.remove("rotate-1-right");
    });
}
scrollLeft.addEventListener("click", () => {
    navigationButtonsWrapper.scrollLeft -= 200;
});
scrollRight.addEventListener("click", function() {
    navigationButtonsWrapper.scrollLeft += 200;
});
function wheelHandler(event) {
    //если это тачпад ничего не делаем он и так хорошо работает
    if(event.deltaX !== 0){
        return;
    }
    event.preventDefault();
    navigationButtonsWrapper.scrollLeft += event.deltaY * 2;
}

navigationButtonsWrapper.addEventListener("wheel", wheelHandler);

const currentDate = new Date();
currentDate.setHours(currentDate.getHours() + 3);
currentDate.setMinutes(currentDate.getMinutes() + 55);
currentDate.setSeconds(currentDate.getSeconds() + 1);

function fillPage(){
    let option = JSON.parse(localStorage.getItem("last_option"));
    for(let taskObj of option){
        listTasksToSubmit.push(
            {
                id: taskObj.id,
                answer: null,
                score: 0
            }
        );
        let button = document.createElement('span');
        arrOfNavigationButtons.push(button);
        let num = arrOfNavigationButtons.length;
        button.classList.add("navigation-button");
        button.textContent = num;
        button.id = "tab_"+num;
        listTasksToSubmit[num-1].id = taskObj.id;
        button.addEventListener("click", ()=>{
            if(answerIsShow){
                showAnswer.click();
            }
            listTasksToSubmit[currentTaskOrder-1].id = taskObj.id;
            for(let b of arrOfNavigationButtons){
                b.classList.remove("navigation-tab-selected");
            }
            currentTaskOrder = num;
            button.classList.add("navigation-tab-selected");
            taskNum.innerText = num;
            taskText.innerText = taskObj.task;

            if(taskObj.module.verifiable){
                input.value = listTasksToSubmit[currentTaskOrder-1].answer;
                inputWrap.classList.remove("display-none");
                answerWrapper.classList.add("display-none");
            }else{
                inputWrap.classList.add("display-none");
                answerText.innerText = taskObj.answer;
                answerWrapper.classList.remove("display-none");
            }
            if(currentTaskOrder === arrOfNavigationButtons.length){
                arrowRight.classList.add("display-none");
            }else if(currentTaskOrder === 1){
                arrowLeft.classList.add("display-none");
            }else{
                arrowLeft.classList.remove("display-none");
                arrowRight.classList.remove("display-none");
            }
        });
        navigationButtonsWrapper.appendChild(button);
    }
    arrOfNavigationButtons[0].click();
}

input.addEventListener("input", (event)=>{
    listTasksToSubmit[currentTaskOrder-1].answer = event.target.value;
});
function generateOption(){
    fetch(`/api/v1/client/options/autogenerate`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            localStorage.setItem("last_option", JSON.stringify(data));
            fillPage();
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
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

showAnswer.addEventListener("click", ()=>{
    if (!answerIsShow){
        answerIsShow = true;
        answer.classList.remove("display-none");
        showAnswer.textContent = "Скрыть решение";
    }else{
        answerIsShow = false;
        answer.classList.add("display-none");
        showAnswer.textContent = "Показать решение";
    }
});
