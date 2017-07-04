<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">
        <div class="row clearfix" id="userForm">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal" id="userDetails">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Decline Request</strong></legend>

                                <!-- Text Label-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="company">Company Name:</label>
                                    <input id="approveId" type="hidden" value="${approveId}"/>
                                    <input id="requestedId" type="hidden" value="${requestedId}"/>
                                    <input id="version" type="hidden" value="${version}"/>

                                    <div class="col-md-4">
                                        <label id="companyName" class="form-control">${companyName}</label>
                                    </div>
                                </div>

                                <!-- Text Label-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="company">Product Name:</label>

                                    <div class="col-md-4">
                                        <label id="productName" class="form-control">${productName}</label>
                                    </div>
                                </div>

                                <!-- Text Label-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="company">Category Name:</label>

                                    <div class="col-md-4">
                                        <label id="categoryName" class="form-control">${categoryName}</label>
                                    </div>
                                </div>


                                <!-- Text Label-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="company">Request Name:</label>

                                    <div class="col-md-4">
                                        <label id="company" class="form-control">${name}</label>
                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="cause">Decline Cause :</label>

                                    <div class="col-md-6">
                                        <textarea id="cause" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="description" rows="6" cols="20"></textarea>
                                        <label id="causeValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>


                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="decline"></label>

                                    <div class="col-md-4">
                                        <button id="decline" name="decline" class="btn btn-primary"
                                                type="button">Decline
                                        </button>
                                        <button style="position: static" id="cancel" name="cancel"
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
    </div>

    <script>
        $(document).ready(function () {
            $("#cause").val("");
            $("#causeValidation").text("");

            $("#decline").click(function () {
                $("#causeValidation").text("");
                /*  Ajax call for save operation */
                var obj = new Object();
                obj.approveId = $('#approveId').val();
                obj.requestedId = $('#requestedId').val();
                obj.cause = $('#cause').val();
                obj.version = $('#version').val();

                if (formValidation())callAjaxForAddOperation("", "", 0, "Message",obj);


            });

            /*  Ajax call for add operation */

            function callAjaxForAddOperation(part1, part2, icn, msg, obj) {
                console.log(obj);
                $.ajax({
                    headers: {
                        'Accept': 'application/json',
                        'Content-Type': 'application/json'
                    },
                    'type': 'POST',
                    'url': messageResource.get('decline.save.url', 'configMessageForUI'),
                    'data': JSON.stringify(obj),
                    'dataType': 'json',
                    'success': function (d) {
                        if (d.successMsg) {
                            icn = 1;
                            msg = "";
                            part1 = d.successMsg;
                            showServerSideMessage(part1, part2, icn, msg);
                            $("#cause").val("");
                            $("#causeValidation").text("");
                            window.location=messageResource.get('approval_details.load.url', 'configMessageForUI');
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


            /* html form Validation */

            function formValidation() {
                var isValid = true;
                var cause = $("#cause").val();
                if (cause == null || cause.trim().length == 0) {
                    $("#causeValidation").text("Decline cause is required");
                    isValid = false;
                }
                return isValid;
            }

        });

    </script>
</section>


<%@ include file="footer.jsp" %>