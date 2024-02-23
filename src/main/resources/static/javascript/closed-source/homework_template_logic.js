let homework;
let navigationButtons = document.getElementsByClassName("navigation-button");
let warningComment = document.getElementById("warning_comment");
let showAnswer = document.getElementById("show_answer");
let answer = document.getElementById("answer");
let container = document.getElementById("container");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let scrollLeft = document.getElementById("scroll_left");
let scrollRight = document.getElementById("scroll_right");
let wrongAnswerComment = document.getElementById("wrong_answer_comment");
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
let taskNum = document.getElementById("task-num");
let solution = document.getElementById("full-answer");
let imageContainer = document.getElementById("image-container");
let taskText = document.getElementById("task-text");
let answerText = document.getElementById("_answer");
let send = document.getElementById("send");
let inputAnswer = document.getElementById("answer-input");
let currentTaskId;
let currentTaskOrder;

let answerIsShow = false;
let warning = false;

function getModuleNumFromUrl() {
    return location.href.split("/").pop().split("?")[0];
}



function clearButton(button) {
    button.classList.remove("green-border");
    button.classList.remove("yellow-border");
    button.classList.remove("red-border");
    button.classList.remove("navigation-tab-selected");
}

function clearContainer() {
    container.classList.remove("green-border");
    container.classList.remove("yellow-border");
    container.classList.remove("red-border");
}

function clearButtonBorderAndContainer(button, container) {
    button.classList.remove("green-border");
    button.classList.remove("yellow-border");
    button.classList.remove("red-border");
    container.classList.remove("green-border");
    container.classList.remove("yellow-border");
    container.classList.remove("red-border");
}

function markButtonBasedOnStatus(button, status) {
    if (status === "DONE") {
        button.classList.add("green-border");
    } else if (status === "TRIED") {
        button.classList.add("yellow-border");
    } else if (status === "FAILED") {
        button.classList.add("red-border");
    } else {
        clearButton(button);
    }
}

function markContainerBasedOnStatus(status) {
    clearContainer();
    if (status === "DONE") {
        container.classList.add("green-border");
    } else if (status === "TRIED") {
        container.classList.add("yellow-border");
    } else if (status === "FAILED") {
        container.classList.add("red-border");
    } else {
        clearContainer();
    }
}

function displayInputAndSubmitElementsBasedOnStatus(status) {
    if (status === "DONE" || status === "FAILED") {
        inputAnswer.classList.add("display-none");
        send.classList.add("display-none");
    } else {
        inputAnswer.classList.remove("display-none");
        send.classList.remove("display-none");
    }
}



