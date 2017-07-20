<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <%--<div class="col-xs-10 col-xs-offset-1 card">--%>
            <div class="col-xs-12 card">
                <br/>

                <div><span class="glyphicon glyphicon-list"></span><b style="font-size: 20px"> &nbsp;Approval Waiting
                    List</b></div>
                <hr/>
                <br/><br/>
                <table id="setRequestPriorityTable" class="display nowrap table table-bordered" cellspacing="0"
                       width="100%">
                    <thead>
                    <tr>
                        <th width="20px">Id</th>
                        <th width="100px">Name</th>
                        <th width="120px">Approve Type</th>
                        <th width="30px">Request </br>Date</th>
                        <th width="30px">Require </br> Days</th>
                        <th width="30px">Delivery </br>Date</th>
                        <th width="80px">Attachment</th>
                    </tr>
                    </thead>
                </table>
                <br/>

                <button type="button" class="btn bg-red waves-war" id="addPriority" value="1"
                        title="Add To High Priority"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add To Priority Queue
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refresh" value="1" title="Refresh"><img
                        src="resources/images/refresh.png" width="16" height="16" border="0">&nbsp;Refresh
                </button>
                &nbsp;<br/><br/>
                &nbsp;<br/><br/>

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

            loadApproveData();

            function loadApproveData() {
                $('#setRequestPriorityTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/approval_details/approval_detailsList_set_priority",
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    'aoColumns': [
                        {
                            'sTitle': '',
                            "sClass": "checkbox-column",
                            'mData': 'id',
                            'mRender': function (id) {
                                return '<input class="getVal" style="position: static;"  type="checkbox" value="' + id + '" name="' + id + '" id="' + id + '">';
                            },
                            'sWidth': '20px',
                            "orderable": false
                        },
                        {
                            "mData": "requestName", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.requestName != null) {
                                var name = row.requestName.substr(0, 20);
                                return '<a href="/change_request_view?r_id=' + row.requestId + '" title="View Request">' + name + '</a>';
                            }
                            return "";
                        }
                        },
                        {"mData": "approveType", 'sWidth': '120px', "orderable": false},
                        {
                            "mData": "createdOn",
                            "render": function (data, type, row) {
                                var date = new Date(data);
                                var dateFormat = date.toISOString("mm").substr(0, 10);
                                return dateFormat;
                            }
                        },
                        {
                            "mData": "requiredDay", 'sWidth': '30px', "orderable": false,
                            "render": function (data, type, row, id) {
                                if (row.userType == messageResource.get('approve.user.type.itCoordinator', 'configMessageForUI')) return '<input class="requiredDay" type="number" step="any"  id=requiredDay' + row.id + ' >';
                                else {
                                    if (row.requiredDay != null) return row.requiredDay;
                                    else return "";
                                }
                            }
                        },
                        {
                            "mData": "deliverDate", 'sWidth': '30px',
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
                            "mData": "docPath", 'sWidth': '80px', "orderable": false,
                            "render": function (data, type, row, id) {
                                var mainPath = window.location.origin + "/pg";
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
                    "scrollY": "300",
                    "scrollX": true

                });
            }


            var table = $('#setRequestPriorityTable').DataTable();

            /* load table data on click refresh button*/

            $('#refresh').on('click', function () {
                table.ajax.url(messageResource.get('approval_details.priority.list.load.url', 'configMessageForUI')).load();
            });



            /* add priority to approval status */

            $('#addPriority').on('click', function () {
                var data = messageResource.get('approval_details.priority.update.msg', 'configMessageForUI');
                if (checkForNonSelectedRow()) {
                    $.dialogbox({
                        type: 'msg',
                        title: 'Confirm Title',
                        content: messageResource.get('approval_details.priority.set.confirm.msg', 'configMessageForUI'),
                        closeBtn: true,
                        btn: ['Confirm', 'Cancel'],
                        call: [
                            function () {
                                $.dialogbox.close();
                                callAjaxForAddOperation("", "", 0, "Message", getSelectedValue());
                            },
                            function () {
                                $.dialogbox.close();
                                uncheckedAllCheckBox();
                            }
                        ]
                    });
                }
                else showServerSideMessage(data, "", 0, "Message");
            });




            /* get selected checked box value */

            function getSelectedValue() {
                var approvalIds = [];
                $('input:checkbox.getVal').each(function () {
                    var sThisVal = (this.checked ? $(this).val() : "")
                    if (sThisVal) approvalIds.push(sThisVal);
                });
                return approvalIds;
            }



            /*  Ajax call for add operation */

            function callAjaxForAddOperation(part1, part2, icn, msg, approvalIds) {
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    'type': 'POST',
                    'url': messageResource.get('approval_details.priority.update.url', 'configMessageForUI'),
                    'data': JSON.stringify(approvalIds),
                    'dataType': 'json',
                    'success': function (d) {
                        if (d.successMsg) {
                            icn = 1;
                            part1 = d.successMsg;
                            showServerSideMessage(part1, part2, icn, msg);
                            table.ajax.url(messageResource.get('approval_details.priority.list.load.url', 'configMessageForUI')).load();
                        }
                        if (d.validationError) {
                            icn = 0;
                            msg = "";
                            msg = '<strong style="color: red">Error</strong>';
                            part2 = d.validationError;
                            uncheckedAllCheckBox();
                            showServerSideMessage(part1, part2, icn, msg);
                        }
                    },
                    'error': function (error) {
                        icn = 0;
                        msg = '<strong style="color: red">Error</strong>';
                        uncheckedAllCheckBox();
                        showServerSideMessage(part1, getErrorMessage(error), icn, msg);
                    }
                });

            }


        });
    </script>

</section>

<%@ include file="footer.jsp" %>
