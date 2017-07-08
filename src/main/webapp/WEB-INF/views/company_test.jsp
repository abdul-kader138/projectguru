<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>Company List</h4></div>
                <hr/>
                <br/><br/>
                <table id="companyTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="25px">id</th>
                        <th width="250px">Name</th>
                        <th width="450px">Address</th>
                        <th width="150px">Logo</th>
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
                <button type="button" class="btn bg-grey waves-war" id="refreshCompany" value="1" title="Refresh"><img
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


        <div class="row clearfix" id="companyForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal" id="formData">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Company Setting</strong></legend>

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
                                    <label class="col-md-4 control-label" for="address">Address :</label>

                                    <div class="col-md-6">
                                        <textarea id="address" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="address" rows="6" cols="20"></textarea>
                                        <label id="addressValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

                                <%--logo show--%>
                                <div class="form-group" id="uploadedLogo" style="display: none">
                                    <label class="col-md-4 control-label" for="logo">Current Logo</label>

                                    <div class="col-md-6">
                                        <img id="uploadedLogoSrc"
                                             src="" width="70" height="70" border="0">
                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="logo">Logo</label>

                                    <div class="col-md-6">

                                        <input class="form-control input-md"
                                               type="file" name="logo" id="logo"/>
                                        <button id="logoClear" type="button">Clear</button>
                                        <label id="logoValidation" style="color:red; font-size: 11px;"
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
                                        <button id="updateCompany" name="updateCompany" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetCompany" name="resetCompany"
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
                initializeCompanyForm();
                $("saveCompany").show();
                $("#updateCompany").hide();
                var companyGb;
                var mainPath = document.origin + "/PG";


                /* populate Company list when page load */

                $('#companyTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/company/companyList",
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
                                var name = row.name.substr(0,30);
                                return name;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "address", 'sWidth': '400px', "orderable": false,"render": function (data, type, row, id) {
                            if (row.address != null) {
                                var address = row.address.substr(0,50);
                                return address;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "path","orderable": false,
                            "render": function (url, type, full) {
                                return '<img src="' + mainPath + full.imagePath + '" width="30" height="30" />';
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
                    company.logo = $("#logo").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, company);
                });


                /* Update company data using ajax */

                $('#editCompany').click(function () {
                    document.getElementById('companyForm').style.display = "none";
                    initializeCompanyForm();
                    initFormValidationMsg();
                    var newCompany = new Object();
                    newCompany = companyGb;
                    companyGb = null;
                    var data = messageResource.get('company.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newCompany == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        setSelectedRowValueToForm(newCompany);
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
                    if (formValidationForUpdate()) callAjaxForEditOperation(part1, part2, icn, msg, company);

                });


                /* Delete company data using ajax */

                $("#deleteCompany").click(function (event) {
                    initializeCompanyForm();
                    initFormValidationMsg();
                    document.getElementById('companyForm').style.display = "none";
                    var newCompany = new Object();
                    newCompany = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('company.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newCompany == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('company.delete.confirm.msg', 'configMessageForUI'),
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
                    window.location.href = "#viewTableData";
                    document.getElementById('companyForm').style.display = "none";
                    document.getElementById('uploadedLogo').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshCompany').on('click', function () {
                    initializeCompanyForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('company.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('companyForm').style.display = "none";
                    document.getElementById('uploadedLogo').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newCompany) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('company.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newCompany.id),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('company.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, company) {
                    $.ajax({
                        url: messageResource.get('company.edit.url', 'configMessageForUI'),
                        type: "POST",
                        data: new FormData(document.getElementById("formData")),
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false
                    }).done(function (data) {
                        if (data.successMsg) {
                            icn = 1;
                            part1 = data.successMsg;
                            initializeCompanyForm();
                            window.location.href = "#viewTableData";
                            $("#updateCompany").hide();
                            $("#saveCompany").show();
                            document.getElementById('companyForm').style.display = "none";
                            document.getElementById('uploadedLogo').style.display = "none";
                            showServerSideMessage(part1, part2, icn, msg);
                            table.ajax.url(messageResource.get('company.list.load.url', 'configMessageForUI')).load();
                        }
                        if (data.validationError) {
                            uncheckedAllCheckBox();
                            icn = 0;
                            msg = '<strong style="color: red">Error</strong>';
                            part2 = data.validationError;
                            showServerSideMessage(part1, part2, icn, msg);
                        }
                    }).fail(function (jqXHR, textStatus) {
                        uncheckedAllCheckBox();
                        icn = 0;
                        msg = '<strong style="color: red">Error</strong>';
                        showServerSideMessage(part1, getErrorMessage(textStatus), icn, msg);
                    });

                }


                /*  Ajax call for save operation */

                function callAjaxForAddOperation(part1, part2, icn, msg, company) {
                    $.ajax({
                        url: messageResource.get('company.save.url', 'configMessageForUI'),
                        type: "POST",
                        data: new FormData(document.getElementById("formData")),
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false
                    }).done(function (data) {
                        if (data.successMsg) {
                            icn = 1;
                            msg = "";
                            part1 = data.successMsg;
                            initializeCompanyForm();
                            window.location.href = "#viewTableData";
                            document.getElementById('companyForm').style.display = "none";
                            showServerSideMessage(part1, part2, icn, msg);
                            table.ajax.url(messageResource.get('company.list.load.url', 'configMessageForUI')).load();
                        }
                        if (data.validationError) {
                            icn = 0;
                            msg = '<strong style="color: red">Error</strong>';
                            part2 = data.validationError;
                            showServerSideMessage(part1, part2, icn, msg);
                        }
                    }).fail(function (jqXHR, textStatus) {
                        icn = 0;
                        msg = '<strong style="color: red">Error</strong>';
                        showServerSideMessage(part1, getErrorMessage(textStatus), icn, msg);
                    });
                }


                /* Initialize html form value */

                function initializeCompanyForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $("#name").val("");
                    $("#address").val("");
                    $("#logo").val("");
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var address = $("#address").val();
                    var filename = $("#logo").val();
                    if (name == null || name.trim().length == 0) {
                        $("#nameValidation").text("Name is required");
                        isValid = false;
                    }
                    if ((address == null) || (address.trim().length) == 0) {
                        $("#addressValidation").text("Address is required");
                        isValid = false;
                    }

                    if (!(isJpg(filename) || isPng(filename) || isGif(filename))) {
                        $("#logoValidation").text('Please browse a JPG/PNG/GIF file to upload ...');
                        isValid = false;
                    }
                    return isValid;
                }

                /* html form Validation when update */

                function formValidationForUpdate() {
                    var isValid = true;
                    var name = $("#name").val();
                    var address = $("#address").val();
                    var filename = $("#logo").val();
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
                    $("#logoValidation").text("");

                }


                /* clear logo field*/

                $('#logoClear').on('click', function () {
                    $("#logo").val("");
                });


                /* move to company Form*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('companyForm').style.display = "block";
                    companyGb = null;
                    $("#updateCompany").hide();
                    $("#saveCompany").show();
                    uncheckedAllCheckBox();
                    initializeCompanyForm();
                    initFormValidationMsg();
                    window.location.href = "#companyForm";
                });


                /* selected row value set to company form when edit*/

                function setSelectedRowValueToForm(newCompany) {
                    document.getElementById('companyForm').style.display = "block";
                    document.getElementById('uploadedLogo').style.display = "block";
                    $("#updateCompany").show();
                    $("#saveCompany").hide();
                    $("#id").val(newCompany.id);
                    $("#name").val(newCompany.name);
                    $("#version").val(newCompany.version);
                    $("#address").val(newCompany.address);
                    $("#uploadedLogoSrc").attr("src", mainPath + newCompany.imagePath);
                    window.location.href = "#companyForm";
                }

            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>