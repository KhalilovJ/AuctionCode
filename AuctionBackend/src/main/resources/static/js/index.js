
document.addEventListener('readystatechange', event => {
    if (event.target.readyState === "complete") {
        var clockdiv = document.getElementsByClassName("timerDiv");
        var countDownDate = new Array();
        for (var i = 0; i < clockdiv.length; i++) {
            countDownDate[i] = new Array();
            countDownDate[i]['el'] = clockdiv[i];
            countDownDate[i]['time'] = new Date(clockdiv[i].getAttribute('data')).getTime();
        }

        var countdownfunction = setInterval(function() {
            for (var i = 0; i < countDownDate.length; i++) {
                var now = new Date().getTime();
                var distance = countDownDate[i]['time'] - now;
                let days = Math.floor(distance / (1000 * 60 * 60 * 24));
                let hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60));
                let minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60));
                let seconds = Math.floor((distance % (1000 * 60)) / 1000);

                let text = "".concat(days + " gün " + hours + " saat " + minutes + " dəqiqə " + seconds + " saniyə")

                if (distance < 0) {

                    clockdiv[i].innerHTML = " "
                }else{

                    clockdiv[i].innerHTML = text
                }

            }
        }, 1000);
    }
});