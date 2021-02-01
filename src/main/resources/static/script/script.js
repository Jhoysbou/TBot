buttonTextInput = document.getElementById("button_text_input");
responseTextInput = document.getElementById("responseTextInput");
SUCCESS_ALERT = document.getElementById("success_alert");
ERROR_ALERT = document.getElementById("error_alert");

const updateItem = () => {
    debugger
    const buttonText = buttonTextInput.value;
    const responseText = responseTextInput.value;
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    params.set("trigger", buttonText);
    params.set("responseText", responseText);
    url.href = url.protocol + url.host + "/edit";
    url.search = params.toString();
    fetch(url.toString(), {method: "POST"})
        .then(response => {
            if (response.ok) {
                showSuccessAlert();
            } else {
                showErrorAlert();
            }
        });
};

const addNewItem = () => {
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    url.href = url.protocol + url.host + "/add";
    url.search = params.toString();

    fetch(url.toString(), {method: "POST"})
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                showErrorAlert();
            }

        });
};

const deleteItem = () => {
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    url.href = url.protocol + url.host + "/delete";
    url.search = params.toString();
    fetch(url.toString(), {method: "POST"})
        .then(response => {
            if (response.ok) {
                window.history.back();
            } else {
                showErrorAlert();
            }
        });
};

const showSuccessAlert = () => {
    SUCCESS_ALERT.style.visibility = "visible";
    setTimeout(() => {
        SUCCESS_ALERT.style.visibility = 'hidden'
    }, 1000);
};

const showErrorAlert = () => {
    SUCCESS_ALERT.style.display = "none";
    ERROR_ALERT.style.display = 'block';
    setTimeout(() => {
        SUCCESS_ALERT.style.display = "block";
        ERROR_ALERT.style.display = 'none';
    }, 1000);
}

const goBack = () => {
    window.history.back();
};

// for the pretty alerts after reload
// document.addEventListener("DOMContentLoaded", function (event) {
//     if (window.location.hash === "#SUCCESS") {
//         showSuccessAlert();
//         window.location.hash = "";
//
//     } else if (window.location.hash === "#ERROR") {
//         showErrorAlert();
//         window.location.hash = "";
//     }
// });

const goTo = (id) => {
    let url = "/?id=" + id;
    let currentUrl = new URL(window.location.href);
    let params = new URLSearchParams(currentUrl.search);
    window.history.pushState({prevId: params.get("id")}, null, url);
    location.reload();
};


window.onpopstate = (e) => {
    location.reload();
}
