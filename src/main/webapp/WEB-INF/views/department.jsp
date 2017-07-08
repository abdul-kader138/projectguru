<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>Department List</h4></div>
                <hr/>
                <br/><br/>
                <table id="departmentTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="15px">id</th>
                        <th width="200px">Department Name</th>
                        <th width="200px">Company Name</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editDepartment" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteDepartment" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshDepartment" value="1" title="Refresh">
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


        <div class="row clearfix" id="departmentForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Department Setting</strong></legend>


                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfCompany">Company Name</label>

                                    <div class="col-md-4">
                                        <select id="listOfCompany" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="companyNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

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


                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveDepartment"></label>

                                    <div class="col-md-4">
                                        <button id="saveDepartment" name="saveDepartment" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateDepartment" name="updateDepartment" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetDepartment" name="resetDepartment"
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
                initializeDepartmentForm();
                $("saveDepartment").show();
                $("#updateDepartment").hide();
                var companyGb;
                getAllCompany();


                /* populate Department list when page load */

                $('#departmentTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/department/departmentList",
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
                                var name = row.name.substr(0, 20);
                                return name;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "company.name", 'sWidth': '200px', "render": function (data, type, row, id) {
                            if (row.company.name != null) {
                                var companyName = row.company.name.substr(0, 25);
                                return companyName;
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


                /* Save Department data using ajax */

                $("#saveDepartment").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var department = new Object();
                    department.id = $("#id").val();
                    department.version = $("#version").val();
                    department.name = $("#name").val();
                    department.companyId = $("#listOfCompany option:selected").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, department);
                });


                /* Update Department data using ajax */

                $('#editDepartment').click(function () {
                    document.getElementById('departmentForm').style.display = "none";
                    initializeDepartmentForm();
                    initFormValidationMsg();
                    var newDepartment = new Object();
                    newDepartment = companyGb;
                    companyGb = null;
                    var data = messageResource.get('department.edit.validation.msg', 'configMessageForUI');
                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newDepartment == null)showServerSideMessage(data, "", 0, "Message");
                    else setDataToDepartmentForm(newDepartment);
                });

                var table = $('#departmentTable').DataTable();

                $("#updateDepartment").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var department = new Object();
                    department.id = $("#id").val();
                    department.version = $("#version").val();
                    department.name = $("#name").val();
                    department.companyId = $("#listOfCompany option:selected").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, department);

                });


                /* Delete Department data using ajax */

                $("#deleteDepartment").click(function (event) {
                    document.getElementById('departmentForm').style.display = "none";
                    initializeDepartmentForm();
                    initFormValidationMsg();
                    var newDepartment = new Object();
                    newDepartment = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('department.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newDepartment == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('department.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newDepartment);

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

                $('#departmentTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetDepartment').on('click', function () {
                    companyGb = null;
                    initializeDepartmentForm();
                    initFormValidationMsg();
                    $('#saveDepartment').show();
                    $('#updateDepartment').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('departmentForm').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshDepartment').on('click', function () {
                    initializeDepartmentForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('department.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('departmentForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newDepartment) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('department.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newDepartment.id),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
//                                deleteDataRow(newDepartment.id,'departmentTable');
                                table.ajax.url(messageResource.get('department.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, department) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('department.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(department),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeDepartmentForm();
                                window.location.href = "#viewTableData";
                                $("#updateDepartment").hide();
                                $("#saveDepartment").show();
                                document.getElementById('departmentForm').style.display = "none";
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('department.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForAddOperation(part1, part2, icn, msg, department) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('department.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(department),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeDepartmentForm();
                                setNewDataTableValue(d.department, table);

                                window.location.href = "#viewTableData";
                                showServerSideMessage(part1, part2, icn, msg);
                                document.getElementById('departmentForm').style.display = "none";
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

                function initializeDepartmentForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $("#name").val("");
                    $('#defaultOpt').val('0').prop('selected', true);
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var companyId = $("#listOfCompany option:selected").val();
                    if (name == null || name.trim().length == 0) {
                        $("#nameValidation").text("Name is required");
                        isValid = false;
                    }
                    if ((companyId == null) || (companyId == "0")) {
                        $("#companyNameValidation").text("Company name is required");
                        isValid = false;
                    }

                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#nameValidation").text("");
                    $("#companyNameValidation").text("");

                }


                /* move to add new department div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('departmentForm').style.display = "block";
                    companyGb = null;
                    $("#updateDepartment").hide();
                    $("#saveDepartment").show();
                    initializeDepartmentForm();
                    initFormValidationMsg();
                    uncheckedAllCheckBox();
                    window.location.href = "#departmentForm";
                });


                /* Set new created department value to DataTable*/

                function setNewDataTableValue(department, table) {
                    table.row.add({
                        "id": department.id,
                        "name": department.name,
                        "version": department.version,
                        "company": department.company,
                        "createdBy": department.createdBy,
                        "createdOn": department.createdOn,
                        "updatedBy": department.updatedBy,
                        "updatedOn": department.updatedOn
                    }).draw();

                };


                /* set selected row data to department form for edit */

                function setDataToDepartmentForm(newDepartment) {
                    document.getElementById('departmentForm').style.display = "block";
                    $("#updateDepartment").show();
                    $("#saveDepartment").hide();
                    $("#id").val(newDepartment.id);
                    $("#name").val(newDepartment.name);
                    $("#version").val(newDepartment.version);
                    $('#listOfCompany option:contains("' + newDepartment.company.name + '")').prop('selected', 'selected');
                    window.location.href = "#departmentForm";
                }

                /* Load Company data to select box data using ajax */

                function getAllCompany() {
                    $('#listOfCompany').empty();
                    $.ajax({
                        type: "GET",
                        url: 'http://localhost:8080/company/companyList',
                        success: function (data) {
                            var collaboration;
                            collaboration += '<option id="defaultOpt" value="0">Select Company</option>';
                            $.each(data, function (i, d) {
                                collaboration += "<option value=" + d.id + ">" + d.name + "</option>";
                            });

                            $('#listOfCompany').append(collaboration);
                        },
                        error: function (e) {
                        }
                    });
                }

            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>