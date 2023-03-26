let i = 1;
const bidsWrapper = document.getElementById("bids");
let bidstepVal = parseFloat(document.getElementById("bid_step").innerText);
// let bidButton = document.getElementById("bidButton")
let bidplaced = true

function pad(n) {
    return (n < 10 ? "0" + n : n);
}

function updateBidBox(){
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    i=1;
    document.getElementById('inc').value = bidstepVal+parseFloat(currentBid);

}
function subscribe(){

    let url = new URL(window.location.href);

    console.log(url);

    let lotId = url.pathname.split("/")[2];

    console.log(lotId)

    let urlEndpoint = `http://127.0.0.1:9090/open/api/bids/${lotId}`
    let eventSource = new EventSource(urlEndpoint)

    eventSource.addEventListener("bid", function(event){

        let json = JSON.parse(event.data);
        let text = json.lotCurrentBidPrice;
        let bidtext = '<span class="col-md-3">'.concat(json.username).concat("</span>");
        bidtext = bidtext.concat('<span class="col-md-3">').concat(json.bid).concat("</span>");

        let timestamp= new Date(json.bidTime);
        let datetext = timestamp.toISOString().split('T')[0]
        // console.log(datetext)
        var seconds = timestamp.getSeconds();
        var minutes = timestamp.getMinutes();
        var hour = timestamp.getHours();
        let timetext = datetext+ " " + pad(hour) + ":" + pad(minutes)+":" + pad(seconds)
        bidtext = bidtext.concat('<span class="col-md-6">').concat(timetext).concat("</span>");

        const nodeDiv = document.createElement("div");
        nodeDiv.setAttribute("class", "shadow-sm border d-flex justify-content-between rounded-3");
        nodeDiv.innerHTML = bidtext

        document.getElementById("currentBid").textContent=text;
        bidsWrapper.prepend(document.createElement("br"));
        bidsWrapper.prepend(nodeDiv)
        // console.log(event.data)
        updateBidBox();
    })
}
subscribe();



function UserAction() {

    // console.log("Bid placed")
    let url = new URL(window.location.href);
    let lotId = url.pathname.split("/")[2];

    let input = "/open/api/bids/makeBid";
    let bidout = parseFloat(document.getElementById("inc").value);
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);

    if (bidout >= currentBid + bidstepVal && bidplaced){

        bidplaced = false

        fetch(input, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },

            body: JSON.stringify('{ "lotId": '.concat(lotId).concat(' , "bid": ').concat(bidout).concat(' }'))


        })
            .then(response => {
                // console.log(response);
                bidplaced = true
                updateBidBox();
            });
    } else {
        // bidplaced = true
        // console.log(bidout); console.log(currentBid)
        console.log("Bid error")}

}


function buttonClickP() {
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = ++i*bidstepVal+currentBid;
    // console.log(document.getElementById("inc").value);
}
function buttonClickM() {
    i--
    if (i<= 0){ i = 1}
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = i*bidstepVal+currentBid;
}


function updateBid(){
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = bidstepVal+currentBid;
}

updateBid();

var date1 = new Date(document.getElementById("endDateW").innerText);

var upgradeTime = 172801;

var seconds = upgradeTime;
function timer() {

    if (date1 > Date.now()) {
        var difference = date1.getTime() - Date.now();
        // console.log(difference);

        var daysDifference = Math.floor(difference / 1000 / 60 / 60 / 24);
        difference -= daysDifference * 1000 * 60 * 60 * 24

        var hoursDifference = Math.floor(difference / 1000 / 60 / 60);
        difference -= hoursDifference * 1000 * 60 * 60

        var minutesDifference = Math.floor(difference / 1000 / 60);
        difference -= minutesDifference * 1000 * 60

        var secondsDifference = Math.floor(difference / 1000);

        // console.log('difference = ' +
        //   daysDifference + ' day/s ' +
        //   hoursDifference + ' hour/s ' +
        //   minutesDifference + ' minute/s ' +
        //   secondsDifference + ' second/s ');



        document.getElementById('countdown').innerHTML = "Auksionun bitməsinə qalan vaxt: " + pad(daysDifference) + " gün " + pad(hoursDifference) + " saat " + pad(minutesDifference) + " dəqiqə " + pad(secondsDifference) + " saniyə";
        if (seconds == 0) {
            clearInterval(countdownTimer);
            document.getElementById('countdown').innerHTML = "Completed";
        } else {
            seconds--;
        }
    } else {

        let bidarea = document.getElementById('bid_area')
        bidarea.innerHTML = "                   <div class=\"justify-content-center d-flex\"><div>\n" +
            "                                <p >Auksion bağlıdır</p>"
            "</div>";
        console.log("stopped")
        clearInterval(countdownTimer);
    }
}
var countdownTimer = setInterval('timer()', 1000);

function convertInnertextToDateTime(elementId){
    let element = document.getElementById(elementId)
    let innertext = element.innerText
    let datestamp = new Date(innertext)

    let datetext = datestamp.toISOString().split('T')[0]
    let seconds = datestamp.getSeconds();
    let minutes = datestamp.getMinutes();
    let hour = datestamp.getHours();
    let timetext = datetext +" " + pad(hour) + ":"+ pad(minutes)+":"+ pad(seconds)

    element.innerText = timetext;}


function  init(){
    console.log("init")
    convertInnertextToDateTime("endDate")
    convertInnertextToDateTime("startDate")

    var elements = document.getElementsByClassName("changeTime");
    for(var i = 0; i < elements.length; i++) {
        console.log(elements[i])
        convertInnertextToDateTime(elements[i].getAttribute("id"))
    }

}

// function docReady(fn) {
//     init()
// }

document.onreadystatechange(init())