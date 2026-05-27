const roofBtn = document.querySelector("#roof-button");
const shedBtn = document.querySelector("#shed-button");
const measurementsBtn = document.querySelector("#measurements-button");



const roofBoxes = document.querySelector(".roof-picker-boxes");
const carportMeasurements = document.querySelector(".carport-measurement-form");
const shedBoxes = document.querySelector(".shed-picker-boxes");


roofBtn.addEventListener("click", () => {
    roofBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    measurementsBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")

    roofBoxes.classList.remove("hidden")
})

shedBtn.addEventListener("click", () => {
    shedBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    measurementsBtn.classList.remove("chosen-category")
    roofBtn.classList.remove("chosen-category")

    shedBoxes.classList.remove("hidden")

})

measurementsBtn.addEventListener("click", () => {
    measurementsBtn.classList.add("chosen-category")

    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    roofBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")

    carportMeasurements.classList.remove("hidden")


})





