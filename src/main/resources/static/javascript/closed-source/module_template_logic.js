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

let currentTask = 1;
let prevNavigationButton;
let arrOfTaskIds;
function getModuleNumFromUrl() {
    return location.href.split("/").pop();
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
function selectTask(id){
    //логика окрашивания контейнера в зависимости от того какие задания выполнены будет тут
    container.classList.remove("selected-border");
    container.classList.remove("red-border");
    returnCurrentInputElement().value="";
    fetch(`/api/v1/task/${id}`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            taskText.innerText = data.task;
            answerText.innerText = data.answer;
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });

}


// инициализируем массив с id всех заданий модуля
fetch(`/api/v1/module/${getModuleNumFromUrl()}`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    },
})
    .then(response => response.json())
    .then(data => {
        arrOfTaskIds = data;
        navigationButtons[0].click();
        prevNavigationButton = navigationButtons[0];
    })
    .catch((error) => {
        console.error('Ошибка:', error);
    });



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
    navigationButtons[currentTask].click();
    navigationButtonsWrapper.scrollLeft = (45 * (currentTask-1));
})
arrowLeft.addEventListener("click", ()=>{
    navigationButtons[currentTask - 2].click();
    navigationButtonsWrapper.scrollLeft = (45 * (currentTask-1));
})
send.addEventListener("click", () => {
    // объявляем тут, так как поле может различаться от задания с изображением к заданию без него
    // выбираем input в зависимости от того какой тип задания выбран в данный момент
    let inputAnswer = returnCurrentInputElement();


    if(inputAnswer.value === answerText.innerText){
        container.classList.add("selected-border");
        container.classList.remove("red-border");
    }else{
        container.classList.add("red-border");
        container.classList.remove("selected-border");
    }
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
            currentTask = i + 1;
            if(currentTask === 1){
                arrowLeft.classList.add("display-none");
            }else{
                arrowLeft.classList.remove("display-none");
            }
            if(currentTask === navigationButtons.length){
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
