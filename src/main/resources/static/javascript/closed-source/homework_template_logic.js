let homework;
let navigationButtons = document.getElementsByClassName("navigation-button");
let container = document.getElementById("container");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let scrollLeft = document.getElementById("scroll_left");
let scrollRight = document.getElementById("scroll_right");
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
let taskNum = document.getElementById("task-num");
let solution = document.getElementById("full-answer");
let imageContainer = document.getElementById("image-container");
let taskText = document.getElementById("task-text");
let answerText = document.getElementById("_answer");
let send = document.getElementById("send");
let currentTaskId;
let currentTaskOrder;
function getModuleNumFromUrl() {
    return location.href.split("/").pop().split("?")[0];
}
fetch(`/api/v1/management/homework/${getModuleNumFromUrl()}`, {
    method: 'GET',
    headers: {
        'Content-Type': 'application/json'
    }
})
    .then(response => response.json())
    .then(data => {
        homework = data;
        for (let i =0; i<navigationButtons.length; i++){
            let button = navigationButtons[i];
            button.addEventListener("click", ()=>{
                currentTaskOrder = i+1;
                if (currentTaskOrder === 1) {
                    arrowLeft.classList.add("display-none");
                } else {
                    arrowLeft.classList.remove("display-none");
                }
                if (currentTaskOrder === navigationButtons.length) {
                    arrowRight.classList.add("display-none");
                } else {
                    arrowRight.classList.remove("display-none");
                }
                for(let b of navigationButtons){
                    b.classList.remove("navigation-tab-selected");
                }
                button.classList.add("navigation-tab-selected");
                currentTaskId = data[i].id;
                taskNum.innerText = currentTaskOrder;
                taskText.innerText = data[i].task;
                console.log(data[i].status);
                imageContainer.classList.add("display-none");
                if (data[i].img !== null) {
                    let imgSrc = data[i].img.replace(/\\/g, '/');
                    imageContainer.src = location.protocol + "//" + location.host + "/" + imgSrc;
                    imageContainer.classList.remove("display-none");
                }

            })
        }
        navigationButtons[0].click();
    })
    .catch((error) => {
    console.error('Ошибка:', error);
});



if (arrowLeft !== null){
    arrowLeft.addEventListener("mouseover", ()=>{
        container.classList.add("rotate-1-left");
    });
    arrowLeft.addEventListener("mouseout", ()=>{
        container.classList.remove("rotate-1-left");
    });
}
arrowRight.addEventListener("click", ()=>{
    if(currentTaskOrder !== navigationButtons.length){
        navigationButtons[currentTaskOrder].click();
    }
});
arrowLeft.addEventListener("click", () =>{
    if(currentTaskOrder !== 1){
        navigationButtons[currentTaskOrder - 2].click();
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