$(function() {
    var icons = {  header: "ui-icon-circle-arrow-e",
        activeHeader: "ui-icon-circle-arrow-s"
    };

    $( "#accordion" ).accordion({
        icons: icons, collapsible:true, active:false
    });

    $('#accordion h3.ui-accordion-header').each(function() {
        $(this).next().slideToggle();
        $(this).toggleClass('ui-state-active ui-state-default.accordionMenu');
        $(this).find('.ui-icon').toggleClass('ui-icon-circle-arrow-e ui-icon-circle-arrow-s');
    });

    $('#accordion h3.ui-accordion-header').click(function() {
        $(this).next().slideToggle();
        $(this).toggleClass('ui-state-active ui-state-default.accordionMenu');
        $(this).find('.ui-icon').toggleClass('ui-icon-circle-arrow-e ui-icon-circle-arrow-s');
    });

    $( "#toggle" ).button().click(function() {
        if ( $( "#accordion" ).accordion("option", "icons" ) ) {
            $( "#accordion" ).accordion( "option", "icons", null );
        } else {
            $( "#accordion" ).accordion( "option", "icons", icons );
        }
    });
});