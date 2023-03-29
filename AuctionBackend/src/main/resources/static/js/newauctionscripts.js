function minChanged(){
    let element = document.getElementById("endDate")
    let start = new Date(document.getElementById("startDate").value)
    start.setDate(start.getDate());
    element.min = start.toISOString().slice(0, -8)
}

function maxChanged(){
    let element = document.getElementById("startDate")
    let start = new Date(document.getElementById("endDate").value)
    start.setDate(start.getDate());
    element.max = start.toISOString().slice(0, -8)
}