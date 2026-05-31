const roofBtn = document.querySelector("#admin-roof-button");
const shedBtn = document.querySelector("#admin-shed-button");
const measurementsBtn = document.querySelector("#admin-measurements-button");
const shedAddBtn = document.querySelector("#admin-shed-addon-button");
const roofAddBtn = document.querySelector("#admin-roof-addon-button");
const quoteBtn = document.querySelector("#admin-quote-button");


const roofBoxes = document.querySelector(".roof-picker-boxes");
const carportMeasurements = document.querySelector(".carport-measurement-form");
const shedBoxes = document.querySelector(".shed-picker-boxes");
const shedAddonForm = document.querySelector(".shed-addon-form");
const roofAddonForm = document.querySelector(".roof-addon-form");
const quoteForm = document.querySelector(".quote-form");




roofBtn.addEventListener("click", () => {
    roofBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    shedAddonForm.classList.add("hidden")
    roofAddonForm.classList.add("hidden")
    quoteForm.classList.add("hidden")

    measurementsBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    quoteBtn.classList.remove("chosen-category")

    roofBoxes.classList.remove("hidden")
})

shedBtn.addEventListener("click", () => {
    shedBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    shedAddonForm.classList.add("hidden")
    roofAddonForm.classList.add("hidden")
    quoteForm.classList.add("hidden")

    measurementsBtn.classList.remove("chosen-category")
    roofBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    quoteBtn.classList.remove("chosen-category")

    shedBoxes.classList.remove("hidden")

})

measurementsBtn.addEventListener("click", () => {
    measurementsBtn.classList.add("chosen-category")

    roofBoxes.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    shedAddonForm.classList.add("hidden")
    roofAddonForm.classList.add("hidden")
    quoteForm.classList.add("hidden")

    roofBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    quoteBtn.classList.remove("chosen-category")

    carportMeasurements.classList.remove("hidden")
})

shedAddBtn.addEventListener("click", () => {
    shedAddBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    roofAddonForm.classList.add("hidden")
    quoteForm.classList.add("hidden")

    roofBtn.classList.remove("chosen-category")
    measurementsBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")
    quoteBtn.classList.remove("chosen-category")

    shedAddonForm.classList.remove("hidden")
    console.log("clicking on shedAddBTN")
})

roofAddBtn.addEventListener("click", () => {
    roofAddBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    shedAddonForm.classList.add("hidden")
    quoteForm.classList.add("hidden")

    roofBtn.classList.remove("chosen-category")
    measurementsBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    quoteBtn.classList.remove("chosen-category")

    roofAddonForm.classList.remove("hidden")
    console.log("clicking on roofAddBTN")
})

quoteBtn.addEventListener("click", () => {
    quoteBtn.classList.add("chosen-category")

    carportMeasurements.classList.add("hidden")
    shedBoxes.classList.add("hidden")
    roofBoxes.classList.add("hidden")
    shedAddonForm.classList.add("hidden")
    roofAddonForm.classList.add("hidden")

    roofBtn.classList.remove("chosen-category")
    measurementsBtn.classList.remove("chosen-category")
    shedBtn.classList.remove("chosen-category")
    shedAddBtn.classList.remove("chosen-category")
    roofAddBtn.classList.remove("chosen-category")

    quoteForm.classList.remove("hidden")
    console.log("clicking on quoteBTN")
})

document.addEventListener("DOMContentLoaded", () => {
    const discountInput = document.getElementById("discountInput");

    if (discountInput) {
        discountInput.addEventListener("change", () => {
            const form = document.getElementById("admin-carport-form");

            form.action = "/admin/preview/quote";
            form.method = "post";
            form.submit();

        });


    }
    if (discountInput.valueOf() !== 0.0){
        quoteBtn.click();
    }

});



