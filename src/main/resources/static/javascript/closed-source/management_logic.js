
document.getElementById("management-navigation-tab").classList.add("mark-header-tab-as-selected");

let createUserPanel = document.getElementById("create-user");
let createButton = document.getElementById("create-user-button");
let userLoginCreate = document.getElementById("user-login-create");
let userPasswordCreate = document.getElementById("user-password-create");
let userNameCreate = document.getElementById("user-name-create");
let userSurnameCreate = document.getElementById("user-surname-create");
let userTelegramCreate = document.getElementById("user-tg-create");
let userPhoneCreate = document.getElementById("user-phone-create");
let userRoleCreate = document.getElementById("user-role-create");
let userMiroCreate = document.getElementById("user-miro-board-create");
let wrongCreateFormComment = document.getElementById("wrong_create_form_comment");

let wrongCreateFormCommentShowing = false;
createButton.addEventListener("click", ()=>{
    const formData = new FormData();
    if(userLoginCreate.value === "" || userPasswordCreate.value === ""
    || userNameCreate.value === "" || userRoleCreate.value === "" ||
        userSurnameCreate.value === "" || userMiroCreate.value === ""){
        if(!wrongCreateFormCommentShowing){
            wrongCreateFormCommentShowing= true;
            wrongCreateFormComment.classList.remove("display-none");
            createUserPanel.classList.add("red-border");
            setTimeout(()=>{
                wrongCreateFormComment.classList.add("display-none");
                createUserPanel.classList.remove("red-border");
                wrongCreateFormCommentShowing = false;
            }, 4000)
        }

        return;
    }
    formData.append('login', userLoginCreate.value);
    formData.append('password', userPasswordCreate.value);
    formData.append('phoneNumber', userPhoneCreate.value);
    formData.append('telegram', userTelegramCreate.value);
    formData.append('name', userNameCreate.value);
    formData.append('surname', userSurnameCreate.value);
    formData.append('role', userRoleCreate.value);
    formData.append('miroBoard', userMiroCreate.value);
    fetch(`/api/v1/management/add/user`, {
        method: 'POST',
        body: formData
    })
        .then(response => {
            if(response.status === 201) {
                console.log("пользователь успешно создан");
            }else{
                console.log("не удалось создать пользователя");
            }
        }
    )

});