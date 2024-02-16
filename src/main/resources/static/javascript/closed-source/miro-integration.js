let stretch = document.getElementById("stretch");
let stretched = false;
stretch.addEventListener("click", ()=>{
    let miro = document.getElementsByTagName("iframe")[0];
    if(!stretched){
        miro.classList.add("stretched");
        stretch.classList.add("reverse");
        stretched = true;
    }else{
        miro.classList.remove("stretched");
        stretch.classList.remove("reverse");
        stretched = false;
    }
});