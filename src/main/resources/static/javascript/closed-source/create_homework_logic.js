let addTaskButton = document.getElementById("add-task");
let condition = document.getElementById("task-condition-form");
let fullAnswer = document.getElementById("task-full-answer-form");
let shortAnswer = document.getElementById("task-answer-form");
let imgForm = document.getElementById("task-img-form");

let tasksIDs = []

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
                })
            }else{
                console.log("не удалось создать задание")
            }
        });
})
