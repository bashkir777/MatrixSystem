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
let solution = document.getElementById("full-answer");
let imageContainer = document.getElementById("image-container");

let navigationButtons = document.getElementsByClassName("navigation-button");
let container = document.getElementById("container");
let answer = document.getElementById("answer");
let inputAnswer = document.getElementById("answer-input");
let warningComment = document.getElementById("warning_comment");
let wrongAnswerComment = document.getElementById("wrong_answer_comment");
let taskNum = document.getElementById("task-num");
let moduleNum = document.getElementById("current-task");
let warning = false;
let currentTaskId;
let prevNavigationButton;
let arrOfTaskIds;
let currentTaskNum;


document.getElementById("all-tasks-navigation-tab").classList.add("mark-header-tab-as-selected");

function getModuleNumFromUrl() {
    return location.href.split("/").pop().split("?")[0];
}

function getCurrentButton() {
    return navigationButtons[getCurrentTaskOrdinalNumberById(currentTaskId) - 1];
}

function clearButton(button) {
    button.classList.remove("green-border");
    button.classList.remove("yellow-border");
    button.classList.remove("red-border");
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
    clearButton(getCurrentButton());
    if (status === "DONE") {
        button.classList.add("green-border");
    } else if (status === "TRIED") {
        button.classList.add("yellow-border");
    } else if (status === "FAILED") {
        button.classList.add("red-border");
        warning = false;
    } else {
        clearButton(button);
        clearContainer();
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

function selectTask(id) {
    currentTaskId = id;

    if (answerIsShow) showAnswer.click();
    // запрашиваем статус задания и красим в зависимости от него
    fetch(`/api/v1/client/task/${id}/status`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.text())
        .then(data => {
            if (data === "FAILED" && !answerIsShow) {
                showAnswer.click();
            }
            markButtonBasedOnStatus(getCurrentButton(), data);
            clearContainer();
            markContainerBasedOnStatus(data);
            displayInputAndSubmitElementsBasedOnStatus(data);
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });

    inputAnswer.value = "";

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
            imageContainer.classList.add("display-none");

            if (data.img !== null) {
                let imgSrc = data.img.replace(/\\/g, '/');
                imageContainer.src = location.protocol + "//" + location.host + "/" + imgSrc;
                imageContainer.classList.remove("display-none");
            }

        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });

}

function getCurrentTaskOrdinalNumberById(id) {
    for (let i = 0; i < arrOfTaskIds.length; i++) {
        if (arrOfTaskIds[i] === id) {
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
        currentTaskNum = 1;
        taskNum.innerText = currentTaskNum;
        prevNavigationButton = navigationButtons[0];
        for (let i = 0; i < navigationButtons.length; i++) {
            navigationButtons[i].addEventListener("click",
                () => {
                    currentTaskId = arrOfTaskIds[i];
                    currentTaskNum = i + 1;
                    taskNum.innerText = currentTaskNum;
                    // если это первое по порядку задание то гасим левую стрелку
                    // иначе включаем
                    if (currentTaskId === arrOfTaskIds[0]) {
                        arrowLeft.classList.add("display-none");
                    } else {
                        arrowLeft.classList.remove("display-none");
                    }
                    // если это последнее по порядку задание то гасим правую стрелку
                    // иначе включаем
                    if (currentTaskId === arrOfTaskIds[navigationButtons.length - 1]) {
                        arrowRight.classList.add("display-none");
                    } else {
                        arrowRight.classList.remove("display-none");
                    }
                    if (prevNavigationButton !== undefined) {
                        prevNavigationButton.classList.remove("navigation-tab-selected");
                    }
                    navigationButtons[i].classList.add("navigation-tab-selected");
                    selectTask(arrOfTaskIds[i]);
                    prevNavigationButton = navigationButtons[i];
                })
        }
        markTasks();
    })
    .catch((error) => {
        console.error('Ошибка:', error);
    });

let moduleName;
let taskVerifiable;
fetch(`/api/v1/client/module/${getModuleNumFromUrl()}/info`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    },
})
    .then(response => response.json())
    .then(data => {
        taskVerifiable = data.verifiable;
        if (!data.verifiable) {
            send.innerText = "Пометить решенным"
            inputAnswer.classList.add("display-none-important");
        }
        moduleName = data.name;
    })
    .catch((error) => {
        console.error('Ошибка:', error);
    });


