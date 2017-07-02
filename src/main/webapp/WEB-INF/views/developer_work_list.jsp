<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">
        <div class="block-header">
            <h2>DASHBOARD</h2>
        </div>
        <!-- Widgets -->
        <div class="row clearfix">
            <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
                <div class="info-box bg-pink hover-expand-effect">
                    <div class="icon">
                        <i class="material-icons">playlist_add_check</i>
                    </div>
                    <div class="content">
                        <div class="text">TOTAL USER REQUEST</div>
                        <div class="number count-to" id="requestAll" data-from="0" data-to="${totalRequest}"
                             data-speed="15"
                             data-fresh-interval="20"></div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
                <div class="info-box bg-cyan hover-expand-effect">
                    <div class="icon">
                        <i class="material-icons">help</i>
                    </div>
                    <div class="content">
                        <div class="text">REQUEST WORK IN PROGRESS</div>
                        <div class="number count-to" id="requestOpen" data-from="0" data-to="${totalRequestOpen}"
                             data-speed="20"
                             data-fresh-interval="20"></div>
                    </div>
                </div>
            </div>
            <div class="col-lg-4 col-md-4 col-sm-6 col-xs-12">
                <div class="info-box bg-light-green hover-expand-effect">
                    <div class="icon">
                        <i class="material-icons">forum</i>
                    </div>
                    <div class="content">
                        <div class="text">REQUEST COMPLETED</div>
                        <div class="number count-to" id="requestDone" data-from="0" data-to="${totalRequestDone}"
                             data-speed="20"
                             data-fresh-interval="20"></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- #END# Widgets -->
        <!-- Task List -->
        <div class="row clearfix">
            <!-- Task Info -->
            <div class="col-xs-12">
                <div class="card">
                    <div class="header">
                    </div>
                    <div class="body">
                        <div class="table-responsive">
                            <table class="table table-hover dashboard-task-infos">
                                <table id="requestTable" class="display nowrap" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th width="200px">Name</th>
                                        <th width="500px">Description</th>
                                        <th width="250px">Company</th>
                                        <th width="250px">Product</th>
                                        <th width="250px">Category</th>
                                        <th width="120px"> Status</th>
                                        <th width="200px"> Delivery Date</th>
                                        <th width="100px">Attachment</th>
                                    </tr>
                                    </thead>
                                </table>

                            </table>
                        </div>
                    </div>
                </div>
            </div>
        </div>

        <!-- CPU Usage -->
        <div class="row clearfix">
            <div class="col-xs-12 col-sm-12 col-md-12 col-lg-12">
                <div class="card">
                    <div class="header">
                        <div class="row clearfix">
                            <div class="col-xs-12 col-sm-6">
                                <h2>CPU USAGE (%)</h2>
                            </div>
                            <div class="col-xs-12 col-sm-6 align-right">
                                <div class="switch panel-switch-btn">
                                    <span class="m-r-10 font-12">REAL TIME</span>
                                    <label>OFF<input type="checkbox" id="realtime" checked><span
                                            class="lever switch-col-cyan"></span>ON</label>
                                </div>
                            </div>
                        </div>
                        <ul class="header-dropdown m-r--5">
                            <li class="dropdown">
                                <a href="javascript:void(0);" class="dropdown-toggle" data-toggle="dropdown"
                                   role="button" aria-haspopup="true" aria-expanded="false">
                                    <i class="material-icons">more_vert</i>
                                </a>
                                <ul class="dropdown-menu pull-right">
                                    <li><a href="javascript:void(0);">Action</a></li>
                                    <li><a href="javascript:void(0);">Another action</a></li>
                                    <li><a href="javascript:void(0);">Something else here</a></li>
                                </ul>
                            </li>
                        </ul>
                    </div>
                    <div class="body">
                        <div id="real_time_chart" class="dashboard-flot-chart"></div>
                    </div>
                </div>
            </div>
        </div>
        <!-- #END# CPU Usage -->

    </div>
    <script>
        $(document).ready(function () {
            var loading = $.loading();
            $('#requestTable').DataTable({
                "sAjaxSource": "http://localhost:8080/developer_work_status/developer_work_statusList",
                "sAjaxDataProp": "",
                "order": [[0, "asc"]],
                'aoColumns': [
                    {"mData": "name", 'sWidth': '180px'},
                    {"mData": "description", 'sWidth': '480px'},
                    {"mData": "company.name", 'sWidth': '200px'},
                    {"mData": "product.name", 'sWidth': '200px'},
                    {"mData": "category.name", 'sWidth': '200px'},
                    {
                        "mData": "wipStatus", 'sWidth': '100px',
                        "render": function (data, type, row, id) {
                            return "Waiting For Deployment";
                        }
                    },
                    {
                        "mData": "deliverDate", 'sWidth': '180px',
                        "render": function (data, type, row, id) {
                            var date = new Date(row.deliverDate);
                            var dateFormat = date.toISOString("mm").substr(0, 10);
                            return dateFormat;
                        }
                    },
                    {
                        "mData": "docPath", 'sWidth': '80px',
                        "render": function (data, type, row, id) {
                            var mainPath = document.origin + "/PG";
                            return '<a href="' + mainPath + row.docPath + '" download>' + 'Download</a>'
                        }
                    }
                ],
                'aaSorting': [[0, 'asc']],
                "columnDefs": [{}],
                "cache": false,
                "bPaginate": true,
                "bLengthChange": true,
                "bFilter": true,
                "bInfo": false,
                "bAutoWidth": true,
                "scrollY": "400",
                "scrollX": true

            });


            function requiredDays(startDate, Enddate) {
                var ONE_DAY = 1000 * 60 * 60 * 24;
                var difference_ms = Math.abs(Enddate - startDate);
                console.log(startDate);
                console.log(Enddate);
                console.log(difference_ms);
                console.log(Math.round(difference_ms / ONE_DAY));
                return Math.round(difference_ms / ONE_DAY);
            }

            $("#approval_details_notification").hide();
        });

    </script>
</section>
<%@ include file="footer.jsp" %>
