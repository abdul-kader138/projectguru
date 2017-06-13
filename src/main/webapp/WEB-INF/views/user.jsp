<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>User List</h4></div>
                <hr/>
                <br/><br/>
                <table id="userTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="15px">id</th>
                        <th width="200px">Name</th>
                        <th width="200px">Email</th>
                        <th width="200px">Phone</th>
                        <th width="200px">Designation</th>
                        <th width="200px">Role</th>
                        <th width="200px">Photo</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editUser" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteUser" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshUser" value="1" title="Delete"><img
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


        <div class="row clearfix" id="userForm" style="display: none">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal" id="userDetails">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>User Setting</strong></legend>

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
                                <!-- Text input-->

                                <div class="form-group" id="passwordGroup">
                                    <label class="col-md-4 control-label" for="name">Password :</label>

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
                                    <label class="col-md-4 control-label" for="name">Confirm Password :</label>

                                    <div class="col-md-6">

                                        <input id="confirmPassword" name="confirmPassword" type="password" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="confirmPasswordValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group" id="emailGroup">
                                    <label class="col-md-4 control-label" for="name">Email :</label>

                                    <div class="col-md-6">

                                        <input id="email" name="email" type="text" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required=""/>
                                        <label id="emailValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="name">Phone :</label>

                                    <div class="col-md-6">

                                        <input id="phone" name="phone" type="text" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="phoneValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="name">Designation :</label>

                                    <div class="col-md-6">

                                        <input id="designation" name="designation" type="text" placeholder=""
                                               class="form-control input-md"
                                               style="border-color:#808080; border-width:1px; border-style:solid;"
                                               required="">
                                        <label id="designationValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>

                                <%--Select Box--%>
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="listOfRole">Role</label>

                                    <div class="col-md-4">
                                        <select id="listOfRole" class="form-control"
                                                style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                                        <label id="roleNameValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>

                                <%--logo show--%>
                                <div class="form-group" id="uploadedPhoto" style="display: none">
                                    <label class="col-md-4 control-label" for="uploadedPhoto">Current Photo</label>

                                    <div class="col-md-6">
                                        <img id="uploadedPhotoSrc"
                                             src="" width="70" height="70" border="0">
                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="photo">Photo</label>

                                    <div class="col-md-6">

                                        <input class="form-control input-md"
                                               type="file" name="photo" id="photo"/>
                                        <button id="photoClear" type="button">Clear</button>
                                        <label id="photoValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>
                                    </div>
                                </div>
                                <!-- Button -->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="saveUser"></label>

                                    <div class="col-md-4">
                                        <button id="saveUser" name="saveUser" class="btn btn-primary"
                                                type="button">Save
                                        </button>
                                        <button id="updateUser" name="updateUser" class="btn btn-primary"
                                                type="button">Update
                                        </button>
                                        <button style="position: static" id="resetUser" name="resetUser"
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
                initializeUserForm();
                $("saveUser").show();
                $("#updateUser").hide();
                var mainPath = document.origin + "/PG";
                var companyGb;
                getAllRole();


                /* populate User list when page load */

                $('#userTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/user/userList",
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
                        {"mData": "email", 'sWidth': '200px'},
                        {"mData": "phone", 'sWidth': '200px'},
                        {"mData": "designation", 'sWidth': '200px'},
                        {"mData": "role", 'sWidth': '200px'},
                        {
                            "mData": "path",
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


                /* Save User data using ajax */

                $("#saveUser").click(function (event) {
                    initFormValidationMsg();
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "";
                    var user=setRoleName();
                    if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, user);
                });



                /* Update User data using ajax */

                $('#editUser').click(function () {
                    document.getElementById('userForm').style.display = "none";
                    initializeUserForm();
                    initFormValidationMsg();
                    var newUser =setRoleName();
                    newUser = companyGb;
                    companyGb = null;
                    var data = messageResource.get('user.edit.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newUser == null)showServerSideMessage(data, "", 0, "Message");
                    else setDataToUserForm(newUser);
                });

                var table = $('#userTable').DataTable();

                $("#updateUser").click(function (event) {
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var user=setRoleName();
                    if (formValidationForUpdate()) callAjaxForEditOperation(part1, part2, icn, msg, user);

                });


                /* Delete User data using ajax */

                $("#deleteUser").click(function (event) {
                    document.getElementById('userForm').style.display = "none";
                    initializeUserForm();
                    initFormValidationMsg();
                    var newUser = new Object();
                    newUser = companyGb;
                    companyGb = null;
                    var part1 = "";
                    var part2 = "";
                    var icn = 0;
                    var msg = "Message";
                    var data = messageResource.get('user.delete.validation.msg', 'configMessageForUI');

                    if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
                    else if (newUser == null)showServerSideMessage(data, "", 0, "Message");
                    else {
                        $.dialogbox({
                            type: 'msg',
                            title: 'Confirm Title',
                            content: messageResource.get('user.delete.confirm.msg', 'configMessageForUI'),
                            closeBtn: true,
                            btn: ['Confirm', 'Cancel'],
                            call: [
                                function () {
                                    $.dialogbox.close();
                                    callAjaxForDeleteOperation(part1, part2, icn, msg, newUser);

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

                $('#userTable tbody').on('click', 'tr', function () {
                    companyGb = table.row(this).data();
                    var isChecked = $('#' + companyGb.id).is(":checked");
                    if (isChecked == false) companyGb = null;
                });


                /* Initialize html form value  based on reset button*/

                $('#resetUser').on('click', function () {
                    companyGb = null;
                    initializeUserForm();
                    initFormValidationMsg();
                    $('#saveUser').show();
                    $('#updateUser').hide();
                    uncheckedAllCheckBox();
                    window.location.href = "#viewTableData";
                    document.getElementById('userForm').style.display = "none";
                    document.getElementById('uploadedPhoto').style.display = "none";
                });


                /* load table data on click refresh button*/

                $('#refreshUser').on('click', function () {
                    initializeUserForm();
                    initFormValidationMsg();
                    companyGb = null;
                    table.ajax.url(messageResource.get('user.list.load.url', 'configMessageForUI')).load();
                    document.getElementById('userForm').style.display = "none";
                    document.getElementById('uploadedPhoto').style.display = "none";
                });


                /*  Ajax call for delete operation */

                function callAjaxForDeleteOperation(part1, part2, icn, msg, newUser) {
                    console.log(JSON.stringify(newUser.id));
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': messageResource.get('user.delete.url', 'configMessageForUI'),
                        'data': JSON.stringify(newUser.id),
                        'dataType': 'json',
                        'success': function (d) {
                            if (d.successMsg) {
                                icn = 1;
                                msg = "";
                                part1 = d.successMsg;
                                showServerSideMessage(part1, part2, icn, msg);
//                                deleteDataRow(newUser.id,'userTable');
                                table.ajax.url(messageResource.get('user.list.load.url', 'configMessageForUI')).load();
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

                function callAjaxForEditOperation(part1, part2, icn, msg, user) {
                    $.ajax({
                        url: messageResource.get('user.edit.url', 'configMessageForUI'),
                        type: "POST",
                        data: user,
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false
                    })
                            .done(function (d) {
                                if (d.successMsg) {
                                    icn = 1;
                                    part1 = d.successMsg;
                                    initializeUserForm();
                                    window.location.href = "#viewTableData";
                                    $("#updateUser").hide();
                                    $("#saveUser").show();
                                    document.getElementById('userForm').style.display = "none";
                                    document.getElementById('uploadedPhoto').style.display = "none";
                                    showServerSideMessage(part1, part2, icn, msg);
                                    table.ajax.url(messageResource.get('user.list.load.url', 'configMessageForUI')).load();
                                }
                                if (d.validationError) {
                                    icn = 0;
                                    msg = "";
                                    msg = '<strong style="color: red">Error</strong>';
                                    part2 = d.validationError;
                                    uncheckedAllCheckBox();
                                    showServerSideMessage(part1, part2, icn, msg);
                                }
                            }).
                            fail(function (textStatus) {
                                icn = 0;
                                msg = '<strong style="color: red">Error</strong>';
                                uncheckedAllCheckBox();
                                showServerSideMessage(part1, getErrorMessage(textStatus), icn, msg);
                            });
                }


                /*  Ajax call for save operation */

                function callAjaxForAddOperation(part1, part2, icn, msg, user) {

                    $.ajax({
                        url: messageResource.get('user.save.url', 'configMessageForUI'),
                        type: "POST",
                        data:user,
                        enctype: 'multipart/form-data',
                        processData: false,
                        contentType: false
                    }).
                            done(function (d) {
                                if (d.successMsg) {
                                    icn = 1;
                                    msg = "";
                                    part1 = d.successMsg;
                                    initializeUserForm();
//                                    setNewDataTableValue(d.user, table);
                                    window.location.href = "#viewTableData";
                                    showServerSideMessage(part1, part2, icn, msg);
                                    table.ajax.url(messageResource.get('user.list.load.url', 'configMessageForUI')).load();
                                    document.getElementById('userForm').style.display = "none";
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


                /* Initialize html form value */

                function initializeUserForm() {
                    $("#id").val("0");
                    $("#version").val("0");
                    $("#name").val("");
                    $("#email").val("");
                    $("#password").val("");
                    $("#confirmPassword").val("");
                    $("#phone").val("");
                    $("#designation").val("");
                    $("#photo").val("");
                    $('#defaultOpt').val('0').prop('selected', true);
                }


                /* html form Validation for save */

                function formValidation() {
                    var isValid = true;
                    var name = $("#name").val();
                    var email = $("#email").val();
                    var password = $("#password").val();
                    var confirmPassword = $("#confirmPassword").val();
                    var phone = $("#phone").val();
                    var designation = $("#designation").val();
                    var filename = $("#photo").val();
                    var roleId = $("#listOfRole option:selected").val();

                    isValid=blankCheck(name,"name",isValid);

                    //password check
                    if(isValid == true) isValid=passwordCheck(password,confirmPassword,"password", "confirmPassword");

                    // email check
                    if(isValid == true) isValid=blankCheck(email,"email",isValid);
                    if ((!/^([a-zA-Z0-9_.-])+@(([a-zA-Z0-9-])+.)+([a-zA-Z0-9]{2,4})+$/g.test(email)) && isValid == true){
                        $("#emailValidation").text('Only valid email is allowed!');
                        isValid = false;
                    }

                    // Phone Check
                    if(isValid == true) isValid=blankCheck(phone,"phone",isValid);
                    if ((!/^\d{11}$/g.test(phone)) && isValid == true){
                        $("#phoneValidation").text('Only 11 digit phone no is allowed!');
                        isValid = false;
                    }
                    if(isValid == true) isValid=blankCheck(designation,"designation",isValid);
                    if ((roleId == null) || (roleId == "0") && isValid==true) {
                        $("#roleNameValidation").text("Role name is required");
                        isValid = false;
                    }
                    if (!(isJpg(filename) || isPng(filename) || isGif(filename)) && isValid==true) {
                        $("#photoValidation").text('Please browse a JPG/PNG/GIF file to upload ...');
                        isValid = false;
                    }
                    return isValid;
                }



                /* html form Validation for update */

                function formValidationForUpdate() {
                    var isValid = true;
                    var name = $("#name").val();
                    var phone = $("#phone").val();
                    var designation = $("#designation").val();
                    var filename = $("#photo").val();
                    var roleId = $("#listOfRole option:selected").val();

                    isValid=blankCheck(name,"name",isValid);

                    // Phone Check
                    if(isValid == true) isValid=blankCheck(phone,"phone",isValid);
                    if ((!/^\d{11}$/g.test(phone)) && isValid == true){
                        $("#phoneValidation").text('Only 11 digit phone no is allowed!');
                        isValid = false;
                    }
                    if(isValid == true) isValid=blankCheck(designation,"designation",isValid);
                    if ((roleId == null) || (roleId == "0") && isValid==true) {
                        $("#roleNameValidation").text("Role name is required");
                        isValid = false;
                    }
                    return isValid;
                }



                /* Initialize html form validation error field*/

                function initFormValidationMsg() {
                    $("#nameValidation").text("");
                    $("#emailValidation").text("");
                    $("#passwordValidation").text("");
                    $("#confirmPasswordValidation").text("");
                    $("#phoneValidation").text("");
                    $("#designationValidation").text("");
                    $("#photoValidation").text("");
                    $("#roleNameValidation").text("");

                }

                /* clear Photo field*/

                $('#PhotoClear').on('click', function () {
                    $("#photo").val("");
                });




                /* move to add new user div*/

                $('#moveToAdd').on('click', function () {
                    document.getElementById('userForm').style.display = "block";
                    companyGb = null;
                    $("#updateUser").hide();
                    $("#saveUser").show();
                    $("#passwordGroup").show();
                    $("#confirmPasswordGroup").show();
                    $("#emailGroup").show();
                    initializeUserForm();
                    initFormValidationMsg();
                    uncheckedAllCheckBox();
                    window.location.href = "#userForm";
                });



                /* set selected row data to user form for edit */

                function setDataToUserForm(newUser) {
                    document.getElementById('userForm').style.display = "block";
                    document.getElementById('uploadedPhoto').style.display = "block";
                    $("#updateUser").show();
                    $("#saveUser").hide();
                    $("#passwordGroup").hide();
                    $("#confirmPasswordGroup").hide();
                    $("#emailGroup").hide();
                    $("#id").val(newUser.id);
                    $("#name").val(newUser.name);
                    $("#version").val(newUser.version);
                    $("#phone").val(newUser.phone);
                    $("#designation").val(newUser.designation);
                    $("#uploadedPhotoSrc").attr("src", mainPath + newUser.imagePath);
                    $('#listOfRole option:contains("' + newUser.role + '")').prop('selected', 'selected');
                    window.location.href = "#userForm";
                }

                /* Load Role data to select box data using ajax */

                function getAllRole() {
                    $('#listOfRole').empty();
                    $.ajax({
                        type: "GET",
                        url: 'http://localhost:8080/roles/rolesList',
                        success: function (data) {
                            var collaboration;
                            collaboration += '<option id="defaultOpt" value="0">Select Role</option>';
                            $.each(data, function (i, d) {
                                collaboration += "<option value=" + d.id + ">" + d.name + "</option>";
                            });

                            $('#listOfRole').append(collaboration);
                        },
                        error: function (e) {
                        }
                    });
                }


                /* Set new created user value to DataTable*/

                function setNewDataTableValue(user, table) {
                    table.row.add({
                        "id": user.id,
                        "name": user.name,
                        "email": user.email,
                        "phone": user.phone,
                        "designation": user.designation,
                        "version": user.version,
                        "role": user.role,
                        "path": user.imagePath
                    }).draw();

                };


                function setRoleName(){
                    var roleId=$("#listOfRole option:selected").val();
                    var roleName=$("#listOfRole option:selected").text();
                    var user=new FormData(document.getElementById("userDetails"));
                    user.append('roleId', roleId);
                    user.append('roleName', roleName);
                    return user;
                }

            });
            //

        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>