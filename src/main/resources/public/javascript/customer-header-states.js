const carportSection = document.querySelector("#progress-step-2");
const myPageSection = document.querySelector("#progress-step-3");


document.addEventListener('DOMContentLoaded', () => {

    const currentPath = window.location.pathname

    if (currentPath === "/"){
        carportSection.classList.add('header-state-active');
    }
    if (currentPath.includes("customerProfilePage")) {
        myPageSection.classList.add('header-state-active');
    }

});

