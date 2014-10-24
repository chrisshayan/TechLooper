$(document).keyup(function(e) {
    if (e.keyCode == 27) {
        var btnClose = $(".btn-close");
        if (btnClose.length == 1) {
            btnClose.click();
        }
    }
});
