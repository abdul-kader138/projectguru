<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/><br/>
                <table id="companyTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>id</th>
                        <th>Name</th>
                        <th>Address</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editCompany" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteCompany" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshCompany" value="1" title="Delete"><img
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


        <div class="row clearfix" id="companyForm">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Company Setting</strong></legend>

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

                                <!-- Textarea -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="address">Address :</label>

                                    <div class="col-md-6">
                                        <textarea id="address" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="address" rows="6" cols="20"></textarea>
                                        <label id="addressValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveCompany"></label>

                                    <div class="col-md-4">
                                        <button id="saveCompany" name="saveCompany" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateCompany" name="saveCompany" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetCompany" name="resetCompany"
                                                class="btn bg-grey"
                                                type="button">Reset
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
                initializeCompanyForm();
                $("saveCompany").show();
                $("#updateCompany").hide();
                var companyGb;


                /* populate Company list when page load */

                $('#companyTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/companyList",
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
                            'bSortable': false
                        },
                        {"mData": "name",'sWidth': '200px'},
                        {"mData": "address"}
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


                /* Save company data using ajax */

                $("#saveCompany").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var company = new Object();
                    company.name = $("#name").val();
                    company.address = $("#address").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, company);
                });


                /* Update company data using ajax */

                $('#editCompany').click(function () {
                    initializeCompanyForm();
                    initFormValidationMsg();
                    var newCompany = new Object();
                    var newCompany = companyGb;
                    companyGb = null;
                    var data = 'please select one record to perform edit operation.';

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newCompany == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $("#updateCompany").show();
                        $("#saveCompany").hide();
                        $("#id").val(newCompany.id);
                        $("#name").val(newCompany.name);
                        $("#version").val(newCompany.version);
                        $("#address").val(newCompany.address);
                        window.location.href = "#companyForm";
                    }
                });

                var table = $('#companyTable').DataTable();

                $("#updateCompany").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var company = new Object();
                    company.id = $("#id").val();
                    company.version = $("#version").val();
                    company.name = $("#name").val();
                    company.address = $("#address").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, company);

                });


                /* Delete company data using ajax */

                $("#deleteCompany").click(function (event) {
                    var newCompany = new Object();
                    newCompany = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = 'please select one record to perform delete operation.';

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newCompany == null)showServerSideMessage(data, "", 0, "Message");
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
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newCompany);

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

                $('#companyTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetCompany').on('click', function () {
                    companyGb = null;
                    initializeCompanyForm();
                    initFormValidationMsg();
                    $('#saveCompany').show();
                    $('#updateCompany').hide();
                    uncheckedAllCheckBox();
                });



                /* load table data on click refresh button*/

                $('#refreshCompany').on('click', function () {
                    initializeCompanyForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url('http://localhost:8080/companyList').load();
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newCompany) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': "http://localhost:8080/company/delete",
                        'data': JSON.stringify(newCompany),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url('http://localhost:8080/companyList').load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, company, obj) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': "http://localhost:8080/company/update",
                        'data': JSON.stringify(company),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeCompanyForm();
                                window.location.href = "#viewTableData";
                                $("#updateCompany").hide();
                                $("#saveCompany").show();
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url('http://localhost:8080/companyList').load();
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

                function callAjaxForAddOperation(part1, part2, icn, msg, company) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': "http://localhost:8080/company/save",
                        'data': JSON.stringify(company),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeCompanyForm();
                                setNewDataTableValue(d.company, table);
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

                function initializeCompanyForm() {
                    $("#id").val("");
                    $("#version").val("");
                    $("#name").val("");
                    $("#address").val("");
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var address = $("#address").val();
                    if (name == null || name.trim().length == 0) {
                        $("#nameValidation").text("Name is required");
                        isValid = false;
                    }
                    if ((address == null) || (address.trim().length) == 0) {
                        $("#addressValidation").text("Address is required");
                        isValid = false;
                    }
                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#nameValidation").text("");
                    $("#addressValidation").text("");

                }


                /* move to add new company div*/

                $('#moveToAdd').on('click', function () {
                    companyGb = null;
                    $("#updateCompany").hide();
                    $("#saveCompany").show();
                    uncheckedAllCheckBox();
                    window.location.href = "#companyForm";
                });


                /* Set new created company value to DataTable*/

                function setNewDataTableValue(company, table) {
                    table.row.add({
                        "id": company.id,
                        "name": company.name,
                        "version": company.version,
                        "address": company.address,
                        "createdBy": company.createdBy,
                        "createdOn": company.createdOn,
                        "updatedBy": company.updatedBy,
                        "updatedOn": company.updatedOn
                    }).draw();

                };


            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>