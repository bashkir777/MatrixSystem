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
let deleteTask = document.getElementById("delete_task");
let failedToDeleteLabel = document.getElementById("failed_to_delete_comment");

let noCondition = document.getElementById("no_condition");
let noShortAnswer = document.getElementById("no_short_answer");
let noSolution = document.getElementById("no_solution");

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

function postTask(verifiable){
    const formData = new FormData();
    if(imgForm.files.length === 0){
        formData.append('image', null);
    }else{
        formData.append('image', imgForm.files[0]);
    }
    if(condition.value === ""){
        noCondition.classList.remove("display-none");
        setTimeout(()=>{
            noCondition.classList.add("display-none");
        }, 3000);
        return;
    }
    if(verifiable && shortAnswer.value === ""){
        noShortAnswer.classList.remove("display-none");
        setTimeout(()=>{
            noShortAnswer.classList.add("display-none");
        }, 3000);
        return;
    }
    if(!verifiable && ( shortAnswer.value === "" && fullAnswer.value === "")){
        noSolution.classList.remove("display-none");
        setTimeout(()=>{
            noSolution.classList.add("display-none");
        }, 3000);
        return;
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
}
addTaskButton.addEventListener("click", ()=>{
    fetch(`/api/v1/client/module/${getModuleNumFromUrl()}/info`, {
        method: 'GET'
    }).then(response => response.json())
        .then(data =>{
            postTask(data.verifiable);
        });
});
let failedToDeleteLabelShowing = false;
deleteTask.addEventListener("click", ()=>{
    fetch(`/api/v1/management/delete/task/${currentTaskId}`, {
        method: 'DELETE'
    })
        .then(response => {if(response.status === 204){
            location.reload()
    }else{
            if(!failedToDeleteLabelShowing){
                failedToDeleteLabelShowing = true;
                failedToDeleteLabel.classList.remove("display-none");
                setTimeout(()=>{
                    failedToDeleteLabel.classList.add("display-none");
                    failedToDeleteLabelShowing = false;
                }, 3000)
            }
        }});
});