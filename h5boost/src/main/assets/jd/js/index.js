
/**
 * Created by Gin on 2017/1/17.
 */
document.onreadystatechange = function () {
    if (document.readyState == "interactive") {
        window.sonic.ready();
    }
}
window.onload = function () {
    topBarGradient();
    countDown();
    slider();
    window.sonic.mounted();
};
function touch() {
    console.log("c-touchmove");
}
var topBarGradient = function () {
    var searchBar = document.getElementsByClassName('search-bar')[0];
    var slider = document.getElementsByClassName('slider')[0];
    var height = slider.offsetHeight;
    window.onscroll = function (e) {
        var st = document.body.scrollTop;
        if (st < height) {
            var alpha = st / height;
            console.log("rgba(216,80,92," + alpha + ")");
            searchBar.style.backgroundColor = "rgba(216,80,92," + alpha + ")";
        } else {
            // 超过轮播图高度 颜色固定
            searchBar.style.backgroundColor = "#d8505c";
        }
    };
}

var countDown = function () {
    var spans = document.querySelectorAll("#secKill .number");
    var times = 60005;

    setInterval(function () {
        times--;
        var h = Math.floor(times / 3600);
        var m = Math.floor((times / 60) % 60);
        var s = Math.floor(times % 60);
        h = h > 10 ? h+"" : "0"+h;
        m = m > 10 ? m+"" : "0"+m;
        s = s > 10 ? s+"" : "0"+s;
        spans[0].innerHTML = h.substring(0,1);
        spans[1].innerHTML = h.substring(1,2);
        spans[2].innerHTML = m.substring(0,1);
        spans[3].innerHTML = m.substring(1,2);
        spans[4].innerHTML = s.substring(0,1);
        spans[5].innerHTML = s.substring(1,2);
    }, 1000);
    console.log(Date.parse("2017-1-1"));
    console.log(Date.currentTime);
}

var slider = function () {
    var slider_wrapper = document.querySelector('#header > .container .slider-wrapper');
    var lis = slider_wrapper.querySelectorAll('li');
    var offset = slider_wrapper.offsetWidth * 0.1;
    var index = 0;

    function setTransition() {
        slider_wrapper.style.transition = "transform 0.2s ease 0s";
        slider_wrapper.style.webkitTransition = "transform 0.2s ease 0s";
    }

    function removeTransition() {
        slider_wrapper.style.transition = "none";
        slider_wrapper.style.webkitTransition = "none";
    }

    function translateTo(x) {
        slider_wrapper.style.transform = "translateX(" + x + "px)";
        slider_wrapper.style.webkitTransform = "translateX(" + x + "px)";
    }

    function start() {
        return setInterval(function () {
            setTransition();
            index = (index + 1) % (lis.length-1);
            console.log("index:" + index);
            translateTo(-offset*(index+1));
        }, 1000);
    }

    var timer = start();

    slider_wrapper.addEventListener("transitionend", function(event) {
        if (index == lis.length - 2) {
            console.log(index);
            removeTransition();
            translateTo(-offset);
            index = 0;
        } else if (index < 0) {
            console.log("i:" + index);
            // 显示第一张时继续向左拖动，需要跳转到最后一张
            /*removeTransition();
            translateTo((lis.length - 2) * -offset);
            index = -1;
            timer = start();*/
        }
    }, false);

    var startX = 0, endX = 0;
    var startPosition = 0;

    slider_wrapper.addEventListener("touchstart", function (e) {
        clearInterval(timer);
        removeTransition();
        startX = e.targetTouches[0].clientX;
        var matrix = window.getComputedStyle(slider_wrapper).transform.split(",");
        startPosition = parseInt(matrix[4]);
        console.log("touchstart" + startPosition);
    },false);

    slider_wrapper.addEventListener("touchmove", function (e) {
        console.log("touchmove");
        endX = e.targetTouches[0].clientX;
        var dis = endX - startX;
        console.log(startPosition + dis + "," + slider_wrapper.offsetLeft);
        translateTo(startPosition + dis);
    },false);

    slider_wrapper.addEventListener("touchend", function (e) {
        console.log("touchend");
        var distance = endX - startX;
        setTransition();
        if (Math.abs(distance) > lis[0].offsetWidth / 2) {
            // 滑动距离大于宽度的一半，则滑动到下一张，否则弹回
            if (distance < 0) {
                index = (index + 1) % lis.length;
                translateTo((index + 1) * -offset);
            } else {
                index = (index - 1) % lis.length;
                translateTo((index + 1) * -offset);
            }
        } else {
            translateTo(startPosition);
        }
        timer = start();
        /*if (index < 0)
            clearInterval(timer);*/
    },false);


}