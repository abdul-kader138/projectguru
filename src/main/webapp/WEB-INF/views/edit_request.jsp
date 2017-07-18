<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">
        <div class="row clearfix" id="requestForm">
            <div class="col-xs-12">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal" id="requestDetails">
                            <fieldset>

                                <!-- Form Name -->
                                <span class="glyphicon glyphicon-pencil"></span> <b style="font-size: 20px">
                                Edit Request</b>
                                <hr/>

                                <!-- select Box for Company-->
                                <div class="form-group">
                                    <label class="col-md-4 label_color control-label" for="CompanyName">Company Name
                                        :</label>

                                    <div class="col-md-8">
                                        <label class="form-control"
                                               for="${changeRequest.category.company.name}">${changeRequest.category.company.name}</label>
                                    </div>
                                </div>

                                <!-- select Box for Product-->
                                <div class="form-group">
                                    <label class="col-md-4 label_color control-label" for="ProductName">Product Name
                                        :</label>

                                    <div class="col-md-8">
                                        <label class="form-control"
                                               for="${changeRequest.category.product.name}">${changeRequest.category.product.name}</label>
                                    </div>
                                </div>

                                <!-- select Box for Product-->
                                <div class="form-group">
                                    <label class="col-md-4 label_color control-label" for="name">Category Name :</label>

                                    <div class="col-md-8">
                                        <label class="form-control"
                                               for="${changeRequest.category.name}">${changeRequest.category.name}</label>
                                    </div>
                                </div>


                                <!-- select Box for Product-->
                                <div class="form-group">
                                    <label class="col-md-4 label_color control-label" for="requestPriority">Request
                                        Priority :</label>

                                    <div class="col-md-4">
                                        <select id="requestPriority" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;">
                                            <option value="Low"  ${changeRequest.priority == 'Low' ? 'selected' : ''}>
                                                Low
                                            </option>
                                            <option value="Medium" ${changeRequest.priority == 'Medium' ? 'selected' : ''}>
                                                Medium
                                            </option>
                                            <option value="High" ${changeRequest.priority == 'High' ? 'selected' : ''}>
                                                High
                                            </option>
                                        </select>
                                        <label id="requestPriorityValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>


                                <div class="form-group" id="uploadedLogo">
                                    <label class="col-md-4 label_color control-label" for="logo">Current Attachment
                                        :</label>

                                    <div class="col-md-6">
                                        <img id="uploadedLogoSrc"
                                             alt="Current Attachment" src="${changeRequest.docPath}" width="70"
                                             height="70" border="0">
                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 label_color control-label" for="doc">Upload Document
                                        :</label>

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
                                    <label class="col-md-4 label_color control-label" for="name">Name :</label>

                                    <div class="col-md-6">
                                        <input type="hidden" class="form-control" id="version" name="version"
                                               value="${version}"
                                               required>
                                        <input type="hidden" class="form-control" id="changeRequestId"
                                               name="changeRequestId"
                                               value="${changeRequest.id}"
                                               required>
                                        <input type="hidden" class="form-control" id="approvalStatusId"
                                               name="approvalStatusId"
                                               value="${approvalStatus.id}"
                                               required>
                                        <input id="name" name="name" type="text" placeholder="" readonly
                                               class="form-control input-md" value="${changeRequest.name}"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="nameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>


                                <!-- Textarea -->
                                <div class="form-group">
                                    <%--<button class="add_field_button">Add More Description</button>--%>
                                    <label class="col-md-4 label_color control-label" for="description">Description
                                        :</label>


                                    <div class="col-md-6 input_fields_wrap">
                                        <c:forEach items="${changeRequest.description}" var="description"
                                                   varStatus="loop">
                                            <div>
                                        <textarea id="description" name="description[]"
                                                  class="form-control input-md description"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  rows="6" cols="20"> ${description}</textarea>
                                                <c:if test="${loop.index != 0}">
                                                    <a href="#" class="remove_field" style="font-size:12px;color: red">Remove</a>
                                                </c:if>
                                            </div>
                                            &nbsp;
                                            &nbsp;
                                        </c:forEach>
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
                                                type="button">Update
                                        </button>
                                        <a class="btn bg-grey waves-war" id="back" value="1" title="Back"
                                           href="/approval_details"><img
                                                src="resources/images/back.png" width="16" height="16" border="0">&nbsp;Back
                                        </a>
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
                    if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, request);
                });


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


                /*  Ajax call for save operation */

                function callAjaxForEditOperation(part1, part2, icn, msg, request) {
                    console.log(request);
                    $.ajax({
                        url: messageResource.get('request.edit.url', 'configMessageForUI'),
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
                                    localStorage.setItem('message_edit_request', part1);
                                    window.location = messageResource.get('approval_details.load.url', 'configMessageForUI');
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


                /*  prepare request object to save */

                function setRequestData() {
                    var requestPriority = $("#requestPriority option:selected").val();
                    var request = new FormData(document.getElementById("requestDetails"));
                    var descriptionListObj = {};
                    var descriptionList = $('.description');
                    for (var i = 0; i < descriptionList.length; i++) {
                        descriptionListObj['Key' + i] = descriptionList[i].value;
                    }
                    request.append('requestPriority', requestPriority);
                    return request;
                }


                /* Reset Request Form  */

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


                $(".input_fields_wrap").on("click", ".remove_field", function (e) { //user click on remove text
                    e.preventDefault();
                    $(this).parent('div').remove();
                });


            });

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>