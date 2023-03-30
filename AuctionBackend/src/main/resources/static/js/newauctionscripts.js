function pad(n) {
    return (n < 10 ? "0" + n : n);
}

function convert(date) {
    let str = date.getFullYear() + "-" + pad(date.getMonth()+1)+"-"+pad(date.getDate())+"T" + pad(date.getHours())+":"+ pad(date.getMinutes())
    return str
}
function minChanged(){
    let element = document.getElementById("endDate")
    let start = new Date(document.getElementById("startDate").value)
    start.setDate(start.getDate());
    element.min = convert(start)
}

function maxChanged(){
    let element = document.getElementById("startDate")
    let start = new Date(document.getElementById("endDate").value)
    start.setMinutes(start.getMinutes() - 15)
    console.log(start)
    element.max = convert(start)}

function init(){
let element1 = document.getElementById("startDate")
let element2 = document.getElementById("endDate")
let start1 = new Date(Date.now())
element1.min = convert(start1);
element2.min = convert(start1);
}

init()