
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
let createdLabel = document.getElementById("created-label");
let loginOfNewUser = document.getElementById("login-of-new-user");
let passwordOfNewUser = document.getElementById("password-of-new-user");
let failedToCreateUser = document.getElementById("failed_to_create_user");
let deleteUser = document.getElementById("delete-user");
let deleteUserButton = document.getElementById("delete-user-button");
let userLoginDelete = document.getElementById("user-login-delete");
let userNameDelete = document.getElementById("user-name-delete");
let userSurnameDelete = document.getElementById("user-surname-delete");

let wrongCreateFormCommentShowing = false;
let failedToCreateUserShowing = false;
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
                smoke.classList.remove("display-none");
                createdLabel.classList.remove("display-none");
                loginOfNewUser.innerText = userLoginCreate.value;
                passwordOfNewUser.innerText = userPasswordCreate.value;
            }else{
                if(!failedToCreateUserShowing) {
                    failedToCreateUserShowing = true;
                    failedToCreateUser.classList.remove("display-none");
                    setTimeout(() => {
                        failedToCreateUser.classList.add("display-none");
                        failedToCreateUserShowing = false;
                    }, 4000)
                }
            }
        }
    )
});

deleteUserButton.addEventListener("click", ()=>{
    if(userNameDelete.value !== "" && userSurnameDelete.value !== ""){
        const formData = new FormData();
        formData.append('name', userNameDelete.value);
        formData.append('surname', userSurnameDelete.value);
        fetch(`/api/v1/management/delete/user/by-info`, {
            method: 'DELETE',
            body: formData
        })
            .then(response => {
                    if(response.status === 204) {
                        console.log("пользователь успешно удален");
                    }else{
                        console.log("Не удалось удалить пользователя");
                    }
                }
            )
    }else if(userLoginDelete.value !== ""){
        const formData = new FormData();
        formData.append('login', userLoginDelete.value);
        fetch(`/api/v1/management/delete/user/by-login`, {
            method: 'DELETE',
            body: formData
        })
            .then(response => {
                    if(response.status === 204) {
                        console.log("пользователь успешно удален");
                    }else{
                        console.log("Не удалось удалить пользователя");
                    }
                }
            )
    }else{
        console.log("форма заполнена некорректно");
    }
})