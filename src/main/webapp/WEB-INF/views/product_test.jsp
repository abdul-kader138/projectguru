<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>Product List</h4></div>
                <hr/>
                <br/><br/>
                <table id="productTable" class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="15px">id</th>
                        <th width="200px">Product Name</th>
                        <th width="300px">Description</th>
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

                <button type="button" class="btn bg-grey waves-war" id="editProduct" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteProduct" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshProduct" value="1" title="Refresh"><img
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


        <div class="row clearfix" id="productForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Product Setting</strong></legend>

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
                                    <label class="col-md-4 control-label" for="saveProduct"></label>

                                    <div class="col-md-4">
                                        <button id="saveProduct" name="saveProduct" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateProduct" name="updateProduct" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetProduct" name="resetProduct"
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
                initializeProductForm();
                $("saveProduct").show();
                $("#updateProduct").hide();
                var companyGb;
                getAllCompany();

                /* populate Product list when page load */

                $('#productTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/product/productList",
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    'aoColumns': [
                        {
                            'sTitle': '',
                            "sClass": "checkbox-column",
                            'mData': 'id',
                            "orderable": false,
                            'mRender': function (id) {
                                return '<input class="getVal" style="position: static;"  type="checkbox" name="' + id + '" id="' + id + '">';
                            },
                            'sWidth': '15px'
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


                /* Save Product data using ajax */

                $("#saveProduct").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var product = new Object();
                    product.id = $("#id").val();
                    product.version = $("#version").val();
                    product.name = $("#name").val();
                    product.description = $("#description").val();
                    product.companyId = $("#listOfCompany option:selected").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, product);
                });


                /* Update Product data using ajax */

                $('#editProduct').click(function () {
                    initializeProductForm();
                    initFormValidationMsg();
                    var newProduct = new Object();
                    newProduct = companyGb;
                    companyGb = null;
                    var data = messageResource.get('product.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newProduct == null)showServerSideMessage(data, "", 0, "Message");
                    else setDataToProductForm(newProduct);
                });

                var table = $('#productTable').DataTable();

                $("#updateProduct").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var newProduct = new Object();
                    newProduct.id = $("#id").val();
                    newProduct.version = $("#version").val();
                    newProduct.name = $("#name").val();
                    newProduct.description = $("#description").val();
                    newProduct.companyId = $("#listOfCompany option:selected").val();
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, newProduct);

                });


                /* Delete Product data using ajax */

                $("#deleteProduct").click(function (event) {
                    document.getElementById('productForm').style.display = "none";
                    initializeProductForm();
                    initFormValidationMsg();
                    var newProduct = new Object();
                    newProduct = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('product.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newProduct == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('product.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newProduct);

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

                $('#productTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetProduct').on('click', function () {
                    companyGb = null;
                    initializeProductForm();
                    initFormValidationMsg();
                    $('#saveProduct').show();
                    $('#updateProduct').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('productForm').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshProduct').on('click', function () {
                    initializeProductForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('product.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('productForm').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newProduct) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('product.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newProduct.id),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('product.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, product) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('product.edit.url', 'configMessageForUI'),
                        'data': JSON.stringify(product),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                part1 = d.successMsg;
                                initializeProductForm();
                                window.location.href = "#viewTableData";
                                $("#updateProduct").hide();
                                $("#saveProduct").show();
                                document.getElementById('productForm').style.display = "none";
                                showServerSideMessage(part1, part2, icn, msg);
                                table.ajax.url(messageResource.get('product.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForAddOperation(part1, part2, icn, msg, product) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('product.save.url', 'configMessageForUI'),
                        'data': JSON.stringify(product),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                initializeProductForm();
                                setNewDataTableValue(d.product, table);
                                window.location.href = "#viewTableData";
                                showServerSideMessage(part1, part2, icn, msg);
                                document.getElementById('productForm').style.display = "none";
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

                function initializeProductForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $("#name").val("");
                    $("#description").val("");
                    $('#defaultOpt').val('0').prop('selected', true);
                }


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var description = $("#description").val();
                    var companyId = $("#listOfCompany option:selected").val();
                    var departmentId = $("#listOfDepartment option:selected").val();
                    if (name == null || name.trim().length == 0) {
                        $("#nameValidation").text("Name is required");
                        isValid = false;
                    }
                    if (description == null || description.trim().length == 0) {
                        $("#descriptionValidation").text("Description is required");
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
                    $("#descriptionValidation").text("");
                    $("#companyNameValidation").text("");

                }


                /* move to add new product div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('productForm').style.display = "block";
                    companyGb = null;
                    $("#updateProduct").hide();
                    $("#saveProduct").show();
                    initializeProductForm();
                    initFormValidationMsg();
                    uncheckedAllCheckBox();
                    window.location.href = "#productForm";
                });


                /* Set new created product value to DataTable*/

                function setNewDataTableValue(product, table) {
                    table.row.add({
                        "id": product.id,
                        "name": product.name,
                        "description": product.description,
                        "version": product.version,
                        "company": product.company
                    }).draw();

                };


                /* set selected row data to product form for edit */

                function setDataToProductForm(newProduct) {
                    document.getElementById('productForm').style.display = "block";
                    $("#updateProduct").show();
                    $("#saveProduct").hide();
                    $("#id").val(newProduct.id);
                    $("#name").val(newProduct.name);
                    $("#description").val(newProduct.description);
                    $("#version").val(newProduct.version);
                    $('#listOfCompany option:contains("' + newProduct.company.name + '")').prop('selected', 'selected');
                    window.location.href = "#productForm";
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