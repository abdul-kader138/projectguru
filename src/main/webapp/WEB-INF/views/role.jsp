<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData">

        </div>


        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/><br/>
                <table id="roleTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th style="width: 15px"></th>
                        <th style="width: 1000px">Name</th>
                        <th style="width: 1000px">Description</th>
                        <th style="width: 50px">PRIVILEGE</th>
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
                <button type="button" class="btn bg-grey waves-war" id="refreshRole" value="1" title="Delete">
                    <img
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

                                <!-- Form Name -->
                                <legend><strong>Role Setting</strong></legend>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="name">Name :</label>

                                    <div class="col-md-6">
                                        <input type="hidden" class="form-control" id="id" name="id" required>
                                        <input type="hidden" class="form-control" id="version" name="version" required>
                                        <input id="name" name="name" type="text" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="nameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="description">Description :</label>

                                    <div class="col-md-6">
                                        <textarea id="description" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="address" rows="4" cols="16"></textarea>
                                        <label id="descriptionValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>


                                <!-- Multiple Checkboxes (inline) -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="viewRights">Rights</label>

                                    <div class="col-md-6">
                                        <label class="checkbox-inline" for="viewRights">
                                            <input class="rightVal" type="checkbox" name="rights" id="viewRights"
                                                   value="View">
                                            Read
                                        </label>
                                        <label class="checkbox-inline" for="writeRights">
                                            <input class="rightVal" type="checkbox" name="rights" id="writeRights"
                                                   value="Write">
                                            Write
                                        </label>
                                        <label class="checkbox-inline" for="editRights">
                                            <input class="rightVal" type="checkbox" name="rights" id="editRights"
                                                   value="Edit">
                                            Edit
                                        </label>
                                        <label class="checkbox-inline" for="deleteRights">
                                            <input class="rightVal" type="checkbox" name="rights" id="deleteRights"
                                                   value="Delete">
                                            Delete
                                        </label>
                                        <label id="rightsValidation" style="color:red; font-size: 11px;"
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
                                        <button id="resetRole" name="resetRole" class="btn bg-grey"
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
                var loading = $.loading();
                initFormValidationMsg();
                initializeRoleForm();
                $("saveRole").show();
                $("#updateRole").hide();
                var companyGb;

                /* Load role data to select box data using ajax */

                $('#roleTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/role/roleList",
                    "sAjaxDataProp": "",
                    "order": [[1, "asc"]],
                    'aoColumns': [
                        {
                            'sTitle': '',
                            "sClass": "checkbox-column",
                            'mData': 'id',
                            'mRender': function (id) {
                                return '<input class="getVal" style="position: static;"  type="checkbox" name="' + id + '" id="' + id + '">';
                            },
                            'sWidth': '15px',
                            'bSortable': false
                        },
                        {"mData": "name", 'sWidth': '100px'},
                        {"mData": "description", 'sWidth': '100px'},
                        {"mData": "rights"},
                    ],
                    'aaSorting': [[0, 'asc']],
                    "columnDefs": [
                        {
//                            "targets": [0],
//                            "visible": false,
//                            "searchable": false
                        }
                    ],
                    "cache": false,
                    "bPaginate": true,
                    "bLengthChange": true,
                    "bFilter": true,
                    "bInfo": false,
                    "bAutoWidth": true,
                    "scrollY": "400",
                    "scrollX": true

                });


                /* Save role data using ajax */

                $("#saveRole").click(function () {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var role = new Object();
                    createObjTOUpdate(role);
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, role);
                });


                /* Update role data using ajax */
