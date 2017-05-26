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
          </tr>
          </thead>
        </table>
        <br/>
        <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
        </button>
        &nbsp;
        &nbsp;

        <button type="button" class="btn bg-grey waves-war" id="editDesignation" value="1" title="Edit"><img
                src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
        </button>
        &nbsp;
        &nbsp;
        <button type="button" class="btn bg-grey waves-war" id="deleteDesignation" value="1" title="Delete"><img
                src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

        </button>
        &nbsp;
        &nbsp;
        <button type="button" class="btn bg-grey waves-war" id="refreshDesignation" value="1" title="Delete"><img
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


    <div class="row clearfix" id="designationForm">
      <div class="col-xs-8 col-xs-offset-2">
        <div class="card">
          <div class="header" style="background-color:#a5a5a5">
            <h2><strong>&nbsp;</strong></h2>
          </div>
          <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
            <form class="form-horizontal">
              <fieldset>

                <!-- Form Name -->
                <legend><strong>Designation Setting</strong></legend>

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



                <!-- Button -->
                <div class="form-group">
                  <label class="col-md-4 control-label" for="saveDesignation"></label>

                  <div class="col-md-4">
                    <button id="saveDesignation" name="saveDesignation" class="btn btn-primary"
                            type="button">Save
                    </button>
                    <button id="updateDesignation" name="updateDesignation" class="btn btn-primary"
                            type="button">Update
                    </button>
                    <button style="position: static" id="resetDesignation" name="resetDesignation"
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

        var loading = $.loading();
        initFormValidationMsg();
        initializeDesignationForm();
        $("saveDesignation").show();
        $("#updateDesignation").hide();
        var companyGb;


        /* populate Designation list when page load */

        $('#designationTable').DataTable({
          "sAjaxSource": "http://localhost:8080/designation/designationList",
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
            {"mData": "name",'sWidth': '200px'}
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


        /* Save Designation data using ajax */

        $("#saveDesignation").click(function (event) {
          initFormValidationMsg();
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "";
          var designation = new Object();
          designation.name = $("#name").val();
          if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, designation);
        });


        /* Update Designation data using ajax */

        $('#editDesignation').click(function () {
          initializeDesignationForm();
          initFormValidationMsg();
          var designation = new Object();
          var designation = companyGb;
          companyGb = null;
          var data=messageResource.get('designation.edit.validation.msg', 'configMessageForUI');

          if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
          else if (designation == null)showServerSideMessage(data, "", 0, "Message");
          else {
            $("#updateDesignation").show();
            $("#saveDesignation").hide();
            $("#id").val(designation.id);
            $("#name").val(designation.name);
            $("#version").val(designation.version);
            window.location.href = "#designationForm";
          }
        });

        var table = $('#designationTable').DataTable();

        $("#updateDesignation").click(function (event) {
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "Message";
          var designation = new Object();
          designation.id = $("#id").val();
          designation.version = $("#version").val();
          designation.name = $("#name").val();
          if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, designation);

        });


        /* Delete Designation data using ajax */

        $("#deleteDesignation").click(function (event) {
          var designation = new Object();
          designation = companyGb;
          companyGb = null;
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "Message";
          var data = messageResource.get('designation.delete.validation.msg', 'configMessageForUI');

          if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
          else if (designation == null)showServerSideMessage(data, "", 0, "Message");
          else {
            $.dialogbox({
              type: 'msg',
              title: 'Confirm Title',
              content: messageResource.get('designation.delete.confirm.msg', 'configMessageForUI'),
              closeBtn: true,
              btn: ['Confirm', 'Cancel'],
              call: [
                function () {
                  $.dialogbox.close();
                  callAjaxForDeleteOperation(part1, part2, icn, msg, designation);

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

        $('#designationTable tbody').on('click', 'tr', function () {
          companyGb = table.row(this).data();
          var isChecked = $('#' + companyGb.id).is(":checked");
          if (isChecked == false) companyGb = null;
        });


        /* Initialize html form value  based on reset button*/

        $('#resetDesignation').on('click', function () {
          companyGb = null;
          initializeDesignationForm();
          initFormValidationMsg();
          $('#saveDesignation').show();
          $('#updateDesignation').hide();
          uncheckedAllCheckBox();
        });



        /* load table data on click refresh button*/

        $('#refreshDesignation').on('click', function () {
          initializeDesignationForm();
          initFormValidationMsg();
          companyGb = null;
          table.ajax.url(messageResource.get('designation.list.load.url', 'configMessageForUI')).load();
        });


        /*  Ajax call for delete operation */

        function callAjaxForDeleteOperation(part1, part2, icn, msg, designation) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('designation.delete.url', 'configMessageForUI'),
            'data': JSON.stringify(designation),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                msg = "";
                part1 = d.successMsg;
                showServerSideMessage(part1, part2, icn, msg);
                table.ajax.url(messageResource.get('designation.list.load.url', 'configMessageForUI')).load();
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

        function callAjaxForEditOperation(part1, part2, icn, msg, designation) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('designation.edit.url', 'configMessageForUI'),
            'data': JSON.stringify(designation),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                part1 = d.successMsg;
                initializeDesignationForm();
                window.location.href = "#viewTableData";
                $("#updateDesignation").hide();
                $("#saveDesignation").show();
                showServerSideMessage(part1, part2, icn, msg);
                table.ajax.url(messageResource.get('designation.list.load.url', 'configMessageForUI')).load();
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

        function callAjaxForAddOperation(part1, part2, icn, msg, designation) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('designation.save.url', 'configMessageForUI'),
            'data': JSON.stringify(designation),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                msg = "";
                part1 = d.successMsg;
                initializeDesignationForm();
                setNewDataTableValue(d.designation, table);
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

        function initializeDesignationForm() {
          $("#id").val("");
          $("#version").val("");
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
          companyGb = null;
          $("#updateDesignation").hide();
          $("#saveDesignation").show();
          uncheckedAllCheckBox();
          window.location.href = "#designationForm";
        });


        /* Set new created designation value to DataTable*/

        function setNewDataTableValue(designation, table) {
          table.row.add({
            "id": designation.id,
            "name": designation.name,
            "version": designation.version,
            "createdBy": designation.createdBy,
            "createdOn": designation.createdOn,
            "updatedBy": designation.updatedBy,
            "updatedOn": designation.updatedOn
          }).draw();

        };


      });
      //

    </script>
  </div>


</section>

<%@ include file="footer.jsp" %>