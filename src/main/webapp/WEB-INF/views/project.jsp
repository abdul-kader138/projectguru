<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData" >

        </div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/><br/>
                <table id="projectTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th></th>
                        <th>Division Name</th>
                        <th>Company Name</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editProject" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteProject" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshProject" value="1" title="Delete"><img
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


        <div class="row clearfix" id="projectForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Division Setting</strong></legend>

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
                                    <label class="col-md-4 control-label" for="listOfCompany">Company Name</label>

                                    <div class="col-md-4">
                                        <select id="listOfCompany" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="companyNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveProject"></label>

                                    <div class="col-md-4">
                                        <button id="saveProject" name="saveProject" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateProject" name="updateProject" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button id="resetProject" name="resetProject" class="btn bg-grey"
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
                initializeProjectForm();
                $("saveProject").show();
                $("#updateProject").hide();
                var companyGb;



                /* populate Company list when page load */

                $('#projectTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/project/projectList",
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
                        {"mData": "name",'sWidth': '200px'},
                        {"mData": "company.name"}
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


                /* Load Company data to select box data using ajax */

                function getAllCompany() {
                    $('#getAllCompany').empty();
                    $.ajax({
                        type: "GET",
                        url: "http://localhost:8080/company/companyList",
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

                getAllCompany();

                /* Save project data using ajax */

                $("#saveProject").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var project = new Object();
                    project.name = $("#name").val();
                    project.companyId = $("#listOfCompany option:selected").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, project);
                });


                /* Update project data using ajax */

                $('#editProject').click(function () {
                    initializeProjectForm();
                    initFormValidationMsg();
                    var project = new Object();
                    project = companyGb;
                    companyGb = null;
                    var data = messageResource.get('project.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (project == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        document.getElementById('projectForm').style.display = "block";
                        $("#updateProject").show();
                        $("#saveProject").hide();
                        $("#id").val(project.id);
                        $("#name").val(project.name);
                        $("#version").val(project.version);
                        $('#listOfCompany option:contains("' + project.company.name + '")').prop('selected', 'selected');
                        window.location.href = "#projectForm";
                    }
                });
                var table = $('#projectTable').DataTable();

                $("#updateProject").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var project = new Object();
                    project.id = $("#id").val();
                    project.version = $("#version").val();
                    project.name = $("#name").val();
                    project.companyId = $("#listOfCompany option:selected").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, project);
                    });


                /* Delete project data using ajax */

                $("#deleteProject").click(function (event) {
                    initializeProjectForm();
                    initFormValidationMsg();
                    document.getElementById('projectForm').style.display = "none";
                    var project = new Object();
                    project = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data =  messageResource.get('project.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (project == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: 'Are You Sure,want to delete this record?',
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, project);
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

                $('#projectTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetProject').on('click', function () {
                    companyGb = null;
                    initializeProjectForm();
                    initFormValidationMsg();
                    $('#saveProject').show();
                    $('#updateProject').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('projectForm').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshProject').on('click', function () {
                    initializeProjectForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url( messageResource.get('project.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('projectForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, project) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('project.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(project),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('project.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, project) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('project.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(project),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeProjectForm();
                                window.location.href = "#viewTableData";
                                $("#updateProject").hide();
                                $("#saveProject").show();
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('project.list.load.url', 'configMessageForUI')).load();
                            }
                            if (d.validationError) {
                                icn = 0;
                                msg = "";
                                msg = '<strong style="color: red">Error</strong>';
                                part2 = d.validationError;
                                uncheckedAllCheckBox();
                                document.getElementById('projectForm').style.display = "none";
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

                function callAjaxForAddOperation(part1, part2, icn, msg, project) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('project.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(project),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeProjectForm();
                                setNewDataTableValue(d.project, table);
                                window.location.href = "#viewTableData";
                                document.getElementById('projectForm').style.display = "none";
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

                function initializeProjectForm() {
                    $("#id").val("");
                    $("#version").val("");
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


                /* move to add new project div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('projectForm').style.display = "block";
                    companyGb = null;
                    $("#updateProject").hide();
                    $("#saveProject").show();
                    uncheckedAllCheckBox();
                    initializeProjectForm();
                    initFormValidationMsg();
                    window.location.href = "#projectForm";
                });


                /* Set new created project value to DataTable*/

                function setNewDataTableValue(project, table) {
                    table.row.add({
                        "id": project.id,
                        "name": project.name,
                        "version": project.version,
                        "company": project.company,
                        "createdBy": project.createdBy,
                        "createdOn": project.createdOn,
                        "updatedBy": project.updatedBy,
                        "updatedOn": project.updatedOn
                    }).draw();

                };


            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>