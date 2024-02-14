
let editButtons = document.getElementsByClassName("edit");
let crossButtons = document.getElementsByClassName("cross");
let plusButtons = document.getElementsByClassName("plus-wrapper");
let newSectionForm = document.getElementById("new_section");
let closeAddSection = document.getElementById("close_add_section");
let closeEditSection = document.getElementById("close_edit_section");
let smoke = document.getElementById("smoke");
let submit = document.getElementById("submit-section");
let edit = document.getElementById("edit-section");
let addResourceName = document.getElementById("add-resource-name");
let addResourceLink = document.getElementById("add-resource-link");
let deleteWarning = document.getElementById("delete_warning");
let redBorderTuned = false;

let currentTaskModifyId = null;
let currentTaskModifyName = null;
let currentTaskModifyLink = null;


function addRedBorderToNewSection(){
    if(!redBorderTuned){
        addResourceName.classList.add("red-border");
        addResourceLink.classList.add("red-border");
        newSectionForm.classList.add("red-border");
        addResourceName.value="";
        addResourceLink.value="";
        redBorderTuned = true;
        setTimeout( ()=>{
            console.log("inside")
            addResourceName.classList.remove("red-border");
            addResourceLink.classList.remove("red-border");
            newSectionForm.classList.remove("red-border");
            redBorderTuned = false;
        }, 2000);
    }
}

submit.addEventListener("click", ()=>{
    let name = addResourceName.value;
    let link = addResourceLink.value;

    if(name.length>5 || link.length>5){
        fetch(`/api/v1/management/add/section`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                "name": name,
                "link": link,
                "module": newSectionForm.querySelector("#current_module").textContent
            })
        })
            .then(response => response.text())
            .then(data => {
                console.log(data);
                closeAddSection.click();
                location.reload();
            })
            .catch((error) => {
                console.error('Ошибка:', error);
            });
    }else{
        addRedBorderToNewSection();
    }
});
let plus_clicked = false;
let curPlusClicked = null;

for(let editButton of editButtons){
    editButton.addEventListener("click", ()=>{
        currentTaskModifyId = editButton.querySelectorAll("span")[0].innerText;
        currentTaskModifyName = editButton.querySelectorAll("span")[1].innerText;
        currentTaskModifyLink = editButton.querySelectorAll("span")[2].innerText;
        let currentTaskModifyVisibleForStudent = editButton.querySelectorAll("span")[3].innerText;
        let checkbox = newSectionForm.querySelectorAll("input")[2];
        checkbox.checked = currentTaskModifyVisibleForStudent === "true";
        closeEditSection.classList.remove("display-none");
        closeAddSection.classList.add("display-none");
        newSectionForm.classList.remove("display-none");
        newSectionForm.querySelectorAll("input")[0].value = currentTaskModifyName;
        newSectionForm.querySelectorAll("input")[1].value = currentTaskModifyLink;
        submit.classList.add("display-none");
        edit.classList.remove("display-none");
        smoke.classList.remove("display-none");
    });
}
closeEditSection.addEventListener("click", ()=>{
    smoke.classList.add("display-none");
    newSectionForm.classList.add("display-none");
});

edit.addEventListener("click", ()=>{
    let newName = newSectionForm.querySelectorAll("input")[0].value;
    let newLink = newSectionForm.querySelectorAll("input")[1].value;
    if(newName.length > 5 && newLink.length>5){
        fetch(`/api/v1/management/update/section/${currentTaskModifyId}`, {
            method: 'PATCH',
            headers: {
                'Content-Type': 'application/json'
            },
            body:JSON.stringify({
                "name": newName,
                "link": newLink
            })
        })
            .then(response => response.text())
            .then(data => {
                closeEditSection.click();
                location.reload();
            })
            .catch((error) => {
                console.error('Ошибка:', error);
            });
    }else{
        addRedBorderToNewSection();
    }
});
for(let plus of plusButtons){
    plus.addEventListener("mouseover", ()=>{
        plus.classList.add("rotate-90");
    });
    plus.addEventListener("mouseout", ()=>{
        if(!plus_clicked){
            plus.classList.remove("rotate-90");
        }

    });
    plus.addEventListener("click", () => {
        newSectionForm.querySelectorAll("input")[0].value = "";
        newSectionForm.querySelectorAll("input")[1].value = "";
        closeEditSection.classList.add("display-none");
        closeAddSection.classList.remove("display-none");
        submit.classList.remove("display-none");
        edit.classList.add("display-none");
        plus_clicked = true;
        smoke.classList.remove("display-none");
        plus.querySelector("svg").classList.add("plus-clicked");
        curPlusClicked = plus;
        let id = plus.querySelector('span').innerText;
        newSectionForm.classList.remove("display-none");
        newSectionForm.querySelector("#current_module").textContent = id;
        let checkbox = newSectionForm.querySelectorAll("input")[2];
        checkbox.checked = true;
    });
}

closeAddSection.addEventListener("click", ()=>{
    curPlusClicked.classList.remove("rotate-90");
    plus_clicked = false;
    smoke.classList.add("display-none");
    curPlusClicked.querySelector("svg").classList.remove("plus-clicked");
    newSectionForm.classList.add("display-none");
    newSectionForm.querySelector("#current_module").textContent = null;
    curPlusClicked = null;
});
let noButton = document.getElementById("no");
noButton.addEventListener("click", ()=>{
    smoke.classList.add("display-none");
    deleteWarning.classList.add("display-none");
});

for (let button of crossButtons){
    button.addEventListener("click", () =>{
        let id = button.querySelector("span").textContent;
        smoke.classList.remove("display-none");
        deleteWarning.classList.remove("display-none");
        let yesButton = document.getElementById("yes");
        yesButton.addEventListener("click", ()=>{
            fetch(`/api/v1/management/delete/section/${id}`, {
                method: 'DELETE',
                headers: {
                    'Content-Type': 'application/json'
                },
            })
                .then(response => response.text())
                .then(data => {
                    location.reload();
                })
                .catch((error) => {
                    console.error('Ошибка:', error);
                });
        });
    });
}