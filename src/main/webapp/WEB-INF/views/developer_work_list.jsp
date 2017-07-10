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
                                <table id="requestTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                                    <thead>
                                    <tr>
                                        <th width="100px">Name</th>
                                        <th width="100px">Company</th>
                                        <th width="100px">Product</th>
                                        <th width="100px">Category</th>
                                        <th width="80px"> Status</th>
                                        <th width="60px">Delivery</br> Date</th>
                                        <th width="60px">Deployed</br> On</th>
                                        <th width="100px">Decline Cause</th>
                                        <th width="80px">Attachment</th>
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
            /* set nav bar color */
            changeNavColor();
            var colorName = localStorage.colorName;
            setNavColor(colorName);

            /* Enable page loader */
            var loading = $.loading();

            /* Load datatable Value */
            $('#requestTable').DataTable({
                "sAjaxSource": "http://localhost:8080/developer_work_status/developer_work_statusList",
                "sAjaxDataProp": "",
                "order": [[0, "asc"]],
                'aoColumns': [
                    {
                        "mData": "name", 'sWidth': '100px', "render": function (data, type, row, id) {
                        if (row.name != null) {
                            var name = row.name.substr(0, 25);
                            return '<a href="/change_request_view?r_id='+row.id +'" title="View Request">'+name+'</a>';
                        }
                        return "";
                    }
                    },
                    {
                        "mData": "category.company.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                        if (row.category.company.name != null) {
                            var companyName = row.category.company.name.substr(0, 25);
                            return companyName;
                        }
                        return "";
                    }
                    },

                    {
                        "mData": "category.product.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                        if (row.category.product.name != null) {
                            var productName = row.category.product.name.substr(0, 25);
                            return productName;
                        }
                        return "";
                    }
                    },
                    {
                        "mData": "category.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                        if (row.category.name != null) {
                            var categoryName = row.category.name.substr(0, 25);
                            return categoryName;
                        }
                        return "";
                    }
                    },
                    {
                        "mData": "wipStatus", 'sWidth': '80px', "orderable": false,
                        "render": function (data, type, row, id) {
                            return "Waiting For Deployment";
                        }
                    },
                    {
                        "mData": "deliverDate", 'sWidth': '60px',
                        "render": function (data, type, row, id) {
                            var date = new Date(row.deliverDate);
                            var dateFormat = date.toISOString("mm").substr(0, 10);
                            return dateFormat;
                        }
                    },
                    {
                        "mData": "deployedOn", 'sWidth': '60px',"orderable": false,
                        "render": function (data, type, row, id) {
                            if (row.deployedOn != null) {
                                var date = new Date(row.deployedOn);
                                var dateFormat = date.toISOString("mm").substr(0, 10);
                                return dateFormat;
                            }
                            return "";
                        }
                    },
                    {
                        "mData": "declineCause", 'sWidth': '100px', "orderable": false,
                        "render": function (data, type, row, id) {
                            if (row.declineCause != null) {
                                var declineCause = row.declineCause.substr(0, 40);
                                var declineCause2 = row.declineCause.substr(41, 80);
                                var declineCause3 = row.declineCause.substr(81, 120);
                                return '<label>'+declineCause+'</br>'+declineCause2+'</br>'+declineCause3+'</label>';
                            }
                            return "";
                        }
                    },
                    {
                        "mData": "docPath", 'sWidth': '80px', "orderable": false,
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


            /* Calculate required Days */
            function requiredDays(startDate, Enddate) {
                var ONE_DAY = 1000 * 60 * 60 * 24;
                var difference_ms = Math.abs(Enddate - startDate);
                return Math.round(difference_ms / ONE_DAY);
            }

        });

    </script>
</section>
<%@ include file="footer.jsp" %>
