<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>Team Member Allocation List</h4></div>
                <hr/>
                <br/><br/>
                <table id="allocationTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="15px">id</th>
                        <th width="100px">Company</th>
                        <th width="100px">Product</th>
                        <th width="100px">Category</th>
                        <th width="100px">Requested By</th>
                        <th width="100px">Checked By</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editAllocation" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteAllocation" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshAllocation" value="1" title="Refresh">
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


        <div class="row clearfix" id="allocationForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Team Member Allocation Setting</strong></legend>

                                <input type="hidden" class="form-control" id="id" name="id" value="0" required>
                                <input type="hidden" class="form-control" id="version" name="version" value="0"
                                       required>
                                <!-- select Box for Company-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfCompany">Company Name</label>

                                    <div class="col-md-4">
                                        <select id="listOfCompany" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="companyNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

                                <!-- select Box for Product-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfProduct">Product Name</label>

                                    <div class="col-md-4">
                                        <select id="listOfProduct" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="productNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>


                                <!-- select Box for Product-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfCategory">Category Name</label>

                                    <div class="col-md-4">
                                        <select id="listOfCategory" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="categoryNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

                                <!-- select Box for Requested By-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfRequestBy">Request by</label>

                                    <div class="col-md-4">
                                        <select id="listOfRequestBy" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="requestByNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>


                                <!-- select Box for Checked By-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfCheckedBy">Checked by</label>

                                    <div class="col-md-4">
                                        <select id="listOfCheckedBy" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="checkedByNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>



                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveAllocation"></label>

                                    <div class="col-md-4">
                                        <button id="saveAllocation" name="saveAllocation" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateAllocation" name="updateAllocation" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetAllocation" name="resetAllocation"
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
                initializeAllocationForm();
                $("saveAllocation").show();
                $("#updateAllocation").hide();
                var companyGb;
                getAllCompany();
                getAllTeamMember();


                /*Based on company Selection load department & product data */

                $("#listOfCompany").change(function () {
                    var id = $(this).val();
                    id = parseInt(id);
                    var company = new Object();
                    company.id = id;
                    getSelectedProduct(company.id, "Select Product");
                });

                /*Based on product Selection load category data */

                $("#listOfProduct").change(function () {
                    var id = $(this).val();
                    id = parseInt(id);
                    var product = new Object();
                    product.id = id;
                    getSelectedCategory(product.id, "Select Category");
                });


                /* populate Allocation list when page load */

                $('#allocationTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/team_allocation/team_allocationList",
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
                            "mData": "category.company.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.category.company.name != null) {
                                var companyName = row.category.company.name.substr(0, 20);
                                return companyName;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "category.product.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.category.product.name != null) {
                                var productName = row.category.product.name.substr(0, 20);
                                return productName;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "category.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.category.name != null) {
                                var categoryName = row.category.name.substr(0, 20);
                                return categoryName;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "requestedBy.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.requestedBy.name != null) {
                                var requestedByName = row.requestedBy.name.substr(0, 20);
                                return requestedByName;
                            }
                            return "";
                        }
                        },
                        {
                            "mData": "checkedBy.name", 'sWidth': '100px', "render": function (data, type, row, id) {
                            if (row.checkedBy.name != null) {
                                var checkedByName = row.checkedBy.name.substr(0, 20);
                                return checkedByName;
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


                /* Save allocation data using ajax */

                $("#saveAllocation").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var allocation = new Object();
                    allocation.id = $("#id").val();
                    allocation.version = $("#version").val();
                    allocation.companyId = $("#listOfCompany option:selected").val();
                    allocation.productId = $("#listOfProduct option:selected").val();
                    allocation.categoryId = $("#listOfCategory option:selected").val();
                    allocation.requestById = $("#listOfRequestBy option:selected").val();
                    allocation.checkedById = $("#listOfCheckedBy option:selected").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, allocation);
                });


                /* Update Allocation data using ajax */

                $('#editAllocation').click(function () {
                    document.getElementById('allocationForm').style.display = "none";
                    initializeAllocationForm();
                    initFormValidationMsg();
                    var newAllocation = new Object();
                    newAllocation = companyGb;
                    companyGb = null;
                    var data = messageResource.get('team_allocation.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newAllocation == null)showServerSideMessage(data, "", 0, "Message");
                    else setDataToAllocationForm(newAllocation);
                });

                var table = $('#allocationTable').DataTable();

                $("#updateAllocation").click(function () {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var newAllocation = new Object();
                    newAllocation.id = $("#id").val();
                    newAllocation.version = $("#version").val();
                    newAllocation.companyId = $("#listOfCompany option:selected").val();
                    newAllocation.productId = $("#listOfProduct option:selected").val();
                    newAllocation.categoryId = $("#listOfCategory option:selected").val();
                    newAllocation.requestById = $("#listOfRequestBy option:selected").val();
                    newAllocation.checkedById = $("#listOfCheckedBy option:selected").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, newAllocation);

                });


                /* Delete Allocation data using ajax */

                $("#deleteAllocation").click(function (event) {
                    document.getElementById('allocationForm').style.display = "none";
                    initializeAllocationForm();
                    initFormValidationMsg();
                    var newAllocation = new Object();
                    newAllocation = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('team_allocation.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newAllocation == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('team_allocation.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newAllocation);

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

                $('#allocationTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetAllocation').on('click', function () {
                    companyGb = null;
                    initializeAllocationForm();
                    initFormValidationMsg();
                    $('#saveAllocation').show();
                    $('#updateAllocation').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('allocationForm').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshAllocation').on('click', function () {
                    initializeAllocationForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('team_allocation.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('allocationForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newAllocation) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('team_allocation.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newAllocation.id),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('team_allocation.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, allocation) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('team_allocation.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(allocation),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeAllocationForm();
                                window.location.href = "#viewTableData";
                                $("#updateAllocation").hide();
                                $("#saveAllocation").show();
                                document.getElementById('allocationForm').style.display = "none";
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('team_allocation.list.load.url', 'configMessageForUI')).load();
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


                /*  Ajax call for save operation */

                function callAjaxForAddOperation(part1, part2, icn, msg, allocation) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('team_allocation.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(allocation),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeAllocationForm();
                                setNewDataTableValue(d.teamAllocation, table);
                                window.location.href = "#viewTableData";
                                showServerSideMessage(part1, part2, icn, msg);
                                document.getElementById('allocationForm').style.display = "none";
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

                function initializeAllocationForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $('#defaultOpt').val('0').prop('selected', true);
                    $('#defaultOptProduct').val('0').prop('selected', true);
                    $('#defaultOptCategory').val('0').prop('selected', true);
                    $('#defaultOptRequestBy').val('0').prop('selected', true);
                    $('#defaultOptCheckedBy').val('0').prop('selected', true);
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var companyId = $("#listOfCompany option:selected").val();
                    var productId = $("#listOfProduct option:selected").val();
                    var categoryId = $("#listOfCategory option:selected").val();
                    var requestById = $("#listOfRequestBy option:selected").val();
                    var checkedById = $("#listOfCheckedBy option:selected").val();


                    if ((companyId == null) || (companyId == "0")) {
                        $("#companyNameValidation").text("Company name is required");
                        isValid = false;
                    }

                    if ((productId == null) || (productId == "0")) {
                        $("#productNameValidation").text("Product name is required");
                        isValid = false;
                    }

                    if ((categoryId == null) || (categoryId == "0")) {
                        $("#categoryNameValidation").text("Category name is required");
                        isValid = false;
                    }

                    if ((requestById == null) || (requestById == "0")) {
                        $("#requestByNameValidation").text("Requester name is required");
                        isValid = false;
                    }

                    if ((checkedById == null) || (checkedById == "0")) {
                        $("#checkedByNameValidation").text("Checked By name is required");
                        isValid = false;
                    }

                    if (checkedById == requestById && ((requestById != null) || (requestById != "0"))) {
                        $("#checkedByNameValidation").text("Checked By and Requester name can't be same");
                        isValid = false;
                    }
                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#companyNameValidation").text("");
                    $("#productNameValidation").text("");
                    $("#categoryNameValidation").text("");
                    $("#requestByNameValidation").text("");
                    $("#checkedByNameValidation").text("");


                }


                /* move to add new Allocation div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('allocationForm').style.display = "block";
                    companyGb = null;
                    $("#updateAllocation").hide();
                    $("#saveAllocation").show();
                    initializeAllocationForm();
                    initFormValidationMsg();
                    uncheckedAllCheckBox();
                    window.location.href = "#allocationForm";
                });


                /* Set new created Allocation value to DataTable*/

                function setNewDataTableValue(allocation, table) {
                    table.row.add({
                        "id": allocation.id,
                        "version": allocation.version,
                        "category": allocation.category,
                        "requestedBy": allocation.requestedBy,
                        "checkedBy": allocation.checkedBy
                    }).draw();
                }


                /* Load Product data to select box data using ajax */

                function getSelectedProduct(companyId, obj) {
                    $('#listOfProduct').empty();
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        url: 'http://localhost:8080/product/productListByCompany',
                        'data': JSON.stringify(companyId),
                        'dataType': 'json',
                        success: function (data) {
                            var collaboration;
                            collaboration += '<option id="defaultOptProduct" value="0">Select Product</option>';
                            $.each(data, function (i, d) {
                                collaboration += "<option value=" + d.id + ">" + d.name + "</option>";
                            });

                            $('#listOfProduct').append(collaboration);
                            $('#listOfProduct option:contains("' + obj + '")').prop('selected', 'selected');
                        },
                        error: function (e) {
                        }
                    });
                }


                /* Load Category data to select box data using ajax */

                function getSelectedCategory(productId, obj) {
                    $('#listOfCategory').empty();
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        url: 'http://localhost:8080/category/categoryListByProduct',
                        'data': JSON.stringify(productId),
                        'dataType': 'json',
                        success: function (data) {
                            var collaboration;
                            collaboration += '<option id="defaultOptCategory" value="0">Select Category</option>';
                            $.each(data, function (i, d) {
                                collaboration += "<option value=" + d.id + ">" + d.name + "</option>";
                            });

                            $('#listOfCategory').append(collaboration);
                            $('#listOfCategory option:contains("' + obj + '")').prop('selected', 'selected');
                        },
                        error: function (e) {
                        }
                    });
                }


                /* set selected row data to Allocation form for edit */

                function setDataToAllocationForm(newAllocation) {
                    document.getElementById('allocationForm').style.display = "block";
                    $("#updateAllocation").show();
                    $("#saveAllocation").hide();
                    $("#id").val(newAllocation.id);
                    $("#version").val(newAllocation.version);
                    $('#listOfCompany option:contains("' + newAllocation.category.company.name + '")').prop('selected', 'selected');
                    $('#listOfRequestBy option:contains("' + newAllocation.requestedBy.name + '")').prop('selected', 'selected');
                    $('#listOfCheckedBy option:contains("' + newAllocation.checkedBy.name + '")').prop('selected', 'selected');
                    var company = new Object();
                    var id = newAllocation.companyId;
                    var productId = newAllocation.productId;
                    id = parseInt(id);
                    productId = parseInt(productId);
                    getSelectedProduct(id, newAllocation.category.product.name);
                    getSelectedCategory(productId, newAllocation.category.name);
                    window.location.href = "#allocationForm";
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

                //


                /* Load User data to select box data using ajax */

                function getAllTeamMember() {
                    $('#listOfRequestBy').empty();
                    $('#listOfcheckedBy').empty();
                    $.ajax({
                        type: "GET",
                        url: 'http://localhost:8080/team/teamList',
                        success: function (data) {
                            var collaborationRequest;
                            var collaborationChecked;
                            collaborationRequest += '<option id="defaultOptRequestBy" value="0">Select RequestBy</option>';
                            collaborationChecked += '<option id="defaultOptCheckedBy" value="0">Select CheckedBy</option>';
                            $.each(data, function (i, d) {
                                collaborationRequest += "<option value=" + d.id + ">" + d.name + "</option>";
                                collaborationChecked += "<option value=" + d.id + ">" + d.name + "</option>";
                            });

                            $('#listOfRequestBy').append(collaborationRequest);
                            $('#listOfCheckedBy').append(collaborationChecked);
                        },
                        error: function (e) {
                        }
                    });
                }

            });
        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>