<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>Role List</h4></div>
                <hr/>
                <br/><br/>
                <table id="roleTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="15px">id</th>
                        <th width="200px">Role Name</th>
                        <th width="200px">Description</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editRole" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteRole" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshRole" value="1" title="Refresh"><img
                        src="resources/images/refresh.png" width="16" height="16" border="0">&nbsp;Refresh
                </button>
                &nbsp;<br/><br/>
                &nbsp;<br/><br/>
            </div>

            <br/>
        </div>

        <%--end of table div--%>


        <br/><br/><br/>

        <%--start of save/update modal--%>


        <div class="row clearfix" id="roleForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>
                                <legend><strong>Role Setting</strong></legend>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="name">Name :</label>

                                    <div class="col-md-6">
                                        <input type="hidden" class="form-control" id="id" name="id" value="0" required>
                                        <input type="hidden" class="form-control" id="version" name="version" value="0"
                                               required>
                                        <input id="name" name="name" type="text" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="nameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>


                                <!-- Textarea -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="description">Description :</label>

                                    <div class="col-md-6">
                                        <textarea id="description" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="description" rows="6" cols="20"></textarea>
                                        <label id="descriptionValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>


                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveRole"></label>

                                    <div class="col-md-4">
                                        <button id="saveRole" name="saveRole" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateRole" name="updateRole" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetRole" name="resetRole"
                                                class="btn bg-grey"
                                                type="button">Cancel
                                        </button>
                                    </div>
                                </div>

                            </fieldset>
                        </form>

                    </div>
                </div>

            </div>
        </div>


        <script type="text/javascript">
            $(document).ready(function () {

                /* set nav bar color */
                changeNavColor();
                var colorName = localStorage.colorName;
                setNavColor(colorName);

                /* Enable page loader */
                var loading = $.loading();

                /*Initialize Page Value*/
                initFormValidationMsg();
                initializeRoleForm();
                $("saveRole").show();
                $("#updateRole").hide();
                var companyGb;


                /* populate Role list when page load */

                $('#roleTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/roles/rolesList",
//                    "sAjaxSource": messageResource.get('company.list.load.url', 'configMessageForUI'),
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    'aoColumns': [
                        {
                            'sTitle': '',
                            "sClass": "checkbox-column",
                            'mData': 'id',
                            'mRender': function (id) {
                                return '<input class="getVal" style="position: static;"  type="checkbox" name="' + id + '" id="' + id + '">';
                            },
                            'sWidth': '15px',
                            "orderable": false
                        },
                        {
                            "mData": "name", 'sWidth': '200px', "render": function (data, type, row, id) {
                            if (row.name != null) {
                                var name = row.name.substr(0, 25);
                                return name;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "description",
                            'sWidth': '300px',
                            "orderable": false,
                            "render": function (data, type, row, id) {
                                if (row.description != null) {
                                    var description = row.description.substr(0, 30);
                                    return description;
                                }
                                return "";
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


                /* Save roles data using ajax */

                $("#saveRole").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var roles = new Object();
                    roles.id = $("#id").val();
                    roles.version = $("#version").val();
                    roles.name = $("#name").val();
                    roles.description = $("#description").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, roles);
                });


                /* Update roles data using ajax */

                $('#editRole').click(function () {
                    document.getElementById('roleForm').style.display = "none";
                    initializeRoleForm();
                    initFormValidationMsg();
                    var newRole = new Object();
                    newRole = companyGb;
                    companyGb = null;
                    var data = messageResource.get('roles.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newRole == null)showServerSideMessage(data, "", 0, "Message");
                    else setDataToRoleForm(newRole);
                });

                var table = $('#roleTable').DataTable();

                $("#updateRole").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var role = new Object();
                    role.id = $("#id").val();
                    role.version = $("#version").val();
                    role.name = $("#name").val();
                    role.description = $("#description").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, role);

                });


                /* Delete Role data using ajax */

                $("#deleteRole").click(function (event) {
                    document.getElementById('roleForm').style.display = "none";
                    initializeRoleForm();
                    initFormValidationMsg();
                    var newRole = new Object();
                    newRole = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('roles.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newRole == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('roles.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newRole);

                                },
                                function () {
                                    $.dialogbox.close();
                                    uncheckedAllCheckBox();
                                }
                            ]
                        });

                        window.location.href = "#viewTableData";
                    }

                });


                /* DataTable select value send to global var */

                $('#roleTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetRole').on('click', function () {
                    companyGb = null;
                    initializeRoleForm();
                    initFormValidationMsg();
                    $('#saveRole').show();
                    $('#updateRole').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('roleForm').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshRole').on('click', function () {
                    initializeRoleForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('roles.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('roleForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newRoles) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('roles.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newRoles.id),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('roles.list.load.url', 'configMessageForUI')).load();
                            }
                            if (d.validationError) {
                                icn = 0;
                                msg = '<strong style="color: red">Error</strong>';
                                part2 = d.validationError;
                                uncheckedAllCheckBox();
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


                /*  Ajax call for edit operation */

                function callAjaxForEditOperation(part1, part2, icn, msg, roles) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('roles.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(roles),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeRoleForm();
                                window.location.href = "#viewTableData";
                                $("#updateRole").hide();
                                $("#saveRole").show();
                                document.getElementById('roleForm').style.display = "none";
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('roles.list.load.url', 'configMessageForUI')).load();
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
                    company = null;

                }


                /*  Ajax call for save operation */

                function callAjaxForAddOperation(part1, part2, icn, msg, role) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('roles.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(role),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeRoleForm();
                                setNewDataTableValue(d.roles, table);
                                window.location.href = "#viewTableData";
                                showServerSideMessage(part1, part2, icn, msg);
                                document.getElementById('roleForm').style.display = "none";
                            }
                            if (d.validationError) {
                                icn = 0;
                                msg = '<strong style="color: red">Error</strong>';
                                part2 = d.validationError;
                                showServerSideMessage(part1, part2, icn, msg);
                            }
                        },
                        'error': function (error) {
                            icn = 0;
                            msg = '<strong style="color: red">Error</strong>';
                            showServerSideMessage(part1, getErrorMessage(error), icn, msg);
                        }
                    });

                }


                /* Initialize html form value */

                function initializeRoleForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $("#name").val("");
                    $("#description").val("");
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var description = $("#description").val();
                    if (name == null || name.trim().length == 0) {
                        $("#nameValidation").text("Name is required");
                        isValid = false;
                    }
                    if (description == null || description.trim().length == 0) {
                        $("#descriptionValidation").text("Description is required");
                        isValid = false;
                    }

                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#nameValidation").text("");
                    $("#descriptionValidation").text("");

                }


                /* move to add new role div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('roleForm').style.display = "block";
                    companyGb = null;
                    $("#updateRole").hide();
                    $("#saveRole").show();
                    initializeRoleForm();
                    initFormValidationMsg();
                    uncheckedAllCheckBox();
                    window.location.href = "#roleForm";
                });


                /* Set new created role value to DataTable*/

                function setNewDataTableValue(role, table) {
                    table.row.add({
                        "id": role.id,
                        "name": role.name,
                        "version": role.version,
                        "description": role.description
                    }).draw();

                }


                /* set selected row data to role form for edit */

                function setDataToRoleForm(role) {
                    document.getElementById('roleForm').style.display = "block";
                    $("#updateRole").show();
                    $("#saveRole").hide();
                    $("#id").val(role.id);
                    $("#name").val(role.name);
                    $("#description").val(role.description);
                    $("#version").val(role.version);
                    window.location.href = "#roleForm";
                }


            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>