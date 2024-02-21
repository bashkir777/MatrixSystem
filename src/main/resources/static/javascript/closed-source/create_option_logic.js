let moduleNumInput = document.getElementById("module-num")
let taskNum = document.getElementById("task-num");
let taskText = document.getElementById("task-text");
let scrollPanel = document.getElementById("scroll-panel");

let create = document.getElementById("create");
let smoke = document.getElementById("smoke");
let createdLabel = document.getElementById("created-label");
let optionNum = document.getElementById("option-num");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let scrollLeft = document.getElementById("scroll_left");
let scrollRight = document.getElementById("scroll_right");
let failedToCreateLabel = document.getElementById("failed-to-create-label");
let imageContainer = document.getElementById("image-container");
let add = document.getElementById("add");
let currentTaskId = null;
let currentModuleId = null;
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
let arrOfNavigationButtons = [];
let all_modules_ids = [];
let currentModuleTaskList = [];
let optionToCreate = [];
let currentTaskOrder = null

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

const inputNumHandler = ()=>{
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
                    currentTaskOrder = Number(button.textContent);
                    if(currentTaskOrder === 1){
                        arrowLeft.classList.add("display-none");
                    }else{
                        arrowLeft.classList.remove("display-none");
                    }
                    if(currentTaskOrder === arrOfNavigationButtons.length){
                        arrowRight.classList.add("display-none");
                    }else{
                        arrowRight.classList.remove("display-none");
                    }
                    currentTaskId = task.id;
                    imageContainer.classList.add("display-none");
                    if(task.img !== null){
                        let imgSrc = task.img.replace(/\\/g, '/');
                        imageContainer.src = location.protocol + "//" + location.host + "/" + imgSrc;
                        imageContainer.classList.remove("display-none");
                    }
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
};

moduleNumInput.addEventListener("input", inputNumHandler);
moduleNumInput.value = 1;
inputNumHandler();

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

                    if(task.img !== null){
                        let taskImg = document.createElement('img');
                        let wrapper = document.createElement('div');
                        wrapper.classList.add("text-align-center");
                        let imgSrc = task.img.replace(/\\/g, '/');
                        taskImg.src = location.protocol + "//" + location.host + "/" + imgSrc;
                        taskImg.classList.add("styles-for-proxy-img");
                        wrapper.appendChild(taskImg);
                        taskProxy.appendChild(wrapper);
                    }

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

let notYetIsShow = false;
create.addEventListener("click", ()=>{
    for (let entry of optionToCreate){
        if(entry.taskId === null){
            if(!notYetIsShow){
                notYetIsShow = true;
                let wrapper = document.getElementById("position-wrapper-2");
                wrapper.classList.remove("display-none");
                setTimeout(()=>{
                    wrapper.classList.add("display-none");
                    notYetIsShow = false;
                }, 3000)
            }
            return;
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
        .then(response =>{
            if(response.status === 201){
                response.json().then(data => {
                        console.log(data.optionId);
                        smoke.classList.remove("display-none");
                        createdLabel.classList.remove("display-none");
                        optionNum.innerText = data.optionId;
                    }
                )
            }else{
                smoke.classList.remove("display-none");
                failedToCreateLabel.classList.remove("display-none")
            }

        })

});
