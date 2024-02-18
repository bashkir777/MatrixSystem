let moduleNumInput = document.getElementById("module-num")
let choose = document.getElementById("choose");
let taskNum = document.getElementById("task-num");
let taskText = document.getElementById("task-text");
let currentTask = document.getElementById("current-task");

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
                    taskNum.innerText = arrOfNavigationButtons.length;
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


