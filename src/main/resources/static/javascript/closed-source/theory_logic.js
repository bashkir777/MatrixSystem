let sections = document.getElementsByClassName("section");
let navigationButtons = document.getElementsByClassName("navigation-button-wrapper");
let links = document.getElementsByClassName("section");
let clarification = document.getElementById("clarification");

document.getElementById("theory-navigation-tab").classList.add("mark-header-tab-as-selected");

clarification.classList.remove("display-none");
setTimeout(()=>{
    clarification.classList.add("display-none");
}, 5000);
document.querySelectorAll('.section:not(:has(td))').forEach(function (element) {
    element.remove();
});

for (let button of navigationButtons) {
    button.addEventListener("click", () => {
        let task_id = 'task_' + button.id.split("_")[1];
        let paragraph = document.getElementById(task_id);
        let paragraphRect = paragraph.getBoundingClientRect();
        let offsetTop = paragraphRect.top + window.scrollY;
        let scrollOffset = offsetTop - window.innerHeight / 2 + paragraph.offsetHeight / 2;
        window.scrollTo({top: scrollOffset + 200, left: 0, behavior: 'smooth'});
    });
}

document.addEventListener("DOMContentLoaded", function() {
    if(window.location.href.split("#").length === 2) {
        setTimeout(()=>{window.scrollBy(0, -200);}, 200);
    }
});

for (let link of links) {
    link.addEventListener("contextmenu", (event) => {
        event.preventDefault();
        fetch(`/api/v1/management/toggle/user-section/
        ${link.querySelector("td").id.split("_")[1]}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json'
            }
        })
            .then(response => {
                let currentLink = document.getElementById(link.querySelector("td").id);
                if(response.status === 201){
                    currentLink.classList.add("read-mark");
                }else if(response.status === 204){
                    currentLink.classList.remove("read-mark");
                }else{
                    console.log("не удалось получить обратную связь от сервера")
                }
            }).catch((error) => {
                console.error('Ошибка:', error);
            });
    });
}
