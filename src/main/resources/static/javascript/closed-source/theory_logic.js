let sections = document.getElementsByClassName("section");
let navigationButtons = document.getElementsByClassName("navigation-button-wrapper");
let links = document.getElementsByTagName("tr");

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

for (let link of links) {
    link.addEventListener("contextmenu", (event) => {
        event.preventDefault();

    });
}
