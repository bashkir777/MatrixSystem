let addTask = document.getElementById("add-task");
let smoke = document.getElementById("smoke");
let cross = document.getElementById("cross");
let taskForm = document.getElementById("task-form");
let addTaskButton = document.getElementById("add-task-button");
let condition = document.getElementById("task-condition-form");
let fullAnswer = document.getElementById("task-full-answer-form");
let shortAnswer = document.getElementById("task-answer-form");
let imgForm = document.getElementById("task-img-form");
let taskImg = document.getElementById("task-img");
let createdLabel = document.getElementById("created-label");
let failedToCreateLabel = document.getElementById("failed-to-create-label");

addTask.addEventListener("click", ()=>{
    smoke.classList.remove("display-none");
    taskForm.classList.remove("display-none");
    cross.classList.remove("display-none");
    addTaskButton.classList.remove("display-none");
})

cross.addEventListener("click", ()=>{
    smoke.classList.add("display-none");
    taskForm.classList.add("display-none");
    cross.classList.add("display-none");
    addTaskButton.classList.add("display-none");
})
function getModuleNumFromUrl() {
    return location.href.split("/").pop().split("?")[0];
}

addTaskButton.addEventListener("click", ()=>{


    const formData = new FormData();
    if(imgForm.files.length === 0){
        formData.append('image', null);
    }else{
        formData.append('image', imgForm.files[0]);
    }

    formData.append('task', condition.value);
    formData.append('answer', shortAnswer.value);
    formData.append('solution', fullAnswer.value);
    formData.append('module', getModuleNumFromUrl());

    fetch(`/api/v1/management/add/task/common-pull`, {
        method: 'POST',
        body: formData,
    })
        .then(response =>{
            taskForm.classList.add("display-none");
            cross.classList.add("display-none");
            addTaskButton.classList.add("display-none");
            if(response.status === 201){
                response.text().then(data => {
                        createdLabel.classList.remove("display-none");
                    }
                )
            }else{
                failedToCreateLabel.classList.remove("display-none");
            }
        });
});