//
                $('#editRole').click(function () {
                    initializeRoleForm();
                    initFormValidationMsg();
                    var role = new Object();
                    role = companyGb;
                    companyGb = null;
                    var data = messageResource.get('role.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (role == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        document.getElementById('roleForm').style.display = "block";
                        setValueToRoleForm(role);
                        setRightToEditForm(role);
                        window.location.href = "#roleForm";
                    }
                });
                var table = $('#roleTable').DataTable();

                $("#updateRole").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var role = new Object();
                    createObjTOUpdate(role);
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, role);
                });


                /* Delete role data using ajax */

                $("#deleteRole").click(function () {
                    initializeRoleForm();
                    initFormValidationMsg();
                    document.getElementById('roleForm').style.display = "none";
                    var role = new Object();
                    role = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('role.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (role == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('role.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, role);
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
                    table.ajax.url(messageResource.get('role.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('roleForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, role) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('role.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(role),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('role.list.load.url', 'configMessageForUI')).load();
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
                            icn = 0;
                            msg = '<strong style="color: red">Error</strong>';
                            showServerSideMessage(part1, getErrorMessage(error), icn, msg);
                        }
                    });
                }


                /*  Ajax call for edit operation */

                function callAjaxForEditOperation(part1, part2, icn, msg, role) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('role.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(role),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeRoleForm();
                                window.location.href = "#viewTableData";
                                $("#updateRole").hide();
                                $("#saveRole").show();
                                showServerSideMessage(part1, part2, icn, msg);
                                document.getElementById('roleForm').style.display = "none";
                                table.ajax.url(messageResource.get('role.list.load.url', 'configMessageForUI')).load();
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
                        'url': messageResource.get('role.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(role),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeRoleForm();
                                console.log(d.role);
                                setNewDataTableValue(d.role, table);
                                document.getElementById('roleForm').style.display = "none";
                                window.location.href = "#viewTableData";
                                showServerSideMessage(part1, part2, icn, msg);
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
                    $("#id").val("");
                    $("#version").val("");
                    $("#name").val("");
                    $("#description").val("");
                    $('input:checkbox.rightVal').each(function () {
                        var sThisVal = (this.checked ? $(this).val() : "");
                        if (sThisVal) $(this).prop('checked', false);
                    });
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var description = $("#description").val();
                    var categoryId = $("#listOfCategory option:selected").val();
                    if (name == null || name.trim().length == 0) {
                        $("#nameValidation").text("Name is required");
                        isValid = false;
                    }
                    if (description == null || description.trim().length == 0) {
                        $("#descriptionValidation").text("Description is required");
                        isValid = false;
                    }
                    if (checkForSingleRightSelect()) {
                        $("#rightsValidation").text("Please select rights");
                        isValid = false;
                    }
                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#nameValidation").text("");
                    $("#descriptionValidation").text("");
                    $("#rightsValidation").text("");
                }


                /* move to add new role div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('roleForm').style.display = "block";
                    companyGb = null;
                    $("#updateRole").hide();
                    $("#saveRole").show();
                    uncheckedAllCheckBox();
                    initializeRoleForm();
                    initFormValidationMsg();
                    window.location.href = "#roleForm";
                });


                /* Set new created role value to DataTable*/

                function setNewDataTableValue(role, table) {
                    table.row.add({
                        "id": role.id,
                        "name": role.name,
                        "version": role.version,
                        "description": role.description,
                        "rights": role.rights,
                        "createdBy": role.createdBy,
                        "createdOn": role.createdOn,
                        "updatedBy": role.updatedBy,
                        "updatedOn": role.updatedOn
                    }).draw();

                };


                /* check for right selection at UI  */

                function checkForSingleRightSelect() {
                    var counter = 0;
                    var isNotSelect = true;
                    $('input:checkbox.rightVal').each(function () {
                        var rThisVal = (this.checked ? $(this).val() : "");
                        if (rThisVal) counter++;
                        if (counter > 1) return false;
                    });
                    if (counter > 1) {
                        isNotSelect = false;
                    }
                    return isNotSelect;
                }


                function setRightToEditForm(role) {
                    for (var i = 0; i <= role.rights.length; i++) {
                        if (role.rights[i] === 'Edit') $("#editRights").prop('checked', true);
                        if (role.rights[i] === 'Delete') $("#deleteRights").prop('checked', true);
                        if (role.rights[i] === 'View') $("#viewRights").prop('checked', true);
                        if (role.rights[i] === 'Write') $("#writeRights").prop('checked', true);
                    }
                }

                function createObjTOUpdate(role) {
                    role.id = $("#id").val();
                    role.version = $("#version").val();
                    role.name = $("#name").val();
                    role.description = $("#description").val();
                    role.VIEW_PRIVILEGE = ($("#viewRights").is(":checked")) ? "View" : "";
                    role.WRITE_PRIVILEGE = ($("#writeRights").is(":checked")) ? "Write" : "";
                    role.EDIT_PRIVILEGE = ($("#editRights").is(":checked")) ? "Edit" : "";
                    role.DELETE_PRIVILEGE = ($("#deleteRights").is(":checked")) ? "Delete" : "";
                }

                function setValueToRoleForm(role) {
                    $("#updateRole").show();
                    $("#saveRole").hide();
                    $("#id").val(role.id);
                    $("#name").val(role.name);
                    $("#version").val(role.version);
                    $("#description").val(role.description);
                }
            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>