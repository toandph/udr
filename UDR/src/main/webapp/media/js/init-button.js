$(function() {
    $("button, input[type='button'], input[type='submit']").button();

    $("#previous").button({icons:{primary:"ui-icon-triangle-1-w"}, text: false});
    $("#seek-first").button({icons:{primary:"ui-icon-seek-first"}, text: false});
    $("#seek-end").button({icons:{primary:"ui-icon-seek-end"}, text: false});
    $("#next").button({icons:{primary:"ui-icon-triangle-1-e"}, text: false});
});