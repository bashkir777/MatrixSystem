let input = document.getElementById("input-option");
let find = document.getElementById("find");
let tdList = document.getElementsByTagName("td");
let commentWrapper = document.getElementById("comment-wrapper");
let tutor_panel = document.getElementById("tutor-option-panel");

document.getElementById("homework-navigation-tab").classList.add("mark-header-tab-as-selected");


for (let i = 0; i < 9; i++) {
    tdList[i].addEventListener("click", () => {
        input.value = input.value + (i + 1);
        find.setAttribute("href", `homework/${input.value}`)
    });
}
tdList[9].addEventListener("click", () => {
    input.value = input.value + "0";
    find.setAttribute("href", `homework/${input.value}`)
});
tdList[10].addEventListener("click", () => {
    if (input.value.length >= 1) {
        input.value = input.value.substring(0, input.value.length - 1);
        find.setAttribute("href", `homework/${input.value}`)
    }
});

let digits = ["0", "1", "2", "3", "4", "5", "6", "7", "8", "9", ""];
let red_border_showing = false;
input.addEventListener("input", function (event) {
    if (event.target.value === "") {
        return;
    }
    if (!digits.includes(event.target.value.charAt(event.target.value.length - 1))) {
        input.value = input.value.substring(0, input.value.length - 1);
        if (!red_border_showing) {
            tutor_panel.classList.add("red-border");
            setTimeout(() => {
                tutor_panel.classList.remove("red-border");
                red_border_showing = false;
            }, 2000);
        }
    }else{
        find.setAttribute("href", `homework/${input.value}`)
    }
});

const homeworkNotFound = () => {
    if(!commentShow){
        commentShow = true;
        commentWrapper.classList.remove("display-none");
        setTimeout(()=>{
            commentWrapper.classList.add("display-none");
            commentShow = false;
        }, 3000)
    }
    if (!red_border_showing) {
        tutor_panel.classList.add("red-border");
        setTimeout(() => {
            tutor_panel.classList.remove("red-border");
            red_border_showing = false;
        }, 3000);
    }
}
let commentShow = false;
find.addEventListener("click", (e) => {
    if(input.value === ""){
        e.preventDefault();
        homeworkNotFound();
    }
    let xhr = new XMLHttpRequest();
    xhr.open('GET', `/api/v1/management/homework/info/${input.value}`, false); // false означает синхронный запрос
    xhr.setRequestHeader('Content-Type', 'application/json');
    try {
        xhr.send();
        if (xhr.status === 200) {
            let data = JSON.parse(xhr.responseText);

            if(data.task_counter === 0){
                e.preventDefault();
                homeworkNotFound();
            }
        } else {
            console.error('Ошибка:', xhr.status);
        }
    } catch (error) {
        console.error('Ошибка:', error);
    }
});