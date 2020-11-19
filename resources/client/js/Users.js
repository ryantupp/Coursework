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

function UsersLogin() { <!--declares the function-->
    //debugger;
    console.log("Invoked UsersLogin() "); <!--logs to console to make it easier when debugging.-->
    let url = "/users/login"; <!--sets url code to the correct API path.-->
    let formData = new FormData(document.getElementById('LoginForm')); <!--gets the element LoginForm in the login.html file.-->

    fetch(url, { <!--uses fetch and it is retrieving data from the database-->
        method: "POST", <!--sets method as post-->
        body: formData,
    }).then(response => {
        return response.json();                 <!--now return that promise to JSON-->
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        <!-- if it does, convert JSON object to string and alert-->
        } else {
            Cookies.set("Token", response.Token); <!--sets cookie using the token and name-->
            Cookies.set("UserName", response.UserName);
            window.open("index.html", "_self");       <!--open index.html in same tab-->
        }
    });
}

function logout() {
    //debugger; //can be used if I need to debug my code.
    console.log("Invoked logout");//prints to console
    let url = "/users/logout";//sets url to logout
    fetch(url, {method: "POST"//fetches the corresponding post method
    }).then(response => {
        return response.json();                 //now return that promise to JSON
    }).then(response => {
        if (response.hasOwnProperty("Error")) {
            alert(JSON.stringify(response));        // if it does, convert JSON object to string and alert
        } else {
            Cookies.remove("Token", response.Token);    //UserName and Token are removed
            Cookies.remove("UserName", response.UserName);
            window.open("login.html", "_self");       //open index.html in same tab
        }
    });
}

