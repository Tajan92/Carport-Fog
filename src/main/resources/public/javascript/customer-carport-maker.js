console.log("customer-carport-maker.js loaded");

// ======= DOM REFERENCES =======
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

const form = document.getElementById("customer-carport-form");
const previewContainer = document.getElementById("blueprint-preview-container");

let selectedRoof = null;
let selectedShed = null;

// ======= SVG PREVIEW =======
function updateSVGPreview() {
    const formData = new FormData(form);

    if (!formData.get("carport_width") || !formData.get("carport_length")) return;
    if (!formData.get("shed_status")) formData.set("shed_status", "NONE");
    if (!formData.get("shed_floor")) formData.set("shed_floor", "FALSE");

    fetch("/blueprint/preview", {
        method: "POST",
        body: formData
    })
        .then(response => response.text())
        .then(svgHtml => {
            if (svgHtml && svgHtml.trim().startsWith("<")) {
                previewContainer.innerHTML = svgHtml;
            }
        })
        .catch(error => console.error("SVG fejl:", error));
}

form.querySelectorAll("select, input").forEach(input => {
    input.addEventListener("change", updateSVGPreview);
});

// ======= STATE =======
roofBtn.addEventListener("click", () => {
    roofBtn.classList.add("chosen-category");
    carportMeasurements.classList.add("hidden");
    shedBoxes.classList.add("hidden");
    shedAddon.classList.add("hidden");
    roofAddon.classList.add("hidden");
    inquiry.classList.add("hidden");
    measurementsBtn.classList.remove("chosen-category");
    shedBtn.classList.remove("chosen-category");
    shedAddBtn.classList.remove("chosen-category");
    roofAddBtn.classList.remove("chosen-category");
    inquiryBtn.classList.remove("chosen-category");
    roofBoxes.classList.remove("hidden");
});

shedBtn.addEventListener("click", () => {
    shedBtn.classList.add("chosen-category");
    carportMeasurements.classList.add("hidden");
    roofBoxes.classList.add("hidden");
    shedAddon.classList.add("hidden");
    roofAddon.classList.add("hidden");
    inquiry.classList.add("hidden");
    measurementsBtn.classList.remove("chosen-category");
    roofBtn.classList.remove("chosen-category");
    shedAddBtn.classList.remove("chosen-category");
    roofAddBtn.classList.remove("chosen-category");
    inquiryBtn.classList.remove("chosen-category");
    shedBoxes.classList.remove("hidden");
});

measurementsBtn.addEventListener("click", () => {
    measurementsBtn.classList.add("chosen-category");
    roofBoxes.classList.add("hidden");
    shedBoxes.classList.add("hidden");
    shedAddon.classList.add("hidden");
    roofAddon.classList.add("hidden");
    inquiry.classList.add("hidden");
    roofBtn.classList.remove("chosen-category");
    shedBtn.classList.remove("chosen-category");
    shedAddBtn.classList.remove("chosen-category");
    roofAddBtn.classList.remove("chosen-category");
    inquiryBtn.classList.remove("chosen-category");
    carportMeasurements.classList.remove("hidden");
});

shedAddBtn.addEventListener("click", () => {
    if (selectedShed === "NONE") return;
    shedAddBtn.classList.add("chosen-category");
    roofBoxes.classList.add("hidden");
    shedBoxes.classList.add("hidden");
    carportMeasurements.classList.add("hidden");
    roofAddon.classList.add("hidden");
    inquiry.classList.add("hidden");
    roofBtn.classList.remove("chosen-category");
    shedBtn.classList.remove("chosen-category");
    measurementsBtn.classList.remove("chosen-category");
    roofAddBtn.classList.remove("chosen-category");
    inquiryBtn.classList.remove("chosen-category");
    shedAddon.classList.remove("hidden");
});

roofAddBtn.addEventListener("click", () => {
    if (selectedRoof === "flat") return;
    roofAddBtn.classList.add("chosen-category");
    roofBoxes.classList.add("hidden");
    shedBoxes.classList.add("hidden");
    carportMeasurements.classList.add("hidden");
    shedAddon.classList.add("hidden");
    inquiry.classList.add("hidden");
    roofBtn.classList.remove("chosen-category");
    shedBtn.classList.remove("chosen-category");
    measurementsBtn.classList.remove("chosen-category");
    shedAddBtn.classList.remove("chosen-category");
    inquiryBtn.classList.remove("chosen-category");
    roofAddon.classList.remove("hidden");
});

inquiryBtn.addEventListener("click", () => {
    inquiryBtn.classList.add("chosen-category");
    roofBoxes.classList.add("hidden");
    shedBoxes.classList.add("hidden");
    carportMeasurements.classList.add("hidden");
    shedAddon.classList.add("hidden");
    roofAddon.classList.add("hidden");
    roofBtn.classList.remove("chosen-category");
    shedBtn.classList.remove("chosen-category");
    measurementsBtn.classList.remove("chosen-category");
    shedAddBtn.classList.remove("chosen-category");
    roofAddBtn.classList.remove("chosen-category");
    inquiry.classList.remove("hidden");
});

// ======= ROOF/SHED SELECTION =======
document.querySelector("#flat-roof-box").addEventListener("click", () => {
    document.querySelector("#flat-roof").checked = true;
    selectedRoof = "flat";
    updateTabAvailability();
    setTimeout(updateSVGPreview, 50);
});

document.querySelector("#high-roof-box").addEventListener("click", () => {
    document.querySelector("#high-roof").checked = true;
    selectedRoof = "high";
    updateTabAvailability();
    setTimeout(updateSVGPreview, 50);
});

document.querySelector("#half-shed-box").addEventListener("click", () => {
    document.querySelector("#half-shed").checked = true;
    selectedShed = "HALF";
    updateTabAvailability();
    setTimeout(updateSVGPreview, 50);
});

document.querySelector("#full-shed-box").addEventListener("click", () => {
    document.querySelector("#full-shed").checked = true;
    selectedShed = "FULL";
    updateTabAvailability();
    setTimeout(updateSVGPreview, 50);
});

document.querySelector("#no-shed-box").addEventListener("click", () => {
    document.querySelector("#no-shed").checked = true;
    selectedShed = "NONE";
    updateTabAvailability();
    setTimeout(updateSVGPreview, 50);
});

function updateTabAvailability() {
    if (selectedRoof === "flat") {
        roofAddBtn.classList.add("disabled-category");
        roofAddBtn.disabled = true;
    } else {
        roofAddBtn.classList.remove("disabled-category");
        roofAddBtn.disabled = false;
    }
    if (selectedShed === "NONE") {
        shedAddBtn.classList.add("disabled-category");
        shedAddBtn.disabled = true;
    } else {
        shedAddBtn.classList.remove("disabled-category");
        shedAddBtn.disabled = false;
    }
}

updateSVGPreview();