let addTask = document.getElementById("add-task");
let smoke = document.getElementById("smoke");
let cross = document.getElementById("cross");
let taskForm = document.getElementById("task-form");
let addTaskButton = document.getElementById("add-task-button");
let condition = document.getElementById("task-condition-form");
let fullAnswer = document.getElementById("task-full-answer-form");
let shortAnswer = document.getElementById("task-answer-form");
let imgForm = document.getElementById("task-img-form");


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
    fetch(`/api/v1/management/add/task/common-pull`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(
            {
                task: condition.value,
                answer: shortAnswer.value,
                solution: fullAnswer.value,
                img: imgForm.value,
                module: getModuleNumFromUrl()
            }
        )
    })
        .then(response =>{
            if(response.status === 201){
                response.text().then(data => {
                            console.log(data);
                    }
                )
            }else{
                console.log("Не удалось создать задание");
            }
        });
});
