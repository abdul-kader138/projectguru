<script type="text/javascript" src="resources/node_modules/message/messageResource.js"></script>
<script type="text/ecmascript" src="resources/node_modules/datatable/jquery.dataTables.min.js"></script>
<script src="resources/node_modules/adminbsb/dialog/js/dialogbox.js"></script>
<script src="resources/node_modules/adminbsb/dialog/ajax-loading.js"></script>


<!-- Bootstrap Core Js -->
<script src="resources/node_modules/adminbsb/bootstrap/js/bootstrap.js"></script>

<!-- Slimscroll Plugin Js -->
<script src="resources/node_modules/adminbsb/jquery-slimscroll/jquery.slimscroll.js"></script>

<!-- Waves Effect Plugin Js -->
<script src="resources/node_modules/adminbsb/node-waves/waves.js"></script>

<!-- Jquery CountTo Plugin Js -->
<script src="resources/node_modules/adminbsb/jquery-countto/jquery.countTo.js"></script>

<!-- Morris Plugin Js -->
<script src="resources/node_modules/adminbsb/raphael/raphael.min.js"></script>
<script src="resources/node_modules/adminbsb/morrisjs/morris.js"></script>

<!-- ChartJs -->
<script src="resources/node_modules/adminbsb/chartjs/Chart.bundle.js"></script>
<script src="resources/node_modules/imageloader/loadimg.min.js"></script>


<%--<!-- Flot Charts Plugin Js -->--%>
<script src="resources/node_modules/adminbsb/flot-charts/jquery.flot.js"></script>
<script src="resources/node_modules/adminbsb/flot-charts/jquery.flot.resize.js"></script>
<script src="resources/node_modules/adminbsb/flot-charts/jquery.flot.pie.js"></script>
<script src="resources/node_modules/adminbsb/flot-charts/jquery.flot.categories.js"></script>
<script src="resources/node_modules/adminbsb/flot-charts/jquery.flot.time.js"></script>

<!-- Sparkline Chart Plugin Js -->
<script src="resources/node_modules/adminbsb/jquery-sparkline/jquery.sparkline.js"></script>

<!-- Custom Js -->
<script src="resources/node_modules/adminbsb/js/admin.js"></script>
<script src="resources/node_modules/adminbsb/js/pages/index.js"></script>


<script src="resources/node_modules/custom-js/custom.js"></script>

<script>

    /* block UI when ajax call start */

    $(document).ajaxStart(function () {
        $("body").addClass('ajaxLoading');
        $("div").addClass('ajaxLoading');
        $("a").addClass('ajaxLoading');
        $("ul li").addClass('ajaxLoading');
        $("ul").addClass('ajaxLoading');
    });


    /* unblock UI after ajax call */

    $(document).ajaxStop(function () {
        $("body").removeClass('ajaxLoading');
        $("div").removeClass('ajaxLoading');
        $("a").removeClass('ajaxLoading');
        $("ul li").removeClass('ajaxLoading');
        $("ul").removeClass('ajaxLoading');
    });

</script>
<noscript>

    <div class="col-sm-10 col-sm-offset-1">
        <div class="panel panel-danger">
            <div class="panel-body">
                <b> For full functionality of this site it is necessary to enable JavaScript.<br/>
                    Here are the instructions -></b> <a href="http://www.enable-javascript.com/" target="_blank">
                How to enable JavaScript in your web browser</a>.
            </div>
        </div>
    </div>
    <style>
        .mainContent {
            display: none;
        }
    </style>
</noscript>
</body>

</html>
