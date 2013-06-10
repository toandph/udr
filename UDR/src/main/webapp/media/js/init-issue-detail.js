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

    $(".CodeMirror").height(p - e - 114);
    $(".CodeMirror-scroll").height(p - e - 114);
}

$(window).resize(function() {
    $.setPane();
    if (myLayout != null) {
        myLayout.resizeAll();
    }
});

var editor;

$(function() {
    $( "#tabs" ).tabs();
    $("#previous").button({icons:{primary:"ui-icon-triangle-1-w"}, text: false});
    $("#back-to-list").button({icons:{primary:"ui-icon-arrowthick-1-w"}, text: false});
    $("#next").button({icons:{primary:"ui-icon-triangle-1-e"}, text: false});

    var textarea = document.getElementById("code");
    var codeMirrorOptions = {
        mode:"text/x-csrc",
        styleActiveLine:true,
        lineNumbers:true,
        lineWrapping:false
    };

    editor = CodeMirror.fromTextArea(textarea, codeMirrorOptions);
    var mainLine = parseInt($("#error-main-line").html()) - 1;

    $.setPane();
    myLayout = $('#myPane').layout({ applyDefaultStyles: true,
        west__size: 350,
        west__minSize:250,
        center__minWidth:500,
        center__minSize:500,
        center__onresize_end:function() {
        }
    });

    editor.addLineClass(mainLine, "background", "CodeMirror-majorLine");
    $(".traceId").each(function() {
        var minorLine = parseInt($(this).html()) - 1;
        if ($("#issue-file").html() == $(this).attr("file")) {
            editor.addLineClass(minorLine, "background", "CodeMirror-minorLine");
        }
    });
    var editors = $('.CodeMirror-scroll');
    editors.scrollTop(0).scrollTop(mainLine * 11 - Math.round(editors.height()/2));

});