function pad(n) {
    return (n < 10 ? "0" + n : n);
}

function init(){
    console.log("init")
        var clockdiv = document.getElementsByClassName("timerDiv");
        for (var i = 0; i < clockdiv.length; i++) {
            convertInnertextToDateTime(clockdiv[i].getAttribute("id"))
        }
}




function convertInnertextToDateTime(elementId){
    let element = document.getElementById(elementId)
    let innertext = element.getAttribute("data-date")
    console.log(innertext)
    let datestamp = new Date(innertext)

    let datetext = datestamp.toISOString().split('T')[0]
    let seconds = datestamp.getSeconds();
    let minutes = datestamp.getMinutes();
    let hour = datestamp.getHours();
    let timetext = datetext + " " + pad(hour)+":"+pad(minutes)+":"+pad(seconds)

    element.innerText = timetext;
}


document.onreadystatechange(init())