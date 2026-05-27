const customerCategory = document.querySelector("#my-page-category-customers")
const inquiryCategory = document.querySelector("#my-page-category-inquiry")
const quoteCategory = document.querySelector("#my-page-category-quote")
const orderCategory = document.querySelector("#my-page-category-order")

const customerResults = document.querySelector(".my-page-customer-results")
const inquiryResults = document.querySelector(".my-page-inquiry-results")
const quoteResults = document.querySelector(".my-page-quote-results")
const orderResults = document.querySelector(".my-page-order-results")



customerCategory.addEventListener("click", () => {

    customerResults.classList.remove("hidden")

    inquiryResults.classList.add("hidden")
    quoteResults.classList.add("hidden")
    orderResults.classList.add("hidden")
})

inquiryCategory.addEventListener("click", () => {

    inquiryResults.classList.remove("hidden")

    customerResults.classList.add("hidden")
    quoteResults.classList.add("hidden")
    orderResults.classList.add("hidden")
})

quoteCategory.addEventListener("click", () => {

    quoteResults.classList.remove("hidden")

    customerResults.classList.add("hidden")
    inquiryResults.classList.add("hidden")
    orderResults.classList.add("hidden")
})

orderCategory.addEventListener("click", () => {

    orderResults.classList.remove("hidden")

    customerResults.classList.add("hidden")
    inquiryResults.classList.add("hidden")
    quoteResults.classList.add("hidden")

})