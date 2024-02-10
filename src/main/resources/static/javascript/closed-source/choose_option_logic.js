let panel = document.getElementById("panel");
let animationRunning = false;
let inversion = false;
panel.addEventListener("mouseover",  ()=>{
    if(!animationRunning){
        if(!inversion){
            animationRunning = true;
            panel.classList.add("rotate-m-45");
            setTimeout(()=>{
                panel.classList.remove("rotate-m-45");
                panel.classList.add("rotate-360");
            }, 400);
            setTimeout(()=>{
                panel.classList.remove("rotate-360");
            }, 1500);
            setTimeout(()=>{
                inversion = true;
                animationRunning = false;
            }, 2600);

        }else{
            animationRunning = true;
            panel.classList.add("rotate-45");
            setTimeout(()=>{
                panel.classList.remove("rotate-45");
                panel.classList.add("rotate-m-360");
            }, 400);
            setTimeout(()=>{
                panel.classList.remove("rotate-m-360");
            }, 1500);
            setTimeout(()=>{
                inversion = false;
                animationRunning = false;
            }, 2600);
        }
    }
});