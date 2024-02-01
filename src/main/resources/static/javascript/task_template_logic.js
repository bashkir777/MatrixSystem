
let answer = document.getElementById("answer");
let showAnswer = document.getElementById("show_answer");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let container = document.getElementById("container");
let send = document.getElementById("send");
let input_answer = document.getElementsByTagName("input")[0];
//содержимое ответа
let _answer = document.getElementById("_answer");

function getModuleTaskRelationFromUrl(){
    let tempArr = location.href.toString().split("/");
    for (let i = 0; i < tempArr.length; i++){
        if(tempArr[i] === "module"){
            return [tempArr[i+1], tempArr[i+3]]
        }
    }
}

function markCurrentTaskAsCompleted(){
    let completedTasks = JSON.parse(localStorage.getItem("completed_tasks"));
    let tempArr = getModuleTaskRelationFromUrl();
    completedTasks[tempArr[0]].push(tempArr[1]) ;
    localStorage.setItem("completed_tasks", JSON.stringify(completedTasks));
}

function isCurrentTaskCompleted(){
    let completedTasks = JSON.parse(localStorage.getItem("completed_tasks"));
    let tempArr = getModuleTaskRelationFromUrl();
    return completedTasks[tempArr[0]].includes(tempArr[1]);
}

let answerIsShow = false;

//local storage initialization
if(localStorage.getItem("completed_tasks") === null){
    let completedTasks = {
    }
    for (let i = 1; i <= 19; i++){
        completedTasks[i] = [];
    }
    console.log(completedTasks);
    localStorage.setItem("completed_tasks", JSON.stringify(completedTasks));
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