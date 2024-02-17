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
let score = document.getElementById("score");
let submitOption = document.getElementById("submit-option");
let sendWarning = document.getElementById("send_warning");
let no = document.getElementById("no");
let yes = document.getElementById("yes");
let onMain = document.getElementById("on-main");
let feedbackWrapper = document.getElementById("feedback-wrapper");
let timerBlock = document.getElementById("timer-block");
let resultText = document.getElementById("result");
let watchResults = document.getElementById("watch-results");
let answerIsShow = false;
let selfScoreInput = document.getElementById("self_score_input_wrapper");
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
                selfScoreInput.classList.add("display-none");
                showAnswer.classList.add("display-none");
            }else{
                inputWrap.classList.add("display-none");
                score.classList.remove("display-none");
                score.setAttribute("max", taskObj.module.maxPoints);
                score.value = listTasksToSubmit[currentTaskOrder-1].score;
                answerText.innerText = taskObj.answer;
                answerWrapper.classList.remove("display-none");
                selfScoreInput.classList.remove("display-none");
                showAnswer.classList.remove("display-none")
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
    localStorage.setItem("lastAnswers", JSON.stringify(listTasksToSubmit));
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
            for(let taskObj of data) {
                listTasksToSubmit.push(
                    {
                        id: taskObj.id,
                        answer: null,
                        score: 0,
                        module: taskObj.module.id
                    }
                );
            }
            localStorage.setItem("lastAnswers", JSON.stringify(listTasksToSubmit));
            fillPage();
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
}

score.addEventListener("input", (event)=>{
    listTasksToSubmit[currentTaskOrder-1].score = event.target.value;
    localStorage.setItem("lastAnswers", JSON.stringify(listTasksToSubmit));
});

let lastAttempt = localStorage.getItem("time_left");
listTasksToSubmit = JSON.parse(localStorage.getItem("lastAnswers"));
if(lastAttempt === null){
    listTasksToSubmit = [];
    localStorage.setItem("lastAnswers", JSON.stringify(listTasksToSubmit))
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
        fillPage();
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
submitOption.addEventListener("click", ()=>{
    sendWarning.classList.remove("display-none");
    smoke.classList.remove("display-none");

});
no.addEventListener("click", ()=>{
    sendWarning.classList.add("display-none");
    smoke.classList.add("display-none");
});

watchResults.addEventListener("click", ()=>{
    feedbackWrapper.classList.add("display-none");
    smoke.classList.add("display-none");
});

yes.addEventListener("click", ()=>{
    sendWarning.classList.add("display-none");
    feedbackWrapper.classList.remove("display-none");
    timerBlock.classList.add("display-none");
    // логика отправки
    fetch(`/api/v1/math/submit/option`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            listTasksToSubmit
        )
    })
        .then(response => response.json())
        .then(data => {
            resultText.innerText = data.score
            console.log(data);
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
    onMain.classList.remove("display-none");
    submitOption.classList.add("display-none");
    clearInterval(timerInterval);
    localStorage.removeItem("time_left");
    localStorage.removeItem("lastAnswers");
    localStorage.removeItem("last_option");
});
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
        yes.click();
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
