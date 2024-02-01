
let answer = document.getElementById("answer");
let showAnswer = document.getElementById("show_answer");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let container = document.getElementById("container");
let send = document.getElementById("send");
let input_answer = document.getElementsByTagName("input")[0];
//содержимое ответа
let _answer = document.getElementById("_answer");
let taskId = document.getElementById("task_id");

function getCurrentTaskNumFromUrl(){
    return location.href.split("/").pop();
}
let taskTab = document.getElementById("tab_"+getCurrentTaskNumFromUrl());
taskTab.classList.add("no-transition");
taskTab.classList.add("navigation-tab-selected");
taskTab.classList.remove("no-transition");

function markCurrentTaskAsCompleted(){
    let completedTasks = JSON.parse(localStorage.getItem("completed_tasks"));
    completedTasks.push(taskId.innerText);
    localStorage.setItem("completed_tasks", JSON.stringify(completedTasks));
}

function isCurrentTaskCompleted(){
    let completedTasks = JSON.parse(localStorage.getItem("completed_tasks"));
    return completedTasks.includes(taskId.innerText);
}

let answerIsShow = false;

//local storage initialization
if(localStorage.getItem("completed_tasks") === null){
    localStorage.setItem("completed_tasks", JSON.stringify([]));
}

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

if (isCurrentTaskCompleted()) container.classList.add("selected-border");

console.log(isCurrentTaskCompleted());
send.addEventListener("click", () => {
    if(input_answer.value === _answer.innerText){
        markCurrentTaskAsCompleted();
        container.classList.add("selected-border");
        if (answerIsShow) showAnswer.click();
    }else{
        container.classList.add("red-border");
        container.classList.remove("selected-border");
        setTimeout( () => {
            container.classList.remove("red-border");
            input_answer.value = "";
            input_answer.setAttribute("value", "");
        }, 3000);
    }
});