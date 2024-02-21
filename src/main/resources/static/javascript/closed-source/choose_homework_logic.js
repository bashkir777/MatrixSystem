let input = document.getElementById("input-option");
let find = document.getElementById("find");
let tdList = document.getElementsByTagName("td");
let commentWrapper = document.getElementById("comment-wrapper");

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
            }, 1000);
        }
    }
});

find.addEventListener("click", () => {
    console.log(input.value);
});