document.getElementById("num").innerText = getModuleNumFromUrl();
moduleNum.setAttribute("href", "/app/theory#task_" + getModuleNumFromUrl());


function markTasks() {
    fetch(`/api/v1/client/module/${getModuleNumFromUrl()}/tasks/statuses`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        },
    })
        .then(response => response.json())
        .then(data => {
            for (let i = 0; i < arrOfTaskIds.length; i++) {
                let status = data[arrOfTaskIds[i]];
                markButtonBasedOnStatus(navigationButtons[i], status);
            }
            navigationButtons[0].click();
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
}


showAnswer.addEventListener("click", () => {
    if (!answerIsShow) {

        if (!warning) {
            let xhr = new XMLHttpRequest();
            xhr.open('GET', `/api/v1/client/task/${currentTaskId}/status`, false); // false означает синхронный запрос
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
        fetch(`/api/v1/client/task/${currentTaskId}/answer`, {
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
                fetch(`/api/v1/client/task/${currentTaskId}/status`, {
                    method: 'GET',
                    headers: {
                        'Content-Type': 'application/json'
                    }
                }).then(response => response.text())
                    .then(data => {
                        markButtonBasedOnStatus(getCurrentButton(), data);
                        clearContainer();
                        markContainerBasedOnStatus(data);
                        displayInputAndSubmitElementsBasedOnStatus(data);
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
if (arrowLeft !== null) {
    arrowLeft.addEventListener("mouseover", () => {
        container.classList.add("rotate-1-left");
    });
    arrowLeft.addEventListener("mouseout", () => {
        container.classList.remove("rotate-1-left");
    });
}
if (arrowRight !== null) {
    arrowRight.addEventListener("mouseover", () => {
        container.classList.add("rotate-1-right");
    });
    arrowRight.addEventListener("mouseout", () => {
        container.classList.remove("rotate-1-right");
    });
}
arrowRight.addEventListener("click", () => {
    let ordinalNumber = getCurrentTaskOrdinalNumberById(currentTaskId);
    navigationButtons[ordinalNumber].click();
    navigationButtonsWrapper.scrollLeft = (45 * (ordinalNumber - 1));
});

arrowLeft.addEventListener("click", () => {
    let ordinalNumber = getCurrentTaskOrdinalNumberById(currentTaskId);
    navigationButtons[ordinalNumber - 2].click();
    navigationButtonsWrapper.scrollLeft = (45 * (ordinalNumber - 1));
});

send.addEventListener("click", () => {
    // объявляем тут, так как поле может различаться от задания с изображением к заданию без него
    // выбираем input в зависимости от того какой тип задания выбран в данный момент

    fetch(`/api/v1/client/task/submit`, {
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
            let currentButton = getCurrentButton();
            clearContainer();
            clearButton(currentButton);
            if (data === "DONE") {
                currentButton.classList.add("green-border");
                container.classList.add("green-border");
            } else {
                currentButton.classList.add("yellow-border");
                container.classList.add("yellow-border");
                wrongAnswerComment.classList.remove("display-none");
                wrongAnswerComment.classList.add("opacity-1");
                setTimeout(() => {
                    wrongAnswerComment.classList.remove("opacity-1");
                    wrongAnswerComment.classList.add("opacity-0");
                }, 5000);
            }
            displayInputAndSubmitElementsBasedOnStatus(data);
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
});
scrollLeft.addEventListener("click", () => {
    navigationButtonsWrapper.scrollLeft -= 200;
});
scrollRight.addEventListener("click", function () {
    navigationButtonsWrapper.scrollLeft += 200;
});



function wheelHandler(event) {
    //если это тачпад ничего не делаем он и так хорошо работает
    if (event.deltaX !== 0) {
        return;
    }
    event.preventDefault();
    navigationButtonsWrapper.scrollLeft += event.deltaY * 2;
}

navigationButtonsWrapper.addEventListener("wheel", wheelHandler);


moduleNum.addEventListener("mouseover", () => {
    document.getElementById("name").innerText = moduleName;
})
moduleNum.addEventListener("mouseout", () => {
    document.getElementById("name").innerText = "";
})