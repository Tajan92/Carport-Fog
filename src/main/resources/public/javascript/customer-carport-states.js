const flatRoofBox = document.querySelector("#flat-roof-box")
const highRoofBox = document.querySelector("#high-roof-box")

const fullShedBox = document.querySelector("#full-shed-box")
const halfShedBox = document.querySelector("#half-shed-box")
const noShedBox = document.querySelector("#no-shed-box")

/* ===== ROOF BOXES ===== */
flatRoofBox.addEventListener("click",() => {
    highRoofBox.classList.remove("chosen-roof-box")
    document.querySelector('#flat-roof').checked = true;
    flatRoofBox.classList.add("chosen-roof-box")
})

highRoofBox.addEventListener("click",() => {
    flatRoofBox.classList.remove("chosen-roof-box")
    document.querySelector('#high-roof').checked = true;
    highRoofBox.classList.add("chosen-roof-box")
})

/* ===== SHED BOXES ===== */
halfShedBox.addEventListener("click",() => {
    fullShedBox.classList.remove("chosen-shed-box")
    noShedBox.classList.remove("chosen-shed-box")
    document.querySelector('#half-shed').checked = true;
    halfShedBox.classList.add("chosen-shed-box")
})

fullShedBox.addEventListener("click",() => {
    halfShedBox.classList.remove("chosen-shed-box")
    noShedBox.classList.remove("chosen-shed-box")
    document.querySelector('#full-shed').checked = true;
    fullShedBox.classList.add("chosen-shed-box")
})

noShedBox.addEventListener("click",() => {
    halfShedBox.classList.remove("chosen-shed-box")
    fullShedBox.classList.remove("chosen-shed-box")
    document.querySelector('#no-shed').checked = true;
    noShedBox.classList.add("chosen-shed-box")
})







