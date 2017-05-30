<%@ include file="header.jsp" %>

<section class="content">
  <div class="container-fluid">

    <%--start of table div--%>

    <div id="viewTableData"></div>
    <div class="row clearfix">
      <div class="col-xs-10 col-xs-offset-1 card">
        <br/><br/>
        <table id="designationTable" class="display nowrap" cellspacing="0" width="100%">
          <thead>
          <tr>
            <th width="15px">id</th>
            <th width="200px">Name</th>
            <th width="200px">Email</th>
            <th width="200px">Phone</th>
            <%--<th width="200px">Designation</th>--%>
            <%--<th width="200px">Department</th>--%>
            <%--<th width="200px">Company</th>--%>
            <th width="200px">Role</th>
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
        <button type="button" class="btn bg-grey waves-war" id="refreshUser" value="1" title="Delete">
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


      <div class="row clearfix" id="userForm">
        <div class="col-xs-8 col-xs-offset-2">
          <div class="card">
            <div class="header" style="background-color:#a5a5a5">
              <h2><strong>&nbsp;</strong></h2>
            </div>
            <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">

              <form class="form-horizontal">
                <fieldset>

                  <!-- Form Name -->
                  <legend>User Setting</legend>

                  <!-- Text input-->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="name">Name</label>

                    <div class="col-md-6">
                      <input type="hidden" class="form-control" id="id" name="id" value="0" required>
                      <input type="hidden" class="form-control" id="version" name="version" value="0"
                             required>
                      <input class="form-control input-md"
                             style="border:solid; border-width: 1px; border-color:#a5a5a5;" id="name"
                             name="name" class="form-control input-md" required="" type="text">
                      <label id="nameValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>

                    </div>
                  </div>

                  <!-- Text input-->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="email">Email</label>

                    <div class="col-md-6">
                      <input class="form-control input-md"
                             style="border:solid; border-width: 1px; border-color:#a5a5a5;" id="email"
                             name="email" placeholder="" class="form-control input-md" required=""
                             type="text">
                      <label id="emailValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>

                    </div>
                  </div>

                  <!-- Text input-->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="phone">Phone</label>

                    <div class="col-md-6">
                      <input class="form-control input-md"
                             style="border:solid; border-width: 1px; border-color:#a5a5a5;" id="phone"
                             name="phone" placeholder="" class="form-control input-md" required=""
                             type="text">
                      <label id="phoneValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>
                    </div>
                  </div>

                  <!-- Select Basic -->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="designation">Designation</label>

                    <div class="col-md-6">
                      <select style="border:solid; border-width: 1px; border-color:#a5a5a5;"
                              id="designation" name="designation" class="form-control">
                        <option value="1">Option one</option>
                        <option value="2">Option two</option>
                      </select>
                      <label id="designationValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>
                    </div>
                  </div>

                  <!-- Select Basic -->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="department">Departent</label>

                    <div class="col-md-6">
                      <select style="border:solid; border-width: 1px; border-color:#a5a5a5;"
                              id="department" name="department" class="form-control">
                        <option value="1">Option one</option>
                        <option value="2">Option two</option>
                      </select>
                      <label id="departmentValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>
                    </div>
                  </div>

                  <!-- Select Basic -->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="company">Comapny</label>

                    <div class="col-md-6">
                      <select style="border:solid; border-width: 1px; border-color:#a5a5a5;"
                              id="company" name="company" class="form-control">
                        <option value="1">Option one</option>
                        <option value="2">Option two</option>
                      </select>
                      <label id="companyValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>
                    </div>
                  </div>

                  <!-- Select Basic -->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="role">Role</label>

                    <div class="col-md-6">
                      <select style="border:solid; border-width: 1px; border-color:#a5a5a5;" id="role"
                              name="role" class="form-control">
                        <option class="mark-border" tion value="1">Option one</option>
                        <option value="2">Option two</option>
                      </select>
                      <label id="roleValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>
                    </div>
                  </div>

                  <!-- Text input-->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="phone">Photo</label>

                    <div class="col-md-6">

                      <input class="form-control input-md"
                             type="file" name="photo" id="photo"/>
                      <button id="photoClear" type="button">Clear</button>
                      <label id="photoValidation" style="color:red; font-size: 11px;"
                             class="form-control"></label>
                    </div>
                  </div>

                  <!-- Text input-->
                  <div class="form-group">
                    <label class="col-md-4 control-label" for="phone">Photo</label>

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
                    <label class="col-md-4 control-label" for="saveUser"></label>

                    <div class="col-md-6">
                      <button id="saveUser" name="saveUser" class="btn btn-primary"
                              type="button">Save
                      </button>
                      <button id="updateUser" name="updateUser" class="btn btn-primary"
                              type="button">Update
                      </button>
                      <button id="resetUser" name="resetUser"
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
        var companyGb;


        /* populate User list when page load */

        $('#userTable').DataTable({
          "sAjaxSource": "http://localhost:8080/user/userList1",
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
            {"mData": "role", 'sWidth': '200px'}
//            {"mData": "name", 'sWidth': '200px'}
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


        /* Save User data using ajax */

        $("#saveUser").click(function (event) {
          initFormValidationMsg();
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "";
          var user = new Object();
          user.name = $("#user").val();
          if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, user);
        });


        /* Update user data using ajax */

        $('#editUser').click(function () {
          initializeDesignationForm();
          initFormValidationMsg();
          var user = new Object();
          user = companyGb;
          companyGb = null;
          var data = messageResource.get('user.edit.validation.msg', 'configMessageForUI');

          if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
          else if (designation == null)showServerSideMessage(data, "", 0, "Message");
          else {
            document.getElementById('designationForm').style.display = "block";
            $("#updateUser").show();
            $("#saveUser").hide();
            $("#id").val(user.id);
            $("#name").val(user.name);
            $("#version").val(user.version);
            window.location.href = "#UserForm";
          }
        });

        var table = $('#userTable').DataTable();

        $("#updateDesignation").click(function (event) {
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "Message";
          var user = new Object();
          user.id = $("#id").val();
          user.version = $("#version").val();
          user.name = $("#name").val();
          if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, user);

        });


        /* Delete user data using ajax */

        $("#deleteUser").click(function (event) {
          document.getElementById('userForm').style.display = "none";
          initializeDesignationForm();
          initFormValidationMsg();
          var user = new Object();
          user = companyGb;
          companyGb = null;
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "Message";
          var data = messageResource.get('user.delete.validation.msg', 'configMessageForUI');

          if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
          else if (designation == null)showServerSideMessage(data, "", 0, "Message");
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
                  callAjaxForDeleteOperation(part1, part2, icn, msg, user);

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
        });


        /* load table data on click refresh button*/

        $('#refreshUser').on('click', function () {
          initializeUserForm();
          initFormValidationMsg();
          companyGb = null;
          table.ajax.url(messageResource.get('user.list.load.url', 'configMessageForUI')).load();
          document.getElementById('userForm').style.display = "none";
        });


        /*  Ajax call for delete operation */

        function callAjaxForDeleteOperation(part1, part2, icn, msg, user) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('user.delete.url', 'configMessageForUI'),
            'data': JSON.stringify(user),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                msg = "";
                part1 = d.successMsg;
                showServerSideMessage(part1, part2, icn, msg);
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
              icn = 0;
              msg = '<strong style="color: red">Error</strong>';
              showServerSideMessage(part1, getErrorMessage(error), icn, msg);
            }
          });
        }


        /*  Ajax call for edit operation */

        function callAjaxForEditOperation(part1, part2, icn, msg, user) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('user.edit.url', 'configMessageForUI'),
            'data': JSON.stringify(user),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                part1 = d.successMsg;
                initializeUserForm();
                window.location.href = "#viewTableData";
                $("#updateUser").hide();
                $("#saveUser").show();
                showServerSideMessage(part1, part2, icn, msg);
                document.getElementById('userForm').style.display = "none";
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

        function callAjaxForAddOperation(part1, part2, icn, msg, user) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('user.save.url', 'configMessageForUI'),
            'data': JSON.stringify(user),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                msg = "";
                part1 = d.successMsg;
                initializeUserForm();
                setNewDataTableValue(d.user, table);
                window.location.href = "#viewTableData";
                document.getElementById('userForm').style.display = "none";
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

        function initializeUserForm() {
          $("#id").val("0");
          $("#version").val("0");
          $("#name").val("");
        }


        /* html form Validation */

        function formValidation() {
          var isValid = true;
          var name = $("#name").val();
          if (name == null || name.trim().length == 0) {
            $("#nameValidation").text("Name is required");
            isValid = false;
          }
          return isValid;
        }


        /* Initialize html form validation error field*/

        function initFormValidationMsg() {
          $("#nameValidation").text("");

        }


        /* move to add new designation div*/

        $('#moveToAdd').on('click', function () {
          document.getElementById('userForm').style.display = "block";
          companyGb = null;
          $("#updateUser").hide();
          $("#saveUser").show();
          uncheckedAllCheckBox();
          initializeUserForm();
          initFormValidationMsg();
          window.location.href = "#userForm";
        });


        /* Set new created designation value to DataTable*/

        function setNewDataTableValue(user, table) {
          table.row.add({
            "id": user.id,
            "name": user.name,
            "version": user.version,
            "email": user.version,
            "phone": user.version,
            "role": user.version,
            "createdBy": user.createdBy,
            "createdOn": user.createdOn,
            "updatedBy": user.updatedBy,
            "updatedOn": user.updatedOn
          }).draw();

        };


      });
      //

      $('#btnUpload').on('click', function() {
        var file = $('[name="file"]');
        var dname = $('name').val();
        var imgContainer = $('#imgContainer');
        var filename = $.trim(file.val());

        if (!(isJpg(filename) || isPng(filename))) {
          alert('Please browse a JPG/PNG file to upload ...');
          return;
        }

        $.ajax({
          url: 'http://localhost:8080/department/echofile',
          type: "POST",
          data: new FormData(document.getElementById("fileForm")),
          enctype: 'multipart/form-data',
          processData: false,
          contentType: false
        }).done(function(data) {
          imgContainer.html('');
          var img = '<img src="data:' + data.contenttype + ';base64,'
                  + data.base64 + '"/>';

          imgContainer.append(img);
        }).fail(function(jqXHR, textStatus) {
          //alert(jqXHR.responseText);
          alert('File upload failed ...');
        });

      });

    </script>
  </div>


</section>

<%@ include file="footer.jsp" %>