let answerIsShow = false;
let showAnswer = document.getElementById("show_answer");
let send = document.getElementById("send");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let scrollLeft = document.getElementById("scroll_left");
let scrollRight = document.getElementById("scroll_right");
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
let taskText = document.getElementById("task-text");
let answerText = document.getElementById("_answer");
let navigationButtons = document.getElementsByClassName("navigation-button");
let container = document.getElementById("container");
let answer = document.getElementById("answer");
let taskWithImage = document.getElementById("task-with-img");
let taskWithoutImage = document.getElementById("task-without-img");

let currentTaskId;
let prevNavigationButton;
let arrOfTaskIds;

function getModuleNumFromUrl() {
    return location.href.split("/").pop().split("?")[0];
}

function getCurrentButton(){
    return navigationButtons[getCurrentTaskOrdinalNumberById(currentTaskId) - 1];
}

function clearButton(button){
    button.classList.remove("green-border");
    button.classList.remove("yellow-border");
    button.classList.remove("red-border");
}
function clearContainer(){
    container.classList.remove("green-border");
    container.classList.remove("yellow-border");
    container.classList.remove("red-border");
}

function clearButtonBorderAndContainer(button, container){
    button.classList.remove("green-border");
    button.classList.remove("yellow-border");
    button.classList.remove("red-border");
    container.classList.remove("green-border");
    container.classList.remove("yellow-border");
    container.classList.remove("red-border");
}

function returnCurrentInputElement(){
    let inputAnswer;
    if(taskWithImage.classList.contains("display-none")){
        inputAnswer = document.getElementsByTagName("input")[1];
    }else{
        inputAnswer = document.getElementsByTagName("input")[0];
    }
    return inputAnswer
}

