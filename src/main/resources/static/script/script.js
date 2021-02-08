buttonTextInput = document.getElementById("button_text_input");
responseTextInput = document.getElementById("responseTextInput");
SUCCESS_ALERT = document.getElementById("success_alert");
ERROR_ALERT = document.getElementById("error_alert");

const updateItem = () => {
    const buttonText = buttonTextInput.value;
    const responseText = responseTextInput.value;
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    params.set("trigger", buttonText);
    params.set("responseText", responseText);
    url.href = url.protocol + url.host + "/tbot/edit";
    url.search = params.toString();
    fetch(url.toString(), {method: "POST"})
        .then(response => {
            debugger
            if (response.ok) {
                showSuccessAlert();
            } else {
                response.text()
                    .then(showErrorAlert)
            }
        });
};

const addNewItem = () => {
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    url.href = url.protocol + url.host + "/tbot/add";
    url.search = params.toString();

    fetch(url.toString(), {method: "POST"})
        .then(response => {
            if (response.ok) {
                location.reload();
            } else {
                response.text()
                    .then(showErrorAlert);
            }

        });
};

const deleteItem = () => {
    let url = new URL(window.location.href);
    let params = new URLSearchParams(url.search);
    url.href = url.protocol + url.host + "/tbot/delete";
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

const showErrorAlert = (text) => {
    SUCCESS_ALERT.style.display = "none";
    ERROR_ALERT.style.display = 'block';
    ERROR_ALERT.innerText = 'Error! ' + text;
    setTimeout(() => {
        SUCCESS_ALERT.style.display = "block";
        ERROR_ALERT.style.display = 'none';
        ERROR_ALERT.innerText = ""
    }, 2000);
}

const goBack = () => {
    window.history.back();
};

const goTo = (id) => {
    let url = "/tbot/?id=" + id;
    let currentUrl = new URL(window.location.href);
    let params = new URLSearchParams(currentUrl.search);
    window.history.pushState({prevId: params.get("id")}, null, url);
    location.reload();
};


window.onpopstate = (e) => {
    location.reload();
}
