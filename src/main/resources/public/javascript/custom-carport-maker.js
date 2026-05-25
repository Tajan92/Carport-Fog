const roofBtn = document.querySelector("#roof-button");
const shedBtn = document.querySelector("#shed-button");
const measurementsBtn = document.querySelector("#measurements-button");



const roofBoxes = document.querySelector(".roof-picker-boxes");
const carportMeasurements = document.querySelector(".carport-measurement-form");
const shedBoxes = document.querySelector(".shed-picker-boxes");


roofBtn.addEventListener("click", () => {
    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    roofBoxes.classList.remove("hidden")
})

shedBtn.addEventListener("click", () => {
    carportMeasurements.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    shedBoxes.classList.remove("hidden")
})

measurementsBtn.addEventListener("click", () => {
    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    carportMeasurements.classList.remove("hidden")

})





