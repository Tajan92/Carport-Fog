const roofBtn = document.querySelector("#roof-button");
const shedBtn = document.querySelector("#shed-button");
const measurementsBtn = document.querySelector("#measurements-button");
const shedAddBtn = document.querySelector("#shed-addon-button");
const roofAddBtn = document.querySelector("#roof-addon-button");
const inquiryBtn = document.querySelector("#inquiry-button");



const roofBoxes = document.querySelector(".roof-picker-boxes");
const carportMeasurements = document.querySelector(".carport-measurement-form");
const shedBoxes = document.querySelector(".shed-picker-boxes");
const shedAddon = document.querySelector(".shed-addon-form");
const roofAddon = document.querySelector(".roof-addon-form");
const inquiry = document.querySelector(".inquiry-form");


roofBtn.addEventListener("click", () => {
    roofBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    shedAddon.classList.add("hidden")
    roofAddon.classList.add("hidden")
    inquiry.classList.add("hidden")

    measurementsBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    inquiryBtn.classList.remove("chosen-category")

    roofBoxes.classList.remove("hidden")
})

shedBtn.addEventListener("click", () => {
    shedBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    shedAddon.classList.add("hidden")
    roofAddon.classList.add("hidden")
    inquiry.classList.add("hidden")

    measurementsBtn.classList.remove("chosen-category")
    roofBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    inquiryBtn.classList.remove("chosen-category")

    shedBoxes.classList.remove("hidden")
})

measurementsBtn.addEventListener("click", () => {
    measurementsBtn.classList.add("chosen-category")

    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    shedAddon.classList.add("hidden")
    roofAddon.classList.add("hidden")
    inquiry.classList.add("hidden")

    roofBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    inquiryBtn.classList.remove("chosen-category")

    carportMeasurements.classList.remove("hidden")


})

shedAddBtn.addEventListener("click", () => {
    shedAddBtn.classList.add("chosen-category")

    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    carportMeasurements.classList.add("hidden")
    roofAddon.classList.add("hidden")
    inquiry.classList.add("hidden")


    roofBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    measurementsBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    inquiryBtn.classList.remove("chosen-category")


    shedAddon.classList.remove("hidden")

})

roofAddBtn.addEventListener("click", () => {
    roofAddBtn.classList.add("chosen-category")

    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    carportMeasurements.classList.add("hidden")
    shedAddon.classList.add("hidden")
    inquiry.classList.add("hidden")


    roofBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    measurementsBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    inquiryBtn.classList.remove("chosen-category")

    roofAddon.classList.remove("hidden")

})

inquiryBtn.addEventListener("click", () => {
    inquiryBtn.classList.add("chosen-category")

    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    carportMeasurements.classList.add("hidden")
    shedAddon.classList.add("hidden")
    roofAddon.classList.add("hidden")


    roofBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    measurementsBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")


    inquiry.classList.remove("hidden")
})






