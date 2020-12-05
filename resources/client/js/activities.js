"use strict";

function addActivity() {
    //debugger;
    console.log("Invoked addActivity");//prints to console
    let url = "/activitiesCompleted/addActivity"; //sets URL to the correct API call

    let formData = new FormData(document.getElementById('addActivityForm')); //sets formData to the data in the form

    fetch(url, {
        method: "POST",
        body: formData,//calls function in Users.java
    }).then(response => {
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {
            alert("Activity added");
        }
    });
}