<%@ include file="header.jsp" %>

<section class="content">
  <div class="container-fluid">

    <%--start of table div--%>

    <div id="viewTableData"></div>
    <div class="row clearfix">
      <div class="col-xs-10 col-xs-offset-1 card">
        <br/><br/>
        <table id="projectTable" class="display nowrap" cellspacing="0" width="100%">
          <thead>
          <tr>
            <th>id</th>
            <th>Name</th>
            <th>Company Name</th>
          </tr>
          </thead>
        </table>
        <br/>
        <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
        </button>
        &nbsp;
        &nbsp;

        <button type="button" class="btn bg-grey waves-war" id="editCompany" value="1" title="Edit"><img
                src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
        </button>
        &nbsp;
        &nbsp;
        <button type="button" class="btn bg-grey waves-war" id="deleteProject" value="1" title="Delete"><img
                src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

        </button>
        &nbsp;
        &nbsp;
        <button type="button" class="btn bg-grey waves-war" id="refreshProject" value="1" title="Delete"><img
                src="resources/images/refresh.png" width="16" height="16" border="0">&nbsp;Refresh</button>
        &nbsp;<br/><br/>
        &nbsp;<br/><br/>
      </div>

      <br/>
    </div>

    <%--end of table div--%>


    <br/><br/><br/>


    <%--start of save/update modal--%>


    <div class="row clearfix" id="projectForm">
      <div class="col-xs-8 col-xs-offset-2">
        <div class="card">
          <div class="header" style="background-color:#a5a5a5">
            <h2><strong>&nbsp;</strong></h2>
          </div>
          <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
            <form class="form-horizontal">
              <fieldset>

                <!-- Form Name -->
                <legend><strong>Project Setting</strong></legend>

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



                <div class="form-group">
                  <label class="col-md-4 control-label" for="listOfCompany">Company Name</label>
                  <div class="col-md-4">
                    <select id="listOfCompany" class="form-control" style="border-color:#808080; border-width:1px; border-style:solid;"></select>
                    <label id="companyNameValidation" style="color:red; font-size: 11px;"
                           class="form-control"></label>
                  </div>
                </div>

                <!-- Button -->
                <div class="form-group">
                  <label class="col-md-4 control-label" for="saveProject"></label>

                  <div class="col-md-4">
                    <button id="saveProject" name="saveProject" class="btn btn-primary"
                            type="button">Save
                    </button>
                    <button id="updateProject" name="saveProject" class="btn btn-primary"
                            type="button">Update
                    </button>
                    <button id="resetProject" name="resetProject" class="btn bg-grey"
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
        $("saveProject").show();
        $("#updateProject").hide();
        var companyGb;


        /* populate Company list when page load */

        $('#projectTable').DataTable({
          "sAjaxSource": "http://localhost:8080/projectList",
          "sAjaxDataProp": "",
          "order": [[0, "asc"]],
          "aoColumns": [
            {"mData": "id"},
            {"mData": "name"},
            {"mData": "company.name"}
          ],
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
//                    "scrollY": 400,
          "scrollX": true

        });


        /* Load company data to select box data using ajax */

        function getAllCompany()
        {
          $('#getAllCompany').empty();
          $.ajax({
            type: "GET",
            url: "http://localhost:8080/companyList",
            success: function(data){
              console.log(data);
              // Parse the returned json data
//              var opts = $.parseJSON(data);
              // Use jQuery's each to iterate over the opts value
              var collaboration;
              $.each(data, function(i, d) {
                console.log(d.id);
                console.log(d.name);
                // You will need to alter the below to get the right values from your json object.  Guessing that d.id / d.modelName are columns in your carModels data
                collaboration += "<option value="+ d.id+">"+d.name+"</option>";
              });
              collaboration+='<option value="0" selected>Select Company</option>';
              $('#listOfCompany').append(collaboration);
            },
            error:function(e){
              console.log(e);
            }
          });
        }

        getAllCompany();

        /* Save company data using ajax */

        $("#saveProject").click(function (event) {

          initFormValidationMsg();
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "";
          var project = new Object();
          project.name = $("#name").val();
          project.companyId = $("#listOfCompany option:selected" ).val();
          if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, project);
        });


        /* Update company data using ajax */

        $('#editCompany').click(function () {
          initializeProjectForm();
          initFormValidationMsg();
          var project = new Object();
          project = companyGb;
          companyGb=null;
          console.log(project);

          if (project == null) {
            var data = 'please select a record to perform edit operation';
            showServerSideMessage(data, "", 0, "Message");
          } else {
            $("#updateProject").show();
            $("#saveProject").hide();
            $("#id").val(project.id);
            $("#name").val(project.name);
            $("#version").val(project.version);
            $('#listOfCompany option:contains("' + project.company.name + '")').prop('selected', 'selected');
            window.location.href = "#projectForm";
          }


        });
        var table = $('#projectTable').DataTable();

        $("#updateProject").click(function (event) {
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "Message";
          var project = new Object();
          project.id = $("#id").val();
          project.version = $("#version").val();
          project.name = $("#name").val();
          project.companyId = $("#listOfCompany option:selected" ).val();
          if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, project);

        });


        /* Delete company data using ajax */

        $("#deleteProject").click(function (event) {
          var project = new Object();
          project.id = companyGb.id;
          companyGb=null;
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "Message";
          if (project == null) {
            var data = 'please select a record to perform delete operation';
            showServerSideMessage(data, "", 0, "Message");
          } else {
            $.dialogbox({
              type: 'msg',
              title: 'Confirm Title',
              content: 'Are You Sure,want to delete this record?',
              closeBtn: true,
              btn: ['Confirm', 'Cancel'],
              call: [
                function () {
                  $.dialogbox.close();
                  callAjaxForDeleteOperation(part1, part2, icn, msg, project);

                },
                function () {
                  $.dialogbox.close();
                }
              ]
            });


            window.location.href = "#viewTableData";
          }

        });


        /* DataTable select value send to global var */

        $('#projectTable tbody').on('click', 'tr', function () {
          companyGb = table.row(this).data();
        });


        /* Initialize html form value  based on reset button*/

        $('#resetProject').on('click', function () {
          companyGb=null;
          initializeProjectForm();
          initFormValidationMsg();
          $('#saveProject').show();
          $('#updateProject').hide();
        });


        /* load table data on click refresh button*/

        $('#refreshProject').on('click', function () {
          companyGb=null;
          table.ajax.url( 'http://localhost:8080/projectList' ).load();
        });




        /*  Ajax call for delete operation */

        function callAjaxForDeleteOperation(part1, part2, icn, msg, project) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': "http://localhost:8080/project/delete",
            'data': JSON.stringify(project),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                msg = "";
                part1 = d.successMsg;
                deleteDataRow(project.id,"projectTable");
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


        /*  Ajax call for edit operation */

        function callAjaxForEditOperation(part1, part2, icn, msg, project) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': "http://localhost:8080/project/update",
            'data': JSON.stringify(project),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                part1 = d.successMsg;
                initializeProjectForm();
                window.location.href = "#viewTableData";
                $("#updateProject").hide();
                $("#saveProject").show();
                showServerSideMessage(part1, part2, icn, msg);
              }
              if (d.validationError) {
                icn = 0;
                msg = "";
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
          company=null;

        }


        /*  Ajax call for save operation */

        function callAjaxForAddOperation(part1, part2, icn, msg, project) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': "http://localhost:8080/project/save",
            'data': JSON.stringify(project),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                console.log(d);
                icn = 1;
                msg = "";
                part1 = d.successMsg;
                initializeProjectForm();
                setNewDataTableValue(d.project,table);
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

        function initializeProjectForm() {
          $("#id").val("");
          $("#version").val("");
          $("#name").val("");
          $("#listOfCompany select").val("0");
        }



        /* html form Validation */

        function formValidation() {
          var isValid = true;
          var name = $("#name").val();
          var companyId = $("#listOfCompany option:selected" ).val();
          if (name == null || name.trim().length == 0) {
            $("#nameValidation").text("Name is required");
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
          $("#companyNameValidation").text("");
        }


        /* move to add new company div*/

        $('#moveToAdd').on('click', function () {
          companyGb=null;
          $("#updateProject").hide();
          $("#saveProject").show();
          window.location.href = "#projectForm";
        });


        /* Set new created company value to DataTable*/

        function setNewDataTableValue(project,table) {
          table.row.add({
            "id": project.id,
            "name": project.name,
            "version": project.version,
            "companyName": project.company.name,
            "createdBy": project.createdBy,
            "createdOn": project.createdOn,
            "updatedBy": project.updatedBy,
            "updatedOn": project.updatedOn
          }).draw();

        };




      });
      //

    </script>
  </div>


</section>

<%@ include file="footer.jsp" %>