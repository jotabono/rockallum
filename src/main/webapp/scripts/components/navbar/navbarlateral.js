$(document).ready(function() {

    var overlay = $('.sidebar-overlay');
    var openedMenuIcon = true;


    $('.barralateral').on('click', function() {

        var sidebar = $('#sidebar');

        $('.link').on('click', function(){
            sidebar.removeClass('open');
            $('.barralateral').html("<i style='color:grey'class='material-icons'>dehaze</i>");
            openedMenuIcon = true;
        });

        $("body").click(function(event) {

        });

        $('.container-fluid').on('click', function(){
            sidebar.removeClass('open');
            $('.barralateral').html("<i style='color:grey'class='material-icons'>dehaze</i>");
            openedMenuIcon = true;
        });

        $('#navbar-collapse').on('click', function(){
            sidebar.removeClass('open');
            $('.barralateral').html("<i style='color:grey'class='material-icons'>dehaze</i>");
            openedMenuIcon = true;
        });

        if(openedMenuIcon){
            $('.barralateral').html("<i style='color:black' class='material-icons closeicon'>close</i>");
            openedMenuIcon = false;
        }
        else{
            $('.barralateral').html("<i style='color:grey'class='material-icons'>dehaze</i>");
            openedMenuIcon = true;
        }
        sidebar.toggleClass('open');

        if ((sidebar.hasClass('sidebar-fixed-left') || sidebar.hasClass('sidebar-fixed-right')) && sidebar.hasClass('open')) {
            overlay.addClass('active');
        } else {
            overlay.removeClass('active');
        }
    });

    /*overlay.on('click', function() {
     $('#sidebar').removeClass('open');
     });*/

    overlay.on('click', function() {
        $(this).removeClass('active');
        $('#sidebar').removeClass('open');
        $('.barralateral').html("<i style='color:grey'class='material-icons'>dehaze</i>");
        openedMenuIcon = true;
    });
});