function markButtonBasedOnStatus(button, status){
    clearButton(getCurrentButton());
    if(status === "DONE"){
        button.classList.add("green-border");
    }else if (status === "TRIED"){
        button.classList.add("yellow-border");
    }else if (status === "FAILED"){
        button.classList.add("red-border");
    }else{
        clearButton(button);
        clearContainer();
    }
}
function markContainerBasedOnStatus(status){
    clearContainer();
    if(status === "DONE"){
        container.classList.add("green-border");
    }else if (status === "TRIED"){
        container.classList.add("yellow-border");
    }else if (status === "FAILED"){
        container.classList.add("red-border");
    }else{
        clearContainer();
    }
}
function selectTask(id){
    currentTaskId = id;


    // запрашиваем статус задания и красим в зависимости от него
    fetch(`/api/v1/client/task/${id}/status`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.text())
        .then(data => {
            console.log(data);
            markButtonBasedOnStatus(getCurrentButton(), data);
            clearContainer();
            markContainerBasedOnStatus(data);
            console.log(data);
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });

    returnCurrentInputElement().value="";

    // получаем и используем содержимое задания
    fetch(`/api/v1/client/task/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            currentTaskId = id;
            taskText.innerText = data.task;
            answerText.innerText = data.answer;
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });

}

function getCurrentTaskOrdinalNumberById(id){
    for (let i = 0; i < arrOfTaskIds.length; i++){
        if (arrOfTaskIds[i] === id){
            return i + 1;
        }
    }
    return null;
}
// инициализируем массив с id всех заданий модуля
fetch(`/api/v1/client/module/${getModuleNumFromUrl()}`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    },
})
    .then(response => response.json())
    .then(data => {
        arrOfTaskIds = data;
        currentTaskId = arrOfTaskIds[0];
        navigationButtons[0].click();
        prevNavigationButton = navigationButtons[0];
    })
    .catch((error) => {
        console.error('Ошибка:', error);
    });


function markTasks(){
    console.log(`/api/v1/client/module/${getModuleNumFromUrl()}/tasks/statuses`);
    fetch(`/api/v1/client/module/${getModuleNumFromUrl()}/tasks/statuses`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            for(let i = 0; i < arrOfTaskIds.length; i ++){
                let status = data[arrOfTaskIds[i]];
                markButtonBasedOnStatus(navigationButtons[i], status);

            }
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
}
markTasks();

showAnswer.addEventListener("click", ()=>{
    if (!answerIsShow){
        answerIsShow = true;
        answer.classList.remove("display-none");
        showAnswer.textContent = "Скрыть ответ";
    }else{
        answerIsShow = false;
        answer.classList.add("display-none");
        showAnswer.textContent = "Показать ответ";
    }
});
if (arrowLeft !== null){
    arrowLeft.addEventListener("mouseover", ()=>{
        container.classList.add("rotate-1-left");
    });
    arrowLeft.addEventListener("mouseout", ()=>{
        container.classList.remove("rotate-1-left");
    });
}
if (arrowRight !== null){
    arrowRight.addEventListener("mouseover", ()=>{
        container.classList.add("rotate-1-right");
    });
    arrowRight.addEventListener("mouseout", ()=>{
        container.classList.remove("rotate-1-right");
    });
}
arrowRight.addEventListener("click", ()=>{
    let ordinalNumber = getCurrentTaskOrdinalNumberById(currentTaskId);
    navigationButtons[ordinalNumber].click();
    navigationButtonsWrapper.scrollLeft = (45 * (ordinalNumber-1));
});

arrowLeft.addEventListener("click", ()=>{
    let ordinalNumber = getCurrentTaskOrdinalNumberById(currentTaskId);
    navigationButtons[ordinalNumber - 2].click();
    navigationButtonsWrapper.scrollLeft = (45 * (ordinalNumber-1));
});

send.addEventListener("click", () => {
    // объявляем тут, так как поле может различаться от задания с изображением к заданию без него
    // выбираем input в зависимости от того какой тип задания выбран в данный момент
    let inputAnswer = returnCurrentInputElement();
    fetch(`/api/v1/client/submit/task`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            id: currentTaskId,
            answer: inputAnswer.value
        })
    })
        .then(response => response.text())
        .then(data => {
            console.log(data)
            let currentButton = getCurrentButton();
            if(data === "DONE"){
                currentButton.classList.add("green-border");
                container.classList.add("green-border");
            }else {
                currentButton.classList.add("yellow-border");
                container.classList.add("yellow-border");
            }
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
    // if(inputAnswer.value === answerText.innerText){
    //     container.classList.add("selected-border");
    //     container.classList.remove("red-border");
    // }else{
    //     container.classList.add("red-border");
    //     container.classList.remove("selected-border");
    // }
});
scrollLeft.addEventListener("click", () => {
    navigationButtonsWrapper.scrollLeft -= 200;
});
scrollRight.addEventListener("click", function() {
    navigationButtonsWrapper.scrollLeft += 200;
});

for (let i = 0; i < navigationButtons.length; i++){
    navigationButtons[i].addEventListener("click",
        () => {
            currentTaskId = arrOfTaskIds[i];
            // если это первое по порядку задание то гасим левую стрелку
            // иначе включаем
            if(currentTaskId === arrOfTaskIds[0]){
                arrowLeft.classList.add("display-none");
            }else{
                arrowLeft.classList.remove("display-none");
            }
            // если это последнее по порядку задание то гасим правую стрелку
            // иначе включаем
            if(currentTaskId === arrOfTaskIds[navigationButtons.length-1]){
                arrowRight.classList.add("display-none");
            }else{
                arrowRight.classList.remove("display-none");
            }
            if (prevNavigationButton !== undefined){
                prevNavigationButton.classList.remove("navigation-tab-selected");
            }
            navigationButtons[i].classList.add("navigation-tab-selected");
            selectTask(arrOfTaskIds[i]);
            prevNavigationButton = navigationButtons[i];
        })
}


function wheelHandler(event) {
    //если это тачпад ничего не делаем он и так хорошо работает
    if(event.deltaX !== 0){
        return;
    }
    event.preventDefault();
    navigationButtonsWrapper.scrollLeft += event.deltaY * 2;
}

navigationButtonsWrapper.addEventListener("wheel", wheelHandler);

