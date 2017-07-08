<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <%--<div class="col-xs-10 col-xs-offset-1 card">--%>
            <div class="col-xs-12 card">
                <div><h4>Approval Waiting List</h4></div>
                <hr/>
                <br/><br/>
                <input type="hidden" value="" id="date"/>
                <table id="approveTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="100px">Name</th>
                        <th width="100px">Company</th>
                        <th width="100px">Product</th>
                        <th width="100px">Category</th>
                        <th width="120px">Approve Type</th>
                        <th width="30px">Request </br>Date</th>
                        <th width="30px">Require </br> Days</th>
                        <th width="30px">Delivery </br>Date</th>
                        <th width="80px">Attachment</th>
                        <th></th>
                        <th></th>
                        <th></th>
                    </tr>
                    </thead>
                </table>
                <br/>
            </div>
        </div>

        <%--end of table div--%>


        <br/><br/><br/>

    </div>

    <script>
        $(document).ready(function () {

            /* set nav bar color */
            changeNavColor();
            var colorName = localStorage.colorName;
            setNavColor(colorName);

            /* Enable page loader */
            var loading = $.loading();

            /*Initialize Page Value*/
            var companyGb;
            loadApproveData();

            /* populate Category list when page load */

            function loadApproveData() {
                $('#approveTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/approval_details/approval_detailsList",
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    'aoColumns': [
                        {
                            "mData": "requestName", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.requestName != null) {
                                var name = row.requestName.substr(0, 20);
                                return '<a href="/change_request_view?r_id='+row.requestId +'" title="View Request">'+name+'</a>';
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "category.company.name", 'sWidth': '100px', "orderable": false, "render": function (data, type, row, id) {
                            if (row.category.company.name != null) {
                                var companyName = row.category.company.name.substr(0, 20);
                                return companyName;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "category.product.name", 'sWidth': '100px', "orderable": false, "render": function (data, type, row, id) {
                            if (row.category.product.name != null) {
                                var productName = row.category.product.name.substr(0, 20);
                                return productName;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "category.name", 'sWidth': '100px', "orderable": false, "render": function (data, type, row, id) {
                            if (row.category.name != null) {
                                var categoryName = row.category.name.substr(0, 20);
                                return categoryName;
                            }
                            return "";
                        }
                        },
                        {"mData": "approveType", 'sWidth': '30px', "orderable": false},
                        {
                            "mData": "createdOn",
                            "render": function (data, type, row) {
                                var date = new Date(data);
                                var dateFormat = date.toISOString("mm").substr(0, 10);
                                return dateFormat;
                            }
                        },
                        {
                            "mData": "requiredDay",'sWidth': '30px', "orderable": false,
                            "render": function (data, type, row, id) {
                                if (row.userType == messageResource.get('approve.user.type.itCoordinator', 'configMessageForUI')) return '<input class="requiredDay" type="number" step="any"  id=requiredDay' + row.id + ' >';
                                else {
                                    if (row.requiredDay != null) return row.requiredDay;
                                    else return "";
                                }
                            }
                        },
                        {
                            "mData": "deliverDate",'sWidth': '30px',
                            "render": function (data, type, row, id) {
                                    if (row.deliverDate != null) {
                                        var date = new Date(row.deliverDate);
                                        var dateFormat = date.toISOString("mm").substr(0, 10);
                                        return dateFormat;
                                    }
                                    else return messageResource.get('approve.delivery.date.column.msg', 'configMessageForUI');
                                }
                        },
                        {
                            "mData": "docPath",'sWidth': '80px', "orderable": false,
                            "render": function (data, type, row, id) {
                                var mainPath=document.origin+"/PG";
                                return '<a href="'+mainPath+row.docPath+'" download>'+'Download</a>'
                            }
                        },
                        {
                            "mData": "id", "orderable": false,
                            "render": function (id, type) {
                                return '<button type="button" class="approve btn bg-light-blue waves-war"  value="1" title="Approve request" >&nbsp;Approve</button>';
                            }
                        },

                        {
                            "mData": "userType", "orderable": false,
                            "render": function (userType) {
                                if (userType == messageResource.get('approve.user.type.checked', 'configMessageForUI') || userType == messageResource.get('approve.user.type.approve', 'configMessageForUI')) return '<button type="button" class="delete btn bg-red waves-war"  value="1" title="Delete Request" >&nbsp;Delete</button>';
                                else return "";
                            }
                        },
                        {
                            "mData": "userType", "orderable": false,
                            "render": function (data, type, row, id) {
                                if (row.userType == messageResource.get('approve.user.type.request.by.acknowledgement', 'configMessageForUI') || row.userType == messageResource.get('approve.user.type.checked.by.acknowledgement', 'configMessageForUI')) return '<a class="decline btn bg-brown waves-war"  href="/decline?a_id='+row.id +'&r_id='+row.requestId+'&ver='+row.version +'" title="Decline Request"> Decline </a>';
                                else return "";
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
            }

            var table = $('#approveTable').DataTable();


            /* Create object for update Approval */

            $('#approveTable tbody').on('click', 'button', function () {
                var blankDeliveryDate="";
                var obj = this;
                var data = table.row($(this).parents('tr')).data();
                var approvalObj = new Object();
                var part1 = "";
                var part2 = "";
                var icn = 0;
                var msg = "Message";
                var version = data.version;
                approvalObj.id = data.id;
                approvalObj.name = data.requestName;
                approvalObj.requestId = data.requestId;
                approvalObj.version = version;
                var day = $('#requiredDay' + data.id).val();
                if(day !="") approvalObj.day = day;
                if (obj.className.split(' ')[0] == messageResource.get('button.name.approve', 'configMessageForUI')) {
                    if (data.userType == messageResource.get('approve.user.type.itCoordinator', 'configMessageForUI')) {
                        if (checkApproveDate(approvalObj.day)) callAjaxForEditOperation(part1, part2, icn, msg, approvalObj);
                    } else {
                        callAjaxForEditOperation(part1, part2, icn, msg, approvalObj);
                    }
                }
                if (obj.className.split(' ')[0] == messageResource.get('button.name.delete', 'configMessageForUI')) deleteApprovalRequest(approvalObj);
            });



            /* delete operation */

            function deleteApprovalRequest(approvalObj) {
                var part1 = "";
                var part2 = "";
                var icn = 0;
                var msg = "Message";
                    $.dialogbox({
                        type: 'msg',
                        title: 'Confirm Title',
                        content: messageResource.get('approval.delete.confirm.msg', 'configMessageForUI'),
                        closeBtn: true,
                        btn: ['Confirm', 'Cancel'],
                        call: [
                            function () {
                                $.dialogbox.close();
                                callAjaxForDeleteOperation(part1, part2, icn, msg, approvalObj);

                            },
                            function () {
                                $.dialogbox.close();
                            }
                        ]
                    });
            }



            /*  Ajax call for edit operation */

            function callAjaxForEditOperation(part1, part2, icn, msg, approvalObj) {
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    'type': 'POST',
                    'url': messageResource.get('approval_details.edit.url', 'configMessageForUI'),
                    'data': JSON.stringify(approvalObj),
                    'dataType': 'json',
                    'success': function (d) {
                        if (d.successMsg) {
                            icn = 1;
                            part1 = d.successMsg;
                            showServerSideMessage(part1, part2, icn, msg);
                            table.ajax.url(messageResource.get('approval_details.list.load.url', 'configMessageForUI')).load();
                            $("#notificationCount").html(d.notificationCount);
                        }
                        if (d.validationError) {
                            icn = 0;
                            msg = "";
                            msg = '<strong style="color: red">Error</strong>';
                            part2 = d.validationError;
                            showServerSideMessage(part1, part2, icn, msg);
                        }
                    },
                    'error': function (error) {
                        console.log(error);
                        icn = 0;
                        msg = '<strong style="color: red">Error</strong>';
                        showServerSideMessage(part1, getErrorMessage(error), icn, msg);
                    }
                });
            }






            /*  Ajax call for delete operation */

            function callAjaxForDeleteOperation(part1, part2, icn, msg, approvalObj) {
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    'type': 'POST',
                    'url': messageResource.get('approval_details.delete.url', 'configMessageForUI'),
                    'data': JSON.stringify(approvalObj),
                    'dataType': 'json',
                    'success': function (d) {
                        if (d.successMsg) {
                            icn = 1;
                            msg = "";
                            part1 = d.successMsg;
                            showServerSideMessage(part1, part2, icn, msg);
                            table.ajax.url(messageResource.get('approval_details.list.load.url', 'configMessageForUI')).load();
                            $("#notificationCount").html(d.notificationCount);
                        }
                        if (d.validationError) {
                            icn = 0;
                            msg = '<strong style="color: red">Error</strong>';
                            part2 = d.validationError;
                            showServerSideMessage(part1, part2, icn, msg);
                        }
                    },
                    'error': function (error) {
                        uncheckedAllCheckBox();
                        icn = 0;
                        msg = '<strong style="color: red">Error</strong>';
                        showServerSideMessage(part1, getErrorMessage(error), icn, msg);
                    }
                });
            }



            /* check for valid no of required days */

            function checkApproveDate(obj) {
                var isValid = true;
                if (obj == null || obj == 0 || obj == undefined) {
                    showServerSideMessage(messageResource.get('approve.delivery.day.msg', 'configMessageForUI'), "", 0, "Message");
                    isValid = false;
                }

                var valid = /^\d{1,3}?$/.test(obj);
                if(!valid) isValid = false;
                return isValid;
            }


        });

    </script>

</section>

<%@ include file="footer.jsp" %>
