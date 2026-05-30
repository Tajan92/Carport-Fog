const previewBtn = document.getElementById("preview-svg-btn");
const modal = document.getElementById("svg-modal");
const closeBtn = document.getElementById("close-svg-modal");
const previewContainer = document.getElementById("svg-preview");

previewBtn.addEventListener("click", async () => {

    const response = await fetch("/preview-svg");

    const svgMarkup = await response.text();

    previewContainer.innerHTML = svgMarkup;

    modal.classList.remove("hidden");
});

closeBtn.addEventListener("click", () => {
    modal.classList.add("hidden");
});


// ======= SVG PREVIEW =======
document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("customer-carport-form");
    const previewContainer = document.getElementById("blueprint-preview-container");

    function updateSVGPreview() {
        const formData = new FormData(form);
        console.log("login-state.js loaded, currentUser:", currentUser);
        console.log("updateSVGPreview called");
        console.log("carport_width:", formData.get("carport_width"));
        console.log("carport_length:", formData.get("carport_length"));
        console.log("shed_status:", formData.get("shed_status"));
        console.log("shed_floor:", formData.get("shed_floor"));

        if (!formData.get("carport_width") || !formData.get("carport_length")) return;

        // Use the selectedShed/selectedRoof variables from customer-carport-states.js
        if (!formData.get("shed_status")) {
            formData.set("shed_status", typeof selectedShed !== "undefined" && selectedShed ? selectedShed : "NONE");
        }
        if (!formData.get("shed_floor")) {
            formData.set("shed_floor", "FALSE");
        }

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

    // Update on any select/input change
    form.querySelectorAll("select, input").forEach(input => {
        input.addEventListener("change", updateSVGPreview);
    });

    // Hook into the existing roof/shed box clicks — these already set the hidden radios
    document.querySelector("#flat-roof-box").addEventListener("click", () => setTimeout(updateSVGPreview, 50));
    document.querySelector("#high-roof-box").addEventListener("click", () => setTimeout(updateSVGPreview, 50));
    document.querySelector("#half-shed-box").addEventListener("click", () => setTimeout(updateSVGPreview, 50));
    document.querySelector("#full-shed-box").addEventListener("click", () => setTimeout(updateSVGPreview, 50));
    document.querySelector("#no-shed-box").addEventListener("click", () => setTimeout(updateSVGPreview, 50));

    // Initial render on page load
    updateSVGPreview();
});