const carportSection = document.querySelector("#progress-step-7");
const myPageSection = document.querySelector("#progress-step-8");


document.addEventListener('DOMContentLoaded', () => {

    const currentPath = window.location.pathname

    if (currentPath.includes("carportMakerSalesRep")){
        carportSection.classList.add('header-state-active');
    }
    if (currentPath.includes("adminProfilePage")) {
        myPageSection.classList.add('header-state-active');
    }

});

