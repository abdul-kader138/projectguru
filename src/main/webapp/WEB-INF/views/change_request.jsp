<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">
        <div class="row clearfix" id="requestForm">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal" id="requestDetails">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Create Request</strong></legend>

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


                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="doc">Upload Document</label>

                                    <div class="col-md-6">

                                        <input class="form-control input-md"
                                               type="file" name="doc" id="doc"/>
                                        <button id="docClear" type="button">Clear</button>
                                        <label id="docNameValidation" style="color:red; font-size: 11px;"
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
                                    <%--<button class="add_field_button">Add More Description</button>--%>
                                    <label class="col-md-4 control-label" for="description">Description :</label>


                                    <div class="col-md-6 input_fields_wrap">
                                        <textarea id="description" name="description[]" class="form-control input-md description"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  rows="6" cols="20"></textarea>
                                        <label id="descriptionValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>

                                    <button type="button" class="btn bg-grey waves-war add_field_button col-md-2"
                                            id="moveToAdd" value="1" title="Add"><img
                                            src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add
                                        More
                                    </button>
                                </div>


                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveRequest"></label>

                                    <div class="col-md-4">
                                        <button id="saveRequest" name="saveRequest" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button style="position: static" id="resetRequest" name="resetRequest"
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

                /* set nav bar color */
                changeNavColor();
                var colorName = localStorage.colorName;
                setNavColor(colorName);

                /* Enable page loader */
                var loading = $.loading();

                /*Initialize Page Value*/
                initFormValidationMsg();
                resetChangeRequestForm();
                getAllCompany();


                /*Based on company Selection load Product data */

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
                    getSelectedCategory(product.id, "Select Product")
                });


                /* Initialize html form value  based on reset button*/

                $('#resetRequest').on('click', function () {
                    initFormValidationMsg();
                    resetChangeRequestForm();
                });


                /* Reset Upload file selection */

                $("#docClear").click(function (event) {
                    $("#doc").val("");
                });


                /* Save request data using ajax */

                $("#saveRequest").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var request = setRequestData();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, request);
                });


                /* html form Validation */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var description = $("#description").val();
                    var filename = $("#doc").val();
                    var companyId = $("#listOfCompany option:selected").val();
                    var categoryId = $("#listOfCategory option:selected").val();
                    var productId = $("#listOfProduct option:selected").val();
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

                    if ((categoryId == null) || (categoryId == "0")) {
                        $("#categoryNameValidation").text("Category name is required");
                        isValid = false;
                    }
                    if ((productId == null) || (productId == "0")) {
                        $("#productNameValidation").text("Product name is required");
                        isValid = false;
                    }

                    if ((filename == null) || (filename == "0")) {
                        $("#docNameValidation").text('Please browse,for uploading attachment...');
                        isValid = false;
                    }
                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#nameValidation").text("");
                    $("#descriptionValidation").text("");
                    $("#companyNameValidation").text("");
                    $("#categoryNameValidation").text("");
                    $("#productNameValidation").text("");
                    $("#docNameValidation").text("");

                }


                /*  Ajax call for save operation */

                function callAjaxForAddOperation(part1, part2, icn, msg, request) {
                    $.ajax({
                        url: messageResource.get('request.save.url', 'configMessageForUI'),
                        type: "POST",
                        data: request,
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false
                    }).
                            done(function (d) {
                                if (d.successMsg) {
                                    icn = 1;
                                    msg = "";
                                    part1 = d.successMsg;
                                    showServerSideMessage(part1, part2, icn, msg);
                                    initFormValidationMsg();
                                    resetChangeRequestForm();
                                }
                                if (d.validationError) {
                                    icn = 0;
                                    msg = '<strong style="color: red">Error</strong>';
                                    part2 = d.validationError;
                                    showServerSideMessage(part1, part2, icn, msg);
                                }
                            }).
                            fail(function (jqXHR, textStatus) {
                                icn = 0;
                                msg = '<strong style="color: red">Error</strong>';
                                showServerSideMessage(part1, getErrorMessage(textStatus), icn, msg);
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


                /*  prepare request object to save */

                function setRequestData() {
                    var categoryId = $("#listOfCategory option:selected").val();
                    var companyId = $("#listOfCompany option:selected").val();
                    var productId = $("#listOfProduct option:selected").val();
                    var request = new FormData(document.getElementById("requestDetails"));
                    var descriptionListObj={};
                    var descriptionList=$('.description');
                    for(i=0; i<descriptionList.length;i++){
                        descriptionListObj['Key'+i] =descriptionList[i].value;
                    }
                    request.append('categoryId', categoryId);
                    request.append('companyId', companyId);
                    request.append('productId', productId);
                    return request;
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


                /* Reset Request Form  */

                function resetChangeRequestForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $("#name").val("");
                    $("#doc").val("");
                    $("#description").val("");
                    $('#defaultOpt').val('0').prop('selected', true);
                    $('#defaultOptProduct').val('0').prop('selected', true);
                    $('#defaultOptCategory').val('0').prop('selected', true);
                    var descriptionList=$('.description');
                    for(i=0; i<descriptionList.length;i++){
                        descriptionList[i].value="";
                    }
                }


                var max_fields = 10; //maximum input boxes allowed
                var wrapper = $(".input_fields_wrap"); //Fields wrapper
                var add_button = $(".add_field_button"); //Add button ID

                var x = 1; //initlal text box count
                $(add_button).click(function (e) { //on add input button click
                    e.preventDefault();
                    if (x < max_fields) { //max input box allowed
                        x++; //text box increment
                        $(wrapper).append('<div><textarea id="description" name="description[]" class="form-control input-md description"style="border-color:#808080; border-width:1px; border-style:solid;"rows="6" cols="20"></textarea><a href="#" class="remove_field" style="font-size:12px;color: red">Remove</a></div>'); //add input box
                    }
                });

                $(wrapper).on("click", ".remove_field", function (e) { //user click on remove text
                    e.preventDefault();
                    $(this).parent('div').remove();
                    x--;
                });

                $('#testButton').click(function(){
                    var descriptionListObj={};
                    var descriptionList=$('.description');
                    for(i=0; i<descriptionList.length;i++){
                        descriptionListObj['Key'+i] =descriptionList[i].value;
                    }
                })
            });

            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>