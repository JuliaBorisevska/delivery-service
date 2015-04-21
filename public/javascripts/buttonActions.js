$(document).ready(function () {
    var wWidth = $(window).width();
    var wHeight = $(window).height();
    var dWidth = wWidth * 0.8;
    $('.select-user').dialog({
        height: wHeight * 0.25,
        width: dWidth,
        autoOpen: false,
        show: {
            effect: "blind",
            duration: 1000

        },
        hide: {
            effect: "explode",
            duration: 1000
        }
    });
});