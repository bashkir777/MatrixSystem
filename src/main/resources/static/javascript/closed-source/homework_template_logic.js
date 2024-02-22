let homework;
let navigationButtons = document.getElementsByClassName("navigation-button");
let container = document.getElementById("container");
let arrowLeft = document.getElementById("arrow_left");
let arrowRight = document.getElementById("arrow_right");
let scrollLeft = document.getElementById("scroll_left");
let scrollRight = document.getElementById("scroll_right");
let navigationButtonsWrapper = document.getElementById("navigation_buttons_wrapper");
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