let i = 1;
const bidsWrapper = document.getElementById("bids");
let bidstepVal = parseFloat(document.getElementById("bid_step").innerText);

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
        let bidtext = json.username.concat(" ").concat(json.bid);
        const nodeDiv = document.createElement("div");
        nodeDiv.innerHTML = bidtext
        document.getElementById("currentBid").textContent=text;
        bidsWrapper.appendChild(nodeDiv)
        console.log(event.data)
        updateBidBox();
    })
}
subscribe();



function UserAction() {

    console.log("Bid placed")
        let url = new URL(window.location.href);
        let lotId = url.pathname.split("/")[2];

        let input = "/open/api/bids/makeBid";
        let bidout = parseFloat(document.getElementById("inc").value);
        let currentBid = parseFloat(document.getElementById("currentBid").innerText);
        if (bidout > currentBid){

        fetch(input, {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json'
            },

            body: JSON.stringify('{ "lotId": '.concat(lotId).concat(' , "bid": ').concat(bidout).concat(' }'))


        })
            .then(response => {
            console.log(response);

            updateBidBox();

            });
        } else {
            console.log(bidout); console.log(currentBid)
            console.log("Bid error")}

}


function buttonClickP() {
    let currentBid = parseFloat(document.getElementById("currentBid").innerText);
    document.getElementById('inc').value = ++i*bidstepVal+currentBid;
    console.log(document.getElementById("inc").value);
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

var date1 = new Date("2023-03-18 14:14:49.363691");

var upgradeTime = 172801;

var seconds = upgradeTime;
function timer() {
    var difference = date1.getTime() - Date.now();
    // console.log(difference);

    var daysDifference = Math.floor(difference/1000/60/60/24);
    difference -= daysDifference*1000*60*60*24

    var hoursDifference = Math.floor(difference/1000/60/60);
    difference -= hoursDifference*1000*60*60

    var minutesDifference = Math.floor(difference/1000/60);
    difference -= minutesDifference*1000*60

    var secondsDifference = Math.floor(difference/1000);

    // console.log('difference = ' +
    //   daysDifference + ' day/s ' +
    //   hoursDifference + ' hour/s ' +
    //   minutesDifference + ' minute/s ' +
    //   secondsDifference + ' second/s ');

    function pad(n) {
        return (n < 10 ? "0" + n : n);
    }
    document.getElementById('countdown').innerHTML = "Auksionun bitməsinə qalan vaxt: " +  pad(daysDifference) + " gün " + pad(hoursDifference) + " saat " + pad(minutesDifference) + " dəqiqə " + pad(secondsDifference) + " saniyə";
    if (seconds == 0) {
        clearInterval(countdownTimer);
        document.getElementById('countdown').innerHTML = "Completed";
    } else {
        seconds--;
    }
}
var countdownTimer = setInterval('timer()', 1000);