let addTask = document.getElementById("add-task");
let smoke = document.getElementById("smoke");
let cross = document.getElementById("cross");
let taskForm = document.getElementById("task-form");
let addTaskButton = document.getElementById("add-task-button");

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