fetch(`/api/v1/management/homework/${getModuleNumFromUrl()}`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    }
})
    .then(response => response.json())
    .then(data => {
        homework = data;
        for (let i =0; i<navigationButtons.length; i++){
            let button = navigationButtons[i];
            button.addEventListener("click", ()=>{
                if(answerIsShow) showAnswer.click();
                currentTaskOrder = i+1;
                if (currentTaskOrder === 1) {
                    arrowLeft.classList.add("display-none");
                } else {
                    arrowLeft.classList.remove("display-none");
                }
                if (currentTaskOrder === navigationButtons.length) {
                    arrowRight.classList.add("display-none");
                } else {
                    arrowRight.classList.remove("display-none");
                }
                for(let b of navigationButtons){
                    b.classList.remove("navigation-tab-selected");
                }
                button.classList.add("navigation-tab-selected");
                currentTaskId = data[i].id;
                taskNum.innerText = currentTaskOrder;
                taskText.innerText = data[i].task;
                imageContainer.classList.add("display-none");
                inputAnswer.value="";
                if (data[i].img !== null) {
                    let imgSrc = data[i].img.replace(/\\/g, '/');
                    imageContainer.src = location.protocol + "//" + location.host + "/" + imgSrc;
                    imageContainer.classList.remove("display-none");
                }
                fetch(`/api/v1/management/custom-task/status/${data[i].id}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                })
                    .then(response => response.text())
                    .then(status => {
                        clearContainer()
                        clearButton(navigationButtons[currentTaskOrder-1])
                        markButtonBasedOnStatus(navigationButtons[currentTaskOrder-1], status);
                        navigationButtons[currentTaskOrder-1].classList.add("navigation-tab-selected")
                        markContainerBasedOnStatus(status);
                        if(status === "DONE" || status === "FAILED" ){
                            inputAnswer.classList.add("display-none");
                            send.classList.add("display-none");
                        }else{
                            inputAnswer.classList.remove("display-none");
                            send.classList.remove("display-none");
                        }
                    })
                    .catch((error) => {
                        console.error('Ошибка:', error);
                    });
            });

        }
        for (let i =0; i<navigationButtons.length; i++){
            let button = navigationButtons[i];
            console.log(button)
            markButtonBasedOnStatus(button, data[i].status);
        }
        navigationButtons[0].click();
    })
    .catch((error) => {
    console.error('Ошибка:', error);
});
let wrongCommentShow = false;
send.addEventListener("click", ()=>{
    fetch(`/api/v1/management/submit/custom-task`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            {
                id: currentTaskId,
                answer: inputAnswer.value
            }
        )
    })
        .then(response => response.text())
        .then(data => {
            if(data==="DONE"){
                inputAnswer.classList.add("display-none");
                send.classList.add("display-none");
            }

            if(data === "TRIED"){
                if(!wrongCommentShow){
                    wrongCommentShow = true;
                    wrongAnswerComment.classList.remove("display-none");
                    setTimeout(()=>{
                        wrongAnswerComment.classList.add("display-none");
                        wrongCommentShow = false;
                    }, 3000);
                }
            }
            for( let task of homework){
                if(task.id === currentTaskId){
                    task.status = data;
                }
            }
            clearContainer()
            clearButton(navigationButtons[currentTaskOrder-1])
            markButtonBasedOnStatus(navigationButtons[currentTaskOrder-1], data);
            navigationButtons[currentTaskOrder-1].classList.add("navigation-tab-selected")
            markContainerBasedOnStatus(data);
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
});


if (arrowLeft !== null){
    arrowLeft.addEventListener("mouseover", ()=>{
        container.classList.add("rotate-1-left");
    });
    arrowLeft.addEventListener("mouseout", ()=>{
        container.classList.remove("rotate-1-left");
    });
}
arrowRight.addEventListener("click", ()=>{
    if(currentTaskOrder !== navigationButtons.length){
        navigationButtons[currentTaskOrder].click();
    }
});
arrowLeft.addEventListener("click", () =>{
    if(currentTaskOrder !== 1){
        navigationButtons[currentTaskOrder - 2].click();
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


showAnswer.addEventListener("click", () => {
    if (!answerIsShow) {
        if (!warning) {
            let xhr = new XMLHttpRequest();
            xhr.open('GET', `/api/v1/management/custom-task/status/${currentTaskId}`, false); // false означает синхронный запрос
            xhr.setRequestHeader('Content-Type', 'application/json');
            try {
                xhr.send();
                if (xhr.status === 200) {
                    let data = xhr.responseText;
                    if (data === "TRIED" || data === "NONE") {
                        warning = true;
                        warningComment.classList.remove("display-none");
                        warningComment.classList.add("opacity-1");
                        setTimeout(() => {
                            warningComment.classList.remove("opacity-1");
                            warningComment.classList.add("opacity-0");
                        }, 5000);
                        return;
                    }
                } else {
                    console.error('Ошибка:', xhr.status);
                }
            } catch (error) {
                console.error('Ошибка:', error);
            }
        }
        answerIsShow = true;
        fetch(`/api/v1/management/custom-task/${currentTaskId}/answer`, {
            method: 'GET',
            headers: {
                'Content-Type': 'application/json'
            },
        })
            .then(response => response.json())
            .then(data => {
                answer.classList.remove("display-none");
                answerText.innerText = data.answer;
                solution.innerText = data.solution;
                fetch(`/api/v1/management/custom-task/status/${currentTaskId}`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(response => response.text())
                    .then(data => {
                        clearContainer()
                        clearButton(navigationButtons[currentTaskOrder-1])
                        markButtonBasedOnStatus(navigationButtons[currentTaskOrder-1], data);
                        navigationButtons[currentTaskOrder-1].classList.add("navigation-tab-selected")
                        markContainerBasedOnStatus(data);
                        if(data === "FAILED"){
                            inputAnswer.classList.add("display-none");
                            send.classList.add("display-none");
                        }

                    }).catch((error) => {
                    console.error('Ошибка:', error);
                });
            })
            .catch((error) => {
                console.error('Ошибка:', error);
            });
        showAnswer.textContent = "Скрыть ответ";
    } else {
        answerIsShow = false;
        answer.classList.add("display-none");
        showAnswer.textContent = "Показать ответ";
    }
});

