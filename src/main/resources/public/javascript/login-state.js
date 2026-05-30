const logInBtn = document.querySelector("#progress-step-4")
const customerMyPageBtn = document.querySelector("#progress-step-3")
const adminMyPageBtn = document.querySelector("#progress-step-8")
const logOutBtn = document.querySelector("#progress-step-out")


if (currentUser && currentUser.role === "CUSTOMER") {
    logInBtn.classList.add("hidden")
    logOutBtn.classList.remove("hidden")
    customerMyPageBtn.classList.remove("hidden")
} else if (currentUser && currentUser.role === "ADMIN") {
    logInBtn.classList.add("hidden")
    logOutBtn.classList.remove("hidden")
    if (adminMyPageBtn) adminMyPageBtn.classList.remove("hidden")
} else {
    if (logOutBtn) logOutBtn.classList.add("hidden")
}