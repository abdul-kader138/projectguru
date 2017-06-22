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
                <table id="approveTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <%--<th width="15px">id</th>--%>
                        <th width="200px">Name</th>
                        <th width="200px">Description</th>
                        <th width="200px">Company</th>
                        <th width="200px">Product</th>
                        <th width="200px">Category</th>
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

            var loading = $.loading();
            var companyGb;


            /* populate Category list when page load */

            $('#approveTable').DataTable({
                "sAjaxSource": "http://localhost:8080/approval_details/approval_detailsList",
                "sAjaxDataProp": "",
                "order": [[0, "asc"]],
                'aoColumns': [
                    {"mData": "requestName", 'sWidth': '150px'},
                    {"mData": "requestDetails", 'sWidth':'250px'},
                    {"mData": "company.name", 'sWidth': '250px'},
                    {"mData": "product.name", 'sWidth': '250px'},
                    {"mData": "category.name", 'sWidth': '250px'},
                    {
                        "mData": "id",
                        "render": function (id, type) {
                            return '<button type="button" class="approve btn bg-light-blue waves-war"  value="1" title="Approve request" >&nbsp;Approve</button>';
                        }
                    },

                    {
                        "mData": "userType",
                        "render": function (userType) {
                            if(userType == messageResource.get('approve.user.type.checked', 'configMessageForUI') || userType == messageResource.get('approve.user.type.approve', 'configMessageForUI')) return '<button type="button" class="delete btn bg-red waves-war"  value="1" title="Delete Request" >&nbsp;Delete</button>';
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

            var table = $('#approveTable').DataTable();
            $('#approveTable tbody').on( 'click', 'button', function () {
                var obj=this;
                var data = table.row( $(this).parents('tr') ).data();
                var approvalObj=new Object();
                var part1 = "";
                var part2 = "";
                var icn = 0;
                var msg = "Message";
                var version=data.version;
                approvalObj.id=data.id;
                approvalObj.version=version;
                if(obj.className.split(' ')[0] == messageResource.get('button.name.approve', 'configMessageForUI')) callAjaxForEditOperation(part1,part2,icn,msg,approvalObj);
                if(obj.className.split(' ')[0] == messageResource.get('button.name.delete', 'configMessageForUI')) deleteApproval(part1,part2,icn,msg,approvalObj);
            } );


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
                            $("#notificationCount").val(d.notificationCount);
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

            function deleteApproval(part1, part2, icn, msg, approvalObj){
                $.dialogbox({
                    type: 'msg',
                    title: 'Confirm Title',
                    content: messageResource.get('approval_details.delete.confirm.msg', 'configMessageForUI'),
                    closeBtn: true,
                    btn: ['Confirm', 'Cancel'],
                    call: [
                        function () {
                            $.dialogbox.close();
//                            callAjaxForDeleteOperation(part1, part2, icn, msg, approvalObj);

                        },
                        function () {
                            $.dialogbox.close();
                        }
                    ]
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
                    'data': JSON.stringify(approvalObj.id),
                    'dataType': 'json',
                    'success': function (d) {
                        if (d.successMsg) {
                            icn = 1;
                            msg = "";
                            part1 = d.successMsg;
                            showServerSideMessage(part1, part2, icn, msg);
                            table.ajax.url(messageResource.get('approval_details.list.load.url', 'configMessageForUI')).load();
                            $("#notificationCount").val(d.notificationCount);
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
        });




    </script>

</section>

<%@ include file="footer.jsp" %>
