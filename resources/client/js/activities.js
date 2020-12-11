"use strict";
function pageLoad(){
    drawVisualization();
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



        /* let calories;
         calories = response;
         console.log(response);
         console.log("information retrieved for graph.")


         let data = google.visualization.arrayToDataTable(
             [
                 [0, 0],
                 [0,  0],
                 [0,  0],
                 [0,  0],
                 [0,  0],
                 [0,  0],
                 [0,  0],
                 [0,  0],
                 [0,  0],
                 [0,  0]
             ]
         );

         console.log("DATA : " + data);

         for (let i = 0; i < 10; i++){
             data[[i], [0]] = i + 1;
             data[[i], [1]] = calories[i];
         }

         console.log("DATA : " + data);

         var options = {
             title : 'Activity Graph',
             vAxis: {title: 'Calories'},
             hAxis: {title: 'Activity Number'},
             seriesType: 'bars',
             series: {5: {type: 'line'}}
         };

         var chart = new google.visualization.ComboChart(document.getElementById('chart_div'));
         chart.draw(data, options);

         console.log("Activity graph drawn");
         console.log(response);*/
    });

}


// function setGoalDifficulty(){
//     let difficulty = document.getElementById("goalDifficulty");
//     console.log(difficulty);
//     generateGoals(difficulty)
// }
//
//
// function generateGoals(){
//
//     //debugger;
//     console.log("Invoked generateGoals");//prints to console
//     let url = "/activity/generateGoals"; //sets URL to the correct API call
//
//     let formData = new FormData(document.getElementById('difficultyGoalsForm')); //sets formData to the data in the form
//
//     fetch(url, {
//         method: "POST",
//         body: formData,//calls function in Users.java
//     }).then(response => {
//         return response.json();
//     }).then(response => {
//         if (response.hasOwnProperty("Error")) {//checks for error
//             alert(JSON.stringify(response));
//         } else {
//             alert("goals generated");
//         }
//     });
//
// }
//
// function addGoal() {

//}