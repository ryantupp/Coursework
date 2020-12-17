"use strict";
function pageLoad(){
    drawVisualization();
    generateGoals()
    displayGoal()
}


//google.charts.load('current', {'packages':['corechart']});
//google.charts.setOnLoadCallback(drawVisualization);

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

function drawVisualization() {

    //debugger;

    console.log("Invoked drawVisualization");//prints to console
    let url = "/activitiesCompleted/drawGraph"; //sets URL to the correct API call

    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();
    }).then(items => {
        if (items.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(items));
        } else {
            var data = [];//creates an array called data

            let i = 0;

            for (let item of items){//fills in the data array with json object
                if(i < 10){
                    data[i] = {"activity": i + 1, "calories": item.calories};
                    i++;
                }
            }

            var labels = data.map(function (e) {//creates labels for the graph
                return e.activity;
            });
            var calories = data.map(function (e) {//creates the data field for the graph
                return e.calories;
            });

            var ctx = document.getElementById("myChart").getContext("2d");
            var config = {//creates the grapg
                type: 'bar',
                data: {
                    labels: labels,
                    datasets: [{
                        label: 'Calories burnt',  //title of graph
                        data: calories,
                        backgroundColor: 'rgba(0, 119, 204, 0.3)'
                    }]
                },
                options: {
                    responsive: true,
                    maintainAspectRatio: false,
                    scales: {
                        yAxes: [{
                            ticks: {
                                beginAtZero: true,
                            }
                        }]
                    }
                },

            };

            var chart= new Chart(ctx, config);
        }
    });

}


function generateGoals(){

    let goals = ["Run 30 km", "Cycle 100km", "Swim 10 km", "Row 50km", "Burn 10000 calories", "Exercise for 600 minutes", "Complete 10 easy activities", "Complete 10 medium activities", "Complete 10 hard activities", "Complete 20 activities"];

    let goal1 = goals[Math.floor(Math.random() * 10)];
    let goal2 = goals[Math.floor(Math.random() * 10)];
    let goal3 = goals[Math.floor(Math.random() * 10)];

    while((goal1 == goal2) || (goal2 == goal3) || (goal1 == goal3)){
        goal2 = goals[Math.floor(Math.random() * 10)];
        goal3 = goals[Math.floor(Math.random() * 10)];
    }

    document.getElementById("goalChoice1").innerHTML = goal1;//sets values in the dropdown list
    document.getElementById("goalChoice2").innerHTML = goal2;
    document.getElementById("goalChoice3").innerHTML = goal3;

}

function addGoal() {
    //debugger;
    console.log("Invoked addGoals");//prints to console
    let url = "/activitiesCompleted/addGoals"; //sets URL to the correct API call

    let formData = new FormData(document.getElementById('goalsForm')); //sets formData to the data in the form

    fetch(url, {
        method: "POST",
        body: formData, //calls function in Users.java
    }).then(response => {
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {
            alert("goal added");//lets user know goal is added
        }
    });
}

function displayGoal(){
    //debugger;
    console.log("Invoked displayGoal");//prints to console
    let url = "/activitiesCompleted/returnGoal"; //sets URL to the correct API call

    fetch(url, {
        method: "GET",
    }).then(response => {
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {
            console.log("here");

            // console.log(response);
            // document.getElementById("goalDescription").innerHTML = JSON.stringify(response);
        }
    });
}

function goalCompleted(){
    //debugger;
    console.log("Invoked goalCompleted");//prints to console
    let url = "/activitiesCompleted/goalCompleted"; //sets URL to the correct API call

    fetch(url, {
        method: "POST",
    }).then(response => {
        return response.json();
    }).then(response => {
        if (response.hasOwnProperty("Error")) {//checks for error
            alert(JSON.stringify(response));
        } else {
            alert(JSON.stringify(response));
        }
    });
}