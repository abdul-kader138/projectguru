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
                                <legend><strong>Change Password</strong></legend>

                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="oldPassword">Old Password :</label>

                                    <div class="col-md-6">

                                        <input id="oldPassword" name="oldPassword" type="password" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="oldPasswordValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="password">New Password :</label>

                                    <div class="col-md-6">

                                        <input id="password" name="password" type="password" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="passwordValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>


                                <!-- Text input-->
                                <div class="form-group" id="confirmPasswordGroup">
                                    <label class="col-md-4 control-label" for="confirmPassword">Confirm Password
                                        :</label>

                                    <div class="col-md-6">

                                        <input id="confirmPassword" name="confirmPassword" type="password"
                                               placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="confirmPasswordValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>


                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="savePassword"></label>

                                    <div class="col-md-4">
                                        <button id="savePassword" name="savePassword" class="btn btn-primary"
                                                type="button">Save
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
                initializePasswordForm();


                /* Save User data using ajax */

                $("#savePassword").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var user = new Object();
                    user.oldPassword = $("#oldPassword").val();
                    user.password = $("#password").val();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, user);
                });


                /* Initialize html form value  based on reset button*/

                $('#resetUser').on('click', function () {
                    initializePasswordForm();
                    initFormValidationMsg();
                });


                /*  Ajax call for add operation */

                function callAjaxForAddOperation(part1, part2, icn, msg, newUser) {
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('user.change.password.url', 'configMessageForUI'),
                        'data': JSON.stringify(newUser),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                window.location.href = messageResource.get('user.logout.url', 'configMessageForUI');
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


                /* Initialize html form value */

                function initializePasswordForm() {
                    $("#oldPassword").val("");
                    $("#password").val("");
                    $("#confirmPassword").val("");
                }


                /* html form Validation for save */

                function formValidation() {
                    var isValid = true;
                    var oldPassword = $("#oldPassword").val();
                    var password = $("#password").val();
                    var confirmPassword = $("#confirmPassword").val();


                    //password check
                    if (isValid == true) isValid = passwordCheck(password, confirmPassword, "password", "confirmPassword");

                    if (oldPassword.trim() === password.trim && isValid == true) {
                        $("#oldPasswordValidation").text("Old password and New Password didn't match.");
                        isValid = false;
                    }
                    return isValid;
                }


                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#oldPasswordValidation").text("");
                    $("#passwordValidation").text("");
                    $("#confirmPasswordValidation").text("");
                }

            });

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>