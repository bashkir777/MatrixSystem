let panel = document.getElementById("panel");
let input = document.getElementById("input-option");
let find = document.getElementById("find");

let tutor_panel = document.getElementById("tutor-option-panel");
let tdList = document.getElementsByTagName("td");

let animationRunning = false;
let inversion = false;
panel.addEventListener("mouseover", () => {
    if (!animationRunning) {
        if (!inversion) {
            animationRunning = true;
            panel.classList.add("rotate-m-45");
            setTimeout(() => {
                panel.classList.remove("rotate-m-45");
                panel.classList.add("rotate-360");
            }, 400);
            setTimeout(() => {
                panel.classList.remove("rotate-360");
            }, 1500);
            setTimeout(() => {
                inversion = true;
                animationRunning = false;
            }, 2600);

        } else {
            animationRunning = true;
            panel.classList.add("rotate-45");
            setTimeout(() => {
                panel.classList.remove("rotate-45");
                panel.classList.add("rotate-m-360");
            }, 400);
            setTimeout(() => {
                panel.classList.remove("rotate-m-360");
            }, 1500);
            setTimeout(() => {
                inversion = false;
                animationRunning = false;
            }, 2600);
        }
    }
});

for (let i = 0; i < 9; i++) {
    tdList[i].addEventListener("click", () => {
        input.value = input.value + (i + 1);
    });
}
tdList[9].addEventListener("click", () => {
    input.value = input.value + "0";
});
tdList[10].addEventListener("click", () => {
    if (input.value.length >= 1) {
        input.value = input.value.substring(0, input.value.length - 1);
    }
});

tutor_panel.addEventListener("mouseover", ()=>{
    for (let i = 0; i < 10; i++) {
        tdList[i].classList.add("td-extended");
    }
    find.classList.add("find-extended");
});
tutor_panel.addEventListener("mouseout", ()=>{
    for (let i = 0; i < 10; i++) {
        tdList[i].classList.remove("td-extended");
    }
    find.classList.remove("find-extended");
});

let digits = ["0", "1", "2", "3", "4", "5", "6", "7", "8","9", ""];
let red_border_showing = false;
input.addEventListener("input", function(event) {
    if(event.target.value ===""){
        return;
    }
    if(!digits.includes(event.target.value.charAt(event.target.value.length - 1))){
        input.value = input.value.substring(0, input.value.length - 1);
        if(!red_border_showing){
            tutor_panel.classList.add("red-border");
            setTimeout(()=>{
                tutor_panel.classList.remove("red-border");
                red_border_showing = false;
            }, 1000);
        }
    }
});