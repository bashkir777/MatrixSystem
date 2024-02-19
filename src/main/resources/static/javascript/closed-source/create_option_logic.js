let moduleNumInput = document.getElementById("module-num")
let choose = document.getElementById("choose");
let taskNum = document.getElementById("task-num");
let taskText = document.getElementById("task-text");
let scrollPanel = document.getElementById("scroll-panel");
let currentTask = document.getElementById("current-task");
let create = document.getElementById("create");
let add = document.getElementById("add");
let currentTaskId = null;
let currentModuleId = null;
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
let arrOfNavigationButtons = [];
let all_modules_ids = [];
let currentModuleTaskList = [];
let optionToCreate = [];

fetch(`/api/v1/client/modules`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    }
})
    .then(response => response.json())
    .then(data => {
        all_modules_ids = data;
        for(let moduleId of data){
            optionToCreate.push(
                {
                    id: moduleId,
                    taskId: null
                }
            );
        }
        moduleNumInput.setAttribute("min", data[0]);
        moduleNumInput.setAttribute("max", data[data.length-1]);
    })
    .catch((error) => {
        console.error('Ошибка:', error);
    });

choose.addEventListener("click", (event)=>{
    let module = moduleNumInput.value;
    fetch(`/api/v1/client/module/${module}/tasks`, {
        method: 'GET',
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response => response.json())
        .then(data => {
            currentModuleId = module;
            currentTask.innerText = module;
            currentModuleTaskList = data;
            let num = 1;
            arrOfNavigationButtons = [];
            while (navigationButtonsWrapper.firstChild) {
                navigationButtonsWrapper.removeChild(navigationButtonsWrapper.firstChild);
            }
            for(let task of data){

                let button = document.createElement('span');
                arrOfNavigationButtons.push(button);
                button.classList.add("navigation-button");
                button.textContent = num;
                button.id = "tab_"+num;
                navigationButtonsWrapper.appendChild(button)
                button.addEventListener("click", ()=>{
                    currentTaskId = task.id;
                    taskNum.innerText = button.textContent;
                    for(let b of arrOfNavigationButtons){
                        b.classList.remove("navigation-tab-selected");
                    }
                    button.classList.add("navigation-tab-selected");
                    taskText.innerText = task.task;
                });
                num ++;
            }
            arrOfNavigationButtons[0].click();
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
});
choose.click();
let comment = document.getElementById("comment-wrapper");
let moduleNumComment = document.getElementById("module-num-comment");
let commentShowing = false;
add.addEventListener("click", () => {
    for (let entry of optionToCreate) {
        if (entry.id === Number(currentModuleId)) {

            if(entry.taskId !== null){
                if(!commentShowing){
                    commentShowing = true;
                    comment.classList.remove("display-none");
                    moduleNumComment.innerText = currentModuleId;
                    setTimeout(()=>{
                        commentShowing = false;
                        comment.classList.add("display-none");
                    },4000)
                }
                break;
            }

            entry.taskId = currentTaskId;

            for(let task of currentModuleTaskList){
                if (task.id === currentTaskId){
                    let taskProxy = document.createElement('div');
                    taskProxy.id = "task_" + currentModuleId;
                    taskProxy.classList.add("task-proxy");

                    let moduleNum = document.createElement('div');
                    moduleNum.classList.add("module-num");
                    moduleNum.innerText = "Задание №"+ currentModuleId;
                    taskProxy.appendChild(moduleNum);

                    let text = document.createElement('div');
                    text.classList.add("text");
                    text.innerText = task.task;
                    taskProxy.appendChild(text);

                    let div = document.createElement('div');
                    div.classList.add("text-align-right");
                    let deleteButton = document.createElement('div');
                    deleteButton.classList.add("delete");
                    deleteButton.id = "delete_" + task.id;
                    deleteButton.innerText = "Удалить";
                    div.appendChild(deleteButton);

                    deleteButton.addEventListener("click", ()=>{
                        scrollPanel.removeChild(taskProxy);
                        entry.taskId = null;
                    });

                    taskProxy.appendChild(div);

                    scrollPanel.appendChild(taskProxy);

                    let sortedChildren = Array.from(scrollPanel.children).sort((a, b) => {
                        let idA = parseInt(a.getAttribute('id').split('_')[1]);
                        let idB = parseInt(b.getAttribute('id').split('_')[1]);
                        return idA - idB;
                    });

                    while (scrollPanel.firstChild){
                        scrollPanel.removeChild(scrollPanel.firstChild);
                    }
                    for(let child of sortedChildren){
                        scrollPanel.appendChild(child);
                    }
                }
            }
            break;
        }
    }
});

create.addEventListener("click", ()=>{
    for (let entry of optionToCreate){
        if(entry.taskId === null){
            console.log("не все задания добавлены")
            break;
        }
    }
    fetch(`/api/v1/management/add/option`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            optionToCreate
        )
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .catch((error) => {
            console.error('Ошибка:', error);
        });
});
