<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/><br/>
                <table id="categoryTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th style="width: 15px">id</th>
                        <th style="width: 200px">Name</th>
                        <th>Description</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editCategory" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteCategory" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshCategory" value="1" title="Delete"><img
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


        <div class="row clearfix" id="categoryForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Category Setting</strong></legend>

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
                                    <label class="col-md-4 control-label" for="saveCategory"></label>

                                    <div class="col-md-4">
                                        <button id="saveCategory" name="saveCategory" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateCategory" name="updateCategory" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetCategory" name="resetCategory"
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

                var loading = $.loading();
                initFormValidationMsg();
                initializeCategoryForm();
                $("saveCategory").show();
                $("#updateCategory").hide();
                var companyGb;


                /* populate category list when page load */

                $('#categoryTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/category/categoryList",
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
                            'bSortable': false
                        },
                        {"mData": "name", 'sWidth': '200px'},
                        {"mData": "description"}
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


                /* Save Category data using ajax */

                $("#saveCategory").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var category = new Object();
                    category.name = $("#name").val();
                    category.description = $("#description").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, category);
                });


                /* Update Category data using ajax */

                $('#editCategory').click(function () {
                    initializeCategoryForm();
                    initFormValidationMsg();
                    var newCategory = new Object();
                    var newCategory = companyGb;
                    companyGb = null;
                    var data = messageResource.get('category.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newCategory == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        document.getElementById('categoryForm').style.display = "block";
                        $("#updateCategory").show();
                        $("#saveCategory").hide();
                        $("#id").val(newCategory.id);
                        $("#name").val(newCategory.name);
                        $("#version").val(newCategory.version);
                        $("#description").val(newCategory.description);
                        window.location.href = "#categoryForm";
                    }
                });

                var table = $('#categoryTable').DataTable();

                $("#updateCategory").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var category = new Object();
                    category.id = $("#id").val();
                    category.version = $("#version").val();
                    category.name = $("#name").val();
                    category.description = $("#description").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, category);

                });


                /* Delete category data using ajax */

                $("#deleteCategory").click(function (event) {
                    document.getElementById('categoryForm').style.display = "none";
                    initializeCategoryForm();
                    initFormValidationMsg();
                    var newCategory = new Object();
                    newCategory = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('category.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newCategory == null) showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('category.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newCategory);

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

                $('#categoryTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetCategory').on('click', function () {
                    companyGb = null;
                    initializeCategoryForm();
                    initFormValidationMsg();
                    $('#saveCategory').show();
                    $('#updateCategory').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('categoryForm').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshCategory').on('click', function () {
                    initializeCategoryForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('category.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('categoryForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newCategory) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('category.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newCategory),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('category.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, category, obj) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('category.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(category),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeCategoryForm();
                                window.location.href = "#viewTableData";
                                $("#updateCategory").hide();
                                $("#saveCategory").show();
                                document.getElementById('categoryForm').style.display = "none";
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('category.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForAddOperation(part1, part2, icn, msg, category) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('category.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(category),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeCategoryForm();
                                document.getElementById('categoryForm').style.display = "none";
                                setNewDataTableValue(d.productCategory, table);
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

                function initializeCategoryForm() {
                    $("#id").val("");
                    $("#version").val("");
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
                    if ((description == null) || (description.trim().length) == 0) {
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


                /* move to add new category div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('categoryForm').style.display = "block";
                    companyGb = null;
                    $("#updateCategory").hide();
                    $("#saveCategory").show();
                    uncheckedAllCheckBox();
                    initializeCategoryForm();
                    initFormValidationMsg();
                    window.location.href = "#categoryForm";
                });


                /* Set new created category value to DataTable*/

                function setNewDataTableValue(category, table) {
                    table.row.add({
                        "id": category.id,
                        "name": category.name,
                        "description": category.description,
                        "version": category.version,
                        "createdBy": category.createdBy,
                        "createdOn": category.createdOn,
                        "updatedBy": category.updatedBy,
                        "updatedOn": category.updatedOn
                    }).draw();

                };


            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>