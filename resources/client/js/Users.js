"use strict";
function getUsersList() {
    //debugger;
    console.log("Invoked getUsersList()");
    const url = "/users/list/";
    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));
        } else {
            formatUsersList(response);
        }
    });
}

function formatUsersList(myJSONArray){
    let dataHTML = "";
    for (let item of myJSONArray) {
        dataHTML += "<tr><td>" + item.UserID + "<td><td>" + item.name + "<tr><td>";
    }
    document.getElementById("UsersTable").innerHTML = dataHTML;
}
