
let answer = document.getElementById("answer");

let showAnswer = document.getElementById("show_answer");

let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let container = document.getElementById("container");

let answerIsShow = false;
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
