var myLayout;

$.getDocHeight = function(){
    var myWidth = 0, myHeight = 0;
    if( typeof( window.innerWidth ) == 'number' ) {
        //Non-IE
        myWidth = window.innerWidth;
        myHeight = window.innerHeight;
    } else if( document.documentElement && ( document.documentElement.clientWidth || document.documentElement.clientHeight ) ) {
        //IE 6+ in 'standards compliant mode'
        myWidth = document.documentElement.clientWidth;
        myHeight = document.documentElement.clientHeight;
    } else if( document.body && ( document.body.clientWidth || document.body.clientHeight ) ) {
        //IE 4 compatible
        myWidth = document.body.clientWidth;
        myHeight = document.body.clientHeight;
    }
    return myHeight;
};

$.setPane = function() {
    var p = $.getDocHeight();
    var e = $("#header").height();
    $("#myPane").height(p - e - 57);
}

$(window).resize(function() {
    $.setPane();
    if (myLayout != null) {
        myLayout.resizeAll();
    }
});

$(function() {
    $.setPane();
    myLayout = $('#myPane').layout({ applyDefaultStyles: true,
            west__size: 200,
            west__minSize:200,
            center__minWidth:600,
            center__onresize_end:function() { }
        }
    );
});