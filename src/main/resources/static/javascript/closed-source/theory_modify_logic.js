
let editButtons = document.getElementsByClassName("edit");
let crossButtons = document.getElementsByClassName("cross");
let plusButtons = document.getElementsByClassName("plus-wrapper");
let newSectionForm = document.getElementById("new_section");
let closeAddSection = document.getElementById("close_add_section");
let smoke = document.getElementById("smoke");
let submit = document.getElementById("submit-section");



submit.addEventListener("mouseover", ()=>{
    newSectionForm.classList.add("blue-border");
});
submit.addEventListener("mouseout", ()=>{
    newSectionForm.classList.remove("blue-border");
});

let plus_clicked = false;
let curPlusClicked = null;
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
        plus_clicked = true;
        smoke.classList.remove("display-none");
        plus.querySelector("svg").classList.add("plus-clicked");
        curPlusClicked = plus;
        let id = plus.querySelector('span').innerText;
        newSectionForm.classList.remove("display-none");
        newSectionForm.querySelector("#current_module").textContent = id;
        console.log('Value of m.id:', id);
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