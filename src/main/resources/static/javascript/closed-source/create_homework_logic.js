let addTaskButton = document.getElementById("add-task");
let condition = document.getElementById("task-condition-form");
let fullAnswer = document.getElementById("task-full-answer-form");
let shortAnswer = document.getElementById("task-answer-form");
let imgForm = document.getElementById("task-img-form");
let scrollPanel = document.getElementById("scroll-panel");
let createHomework = document.getElementById("create-homework");

let tasksIDs = [];
let homeworkList = [];
function addTaskToHomeworkList(id){

    fetch(`/api/v1/management/custom-task/${id}`, {
        method: 'GET'
    })
        .then(response => response.json())
        .then(task=>{
            homeworkList.push(task);
            spawnTasks();
        });

}
function spawnTasks(){
    while(scrollPanel.firstChild){
        scrollPanel.removeChild(scrollPanel.firstChild);
    }

    for(let i = 1; i<= homeworkList.length; i++){
        let taskProxy = document.createElement('div');
        taskProxy.id = "task_" + i;
        taskProxy.classList.add("task-proxy");

        let moduleNum = document.createElement('div');
        moduleNum.classList.add("module-num");
        moduleNum.innerText = "Задание №"+ i;
        taskProxy.appendChild(moduleNum);

        let text = document.createElement('div');
        text.classList.add("text");
        text.innerText = homeworkList[i-1].task;
        taskProxy.appendChild(text);

        if(homeworkList[i-1].img !== null){
            let taskImg = document.createElement('img');
            let wrapper = document.createElement('div');
            wrapper.classList.add("text-align-center");
            let imgSrc = homeworkList[i-1].img.replace(/\\/g, '/');
            taskImg.src = location.protocol + "//" + location.host + "/" + imgSrc;
            taskImg.classList.add("styles-for-proxy-img");
            wrapper.appendChild(taskImg);
            taskProxy.appendChild(wrapper);
        }

        let div = document.createElement('div');
        div.classList.add("text-align-right");
        let deleteButton = document.createElement('div');
        deleteButton.classList.add("delete");
        deleteButton.id = "delete_" + homeworkList[i-1].id;
        deleteButton.innerText = "Удалить";
        div.appendChild(deleteButton);

        deleteButton.addEventListener("click", ()=>{
            scrollPanel.removeChild(taskProxy);

            tasksIDs = tasksIDs.filter(function(item) {
                return Number(item) !== homeworkList[i-1].id;
            });
            homeworkList = homeworkList.filter((item)=>{
                return item.id !== homeworkList[i-1].id;
            })
            spawnTasks();
        });

        taskProxy.appendChild(div);

        scrollPanel.appendChild(taskProxy);

    }
}
addTaskButton.addEventListener("click", ()=>{

    const formData = new FormData();

    let verifiable;
    if(fullAnswer.value === "" && shortAnswer.value === ""){
        console.log("У задания не может не быть ни краткого ответа ни полного решения одновременно");
        return;
    }else verifiable = fullAnswer.value === "";

    if(imgForm.files.length === 0){
        formData.append('image', null);
    }else{
        formData.append('image', imgForm.files[0]);
        imgForm.value = "";
    }
    formData.append('verifiable', verifiable);
    formData.append('task', condition.value);
    formData.append('answer', shortAnswer.value);
    formData.append('solution', fullAnswer.value);


    fetch(`/api/v1/management/add/task/custom-pull`, {
        method: 'POST',
        body: formData
    })
        .then(response =>{
            if(response.status === 201){
                response.text().then(data => {
                    tasksIDs.push(data);
                    addTaskToHomeworkList(data);
                })
            }else{
                console.log("не удалось создать задание")
            }
        });
})
createHomework.addEventListener("click", ()=>{
    fetch(`/api/v1/management/create/homework`, {
        method: 'POST',
        body: JSON.stringify(tasksIDs),
        headers: {
            'Content-Type': 'application/json'
        }
    })
        .then(response =>{
            if(response.status === 201){
                console.log("ДЗ спешно создано");
            }else{
                console.log("не удалось создать ДЗ");
            }
        });
});