let sections = document.getElementsByClassName("section");
let dropDowns = document.getElementsByClassName("drop-down");
let navigationButtons = document.getElementsByClassName("navigation-button-wrapper");

for(let i = 0; i < sections.length; i++){
    sections[i].addEventListener("click",
        ()=>{
            console.log("выполнение")
            dropDowns[i].classList.toggle("display-none");
            sections[i].querySelector('.arrow').classList.toggle("rotate-180");
        });
}


// фиксим кривое отображение предпоследней строки в каждой таблицу
let tables = document.querySelectorAll('table');
tables.forEach(function(table) {
    let rowCount = table.rows.length;
    if (rowCount >= 2) {
        let penultimateRow = table.rows[rowCount - 2];
        penultimateRow.classList.add('border-bottom-none');
        penultimateRow.addEventListener("click", ()=>{
            penultimateRow.classList.toggle('border-bottom-none');
        })
    }
});

for (let button of navigationButtons){
    button.addEventListener("click", () =>{
        let task_id = 'task_' + button.id.split("_")[1];
        let paragraph = document.getElementById(task_id);
        let paragraphRect = paragraph.getBoundingClientRect();
        let offsetTop = paragraphRect.top + window.scrollY;
        let scrollOffset = offsetTop - window.innerHeight / 2 + paragraph.offsetHeight / 2;
        window.scrollTo({ top: scrollOffset+200, left: 0, behavior: 'smooth' });
    });
}

