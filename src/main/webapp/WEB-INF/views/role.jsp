<%@ include file="header.jsp" %>

<section class="content">
  <div class="container-fluid">

    <%--start of table div--%>

    <div id="viewTableData" >

    </div>


    <%--<div class="row clearfix">--%>
      <%--<div class="col-xs-10 col-xs-offset-1 card">--%>
        <%--<br/><br/>--%>
        <%--<table id="subCategoryTable" class="display nowrap" cellspacing="0" width="100%">--%>
          <%--<thead>--%>
          <%--<tr>--%>
            <%--<th style="width: 15px"></th>--%>
            <%--<th style="width: 2000px">Name</th>--%>
            <%--<th style="width: 3000px">Description</th>--%>
            <%--<th>Category Name</th>--%>
          <%--</tr>--%>
          <%--</thead>--%>
        <%--</table>--%>
        <%--<br/>--%>
        <%--<button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img--%>
                <%--src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New--%>
        <%--</button>--%>
        <%--&nbsp;--%>
        <%--&nbsp;--%>

        <%--<button type="button" class="btn bg-grey waves-war" id="editSubCategory" value="1" title="Edit"><img--%>
                <%--src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit--%>
        <%--</button>--%>
        <%--&nbsp;--%>
        <%--&nbsp;--%>
        <%--<button type="button" class="btn bg-grey waves-war" id="deleteSubCategory" value="1" title="Delete"><img--%>
                <%--src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete--%>

        <%--</button>--%>
        <%--&nbsp;--%>
        <%--&nbsp;--%>
        <%--<button type="button" class="btn bg-grey waves-war" id="refreshSubCategory" value="1" title="Delete"><img--%>
                <%--src="resources/images/refresh.png" width="16" height="16" border="0">&nbsp;Refresh--%>
        <%--</button>--%>
        <%--&nbsp;<br/><br/>--%>
        <%--&nbsp;<br/><br/>--%>
      <%--</div>--%>

      <%--<br/>--%>
    <%--</div>--%>

    <%--end of table div--%>


    <br/><br/><br/>


    <%--start of save/update modal--%>


    <div class="row clearfix" id="subCategoryForm">
      <div class="col-xs-8 col-xs-offset-2">
        <div class="card">
          <div class="header" style="background-color:#a5a5a5">
            <h2><strong>&nbsp;</strong></h2>
          </div>
          <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
            <form class="form-horizontal">
              <fieldset>

                <!-- Form Name -->
                <legend><strong>Role Setting</strong></legend>

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
                  <label class="col-md-4 control-label" for="description">Description :</label>

                  <div class="col-md-6">
                                        <textarea id="description" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="address" rows="4" cols="16"></textarea>
                    <label id="descriptionValidation" style="color:red; font-size: 11px;"
                           class="form-control"></label>
                  </div>
                </div>


                <!-- Multiple Checkboxes (inline) -->
                <div class="form-group">
                  <label class="col-md-4 control-label" for="rights">Rights</label>
                  <div class="col-md-4">
                    <label class="checkbox-inline" for="readRights">
                      <input type="checkbox" name="rights" id="readRights" value="READ_PREVILEGE">
                      Read
                    </label>
                    <label class="checkbox-inline" for="writeRights">
                      <input type="checkbox" name="rights" id="writeRights" value="WRITE_PREVILEGE">
                      Write
                    </label>
                    <label class="checkbox-inline" for="editRights">
                      <input type="checkbox" name="rights" id="editRights" value="EDIT_PREVILEGE">
                      Edit
                    </label>
                    <label class="checkbox-inline" for="deleteRights">
                      <input type="checkbox" name="rights" id="deleteRights" value="DELETE_PREVILEGE">
                      Delete
                    </label>
                  </div>
                </div>


                <!-- Button -->
                <div class="form-group">
                  <label class="col-md-4 control-label" for="saveRole"></label>

                  <div class="col-md-4">
                    <button id="saveRole" name="saveRole" class="btn btn-primary"
                            type="button">Save
                    </button>
                    <button id="updateRole" name="updateRole" class="btn btn-primary"
                            type="button">Update
                    </button>
                    <button id="resetRole" name="resetRole" class="btn bg-grey"
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
//        initFormValidationMsg();
//        initializeSubCategoryForm();
        $("saveRole").show();
        $("#updateRole").hide();
        var companyGb;


        /* Load subcategory data to select box data using ajax */

//        $('#subCategoryTable').DataTable({
//          "sAjaxSource": "http://localhost:8080/subcategory/subcategoryList",
//          "sAjaxDataProp": "",
//          "order": [[1, "asc"]],
//          'aoColumns': [
//            {
//              'sTitle': '',
//              "sClass": "checkbox-column",
//              'mData': 'id',
//              'mRender': function (id) {
//                return '<input class="getVal" style="position: static;"  type="checkbox" name="' + id + '" id="' + id + '">';
//              },
//              'sWidth': '15px',
//              'bSortable': false
//            },
//            {"mData": "name",'sWidth': '200px'},
//            {"mData": "description",'sWidth': '300px'},
//            {"mData": "productCategory.name"}
//          ],
//          'aaSorting': [[0, 'asc']],
//          "columnDefs": [
//            {
////                            "targets": [0],
////                            "visible": false,
////                            "searchable": false
//            }
//          ],
//          "cache": false,
//          "bPaginate": true,
//          "bLengthChange": true,
//          "bFilter": true,
//          "bInfo": false,
//          "bAutoWidth": true,
//          "scrollY": "400",
//          "scrollX": true
//
//        });


        /* populate Company list when page load */

        function getAllCategory() {
          $('#getAllCategory').empty();
          $.ajax({
            type: "GET",
            url: "http://localhost:8080/category/categoryList",
            success: function (data) {
              var collaboration;
              collaboration += '<option id="defaultOpt" value="0">Select Category</option>';
              $.each(data, function (i, d) {
                collaboration += "<option value=" + d.id + ">" + d.name + "</option>";
              });

              $('#listOfCategory').append(collaboration);
            },
            error: function (e) {
              console.log(e);
            }
          });
        }

//        getAllCategory();

        /* Save SubCategory data using ajax */

        $("#saveRole").click(function (event) {
//          initFormValidationMsg();
          var part1 = "";
          var part2 = "";
          var icn = 0;
          var msg = "";
          var role = new Object();
          role.name = $("#name").val();
          role.description = $("#description").val();
          role.READ_PREVILEGE = ($("#readRights").is(":checked")) ? $("#readRights").val() : "";
          role.WRITE_PREVILEGE = ($("#writeRights").is(":checked")) ? $("#writeRights").val() : "";
          role.EDIT_PREVILEGE = ($("#editRights").is(":checked")) ? $("#editRights").val() : "";
          role.DELETE_PREVILEGE = ($("#deleteRights").is(":checked")) ? $("#deleteRights").val() : "";
//          if (formValidation()) callAjaxForAddOperation(part1, part2, icn, msg, role);
          callAjaxForAddOperation(part1, part2, icn, msg, role)
        });


        /* Update SubCategory data using ajax */
//
//        $('#editSubCategory').click(function () {
//          initializeSubCategoryForm();
//          initFormValidationMsg();
//          var subCategory = new Object();
//          subCategory = companyGb;
//          companyGb = null;
//          var data = messageResource.get('subcategory.edit.validation.msg', 'configMessageForUI');
//
//          if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
//          else if (subCategory == null)showServerSideMessage(data, "", 0, "Message");
//          else {
//            $("#updateSubCategory").show();
//            $("#saveSubCategory").hide();
//            $("#id").val(subCategory.id);
//            $("#name").val(subCategory.name);
//            $("#version").val(subCategory.version);
//            $("#description").val(subCategory.description);
//            $('#listOfCategory option:contains("' + subCategory.productCategory.name + '")').prop('selected', 'selected');
//            window.location.href = "#subCategoryForm";
//          }
//        });
//        var table = $('#subCategoryTable').DataTable();
//
//        $("#updateSubCategory").click(function (event) {
//          var part1 = "";
//          var part2 = "";
//          var icn = 0;
//          var msg = "Message";
//          var subCategory = new Object();
//          subCategory.id = $("#id").val();
//          subCategory.version = $("#version").val();
//          subCategory.name = $("#name").val();
//          subCategory.description = $("#description").val();
//          subCategory.categoryId = $("#listOfCategory option:selected").val();
//          if (formValidation()) callAjaxForEditOperation(part1, part2, icn, msg, subCategory);
//        });


        /* Delete SubCategory data using ajax */

//        $("#deleteSubCategory").click(function (event) {
//          initializeSubCategoryForm();
//          initFormValidationMsg();
//          var subCategory = new Object();
//          subCategory = companyGb;
//          companyGb = null;
//          var part1 = "";
//          var part2 = "";
//          var icn = 0;
//          var msg = "Message";
//          var data =  messageResource.get('subcategory.delete.validation.msg', 'configMessageForUI');
//
//          if (checkForMultipleRowSelect()) showServerSideMessage(data, "", 0, "Message");
//          else if (subCategory == null)showServerSideMessage(data, "", 0, "Message");
//          else {
//            $.dialogbox({
//              type: 'msg',
//              title: 'Confirm Title',
//              content: messageResource.get('subcategory.delete.confirm.msg', 'configMessageForUI'),
//              closeBtn: true,
//              btn: ['Confirm', 'Cancel'],
//              call: [
//                function () {
//                  $.dialogbox.close();
//                  callAjaxForDeleteOperation(part1, part2, icn, msg, subCategory);
//                },
//                function () {
//                  $.dialogbox.close();
//                  uncheckedAllCheckBox();
//                }
//              ]
//            });
//            window.location.href = "#viewTableData";
//          }
//        });


        /* DataTable select value send to global var */

//        $('#subCategoryTable tbody').on('click', 'tr', function () {
//          companyGb = table.row(this).data();
//          var isChecked = $('#' + companyGb.id).is(":checked");
//          if (isChecked == false) companyGb = null;
//        });


        /* Initialize html form value  based on reset button*/

//        $('#resetSubCategory').on('click', function () {
//          companyGb = null;
//          initializeSubCategoryForm();
//          initFormValidationMsg();
//          $('#saveSubCategory').show();
//          $('#updateSubCategory').hide();
//          uncheckedAllCheckBox();
//        });


        /* load table data on click refresh button*/

//        $('#refreshSubCategory').on('click', function () {
//          initializeSubCategoryForm();
//          initFormValidationMsg();
//          companyGb = null;
//          table.ajax.url( messageResource.get('subcategory.list.load.url', 'configMessageForUI')).load();
//        });


        /*  Ajax call for delete operation */

//        function callAjaxForDeleteOperation(part1, part2, icn, msg, subcategory) {
//          $.ajax({
//            headers: {
//              'Accept': 'application/json',
//              'Content-Type': 'application/json'
//            },
//            'type': 'POST',
//            'url': messageResource.get('subcategory.delete.url', 'configMessageForUI'),
//            'data': JSON.stringify(subcategory),
//            'dataType': 'json',
//            'success': function (d) {
//              if (d.successMsg) {
//                icn = 1;
//                msg = "";
//                part1 = d.successMsg;
//                showServerSideMessage(part1, part2, icn, msg);
//                table.ajax.url(messageResource.get('subcategory.list.load.url', 'configMessageForUI')).load();
//              }
//              if (d.validationError) {
//                icn = 0;
//                msg = '<strong style="color: red">Error</strong>';
//                part2 = d.validationError;
//                uncheckedAllCheckBox();
//                showServerSideMessage(part1, part2, icn, msg);
//              }
//            },
//            'error': function (error) {
//              icn = 0;
//              msg = '<strong style="color: red">Error</strong>';
//              showServerSideMessage(part1, getErrorMessage(error), icn, msg);
//            }
//          });
//        }


        /*  Ajax call for edit operation */

//        function callAjaxForEditOperation(part1, part2, icn, msg, subcategory) {
//          $.ajax({
//            headers: {
//              'Accept': 'application/json',
//              'Content-Type': 'application/json'
//            },
//            'type': 'POST',
//            'url': messageResource.get('subcategory.edit.url', 'configMessageForUI'),
//            'data': JSON.stringify(subcategory),
//            'dataType': 'json',
//            'success': function (d) {
//              if (d.successMsg) {
//                icn = 1;
//                part1 = d.successMsg;
//                initializeSubCategoryForm();
//                window.location.href = "#viewTableData";
//                $("#updateSubCategory").hide();
//                $("#saveSubCategory").show();
//                showServerSideMessage(part1, part2, icn, msg);
//                table.ajax.url(messageResource.get('subcategory.list.load.url', 'configMessageForUI')).load();
//              }
//              if (d.validationError) {
//                icn = 0;
//                msg = "";
//                msg = '<strong style="color: red">Error</strong>';
//                part2 = d.validationError;
//                uncheckedAllCheckBox();
//                showServerSideMessage(part1, part2, icn, msg);
//              }
//            },
//            'error': function (error) {
//              icn = 0;
//              msg = '<strong style="color: red">Error</strong>';
//              showServerSideMessage(part1, getErrorMessage(error), icn, msg);
//            }
//          });
//          company = null;
//
//        }


        /*  Ajax call for save operation */

        function callAjaxForAddOperation(part1, part2, icn, msg, subcategory) {
          $.ajax({
            headers: {
              'Accept': 'application/json',
              'Content-Type': 'application/json'
            },
            'type': 'POST',
            'url': messageResource.get('role.save.url', 'configMessageForUI'),
            'data': JSON.stringify(subcategory),
            'dataType': 'json',
            'success': function (d) {
              if (d.successMsg) {
                icn = 1;
                msg = "";
                part1 = d.successMsg;
//                initializeSubCategoryForm();
                console.log(d.role);
//                setNewDataTableValue(d.role, table);
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

//        function initializeSubCategoryForm() {
//          $("#id").val("");
//          $("#version").val("");
//          $("#name").val("");
//          $("#description").val("");
//          $('#defaultOpt').val('0').prop('selected', true);
//        }


        /* html form Validation */

//        function formValidation() {
//          var isValid = true;
//          var name = $("#name").val();
//          var description = $("#description").val();
//          var categoryId = $("#listOfCategory option:selected").val();
//          if (name == null || name.trim().length == 0) {
//            $("#nameValidation").text("Name is required");
//            isValid = false;
//          }
//          if (description == null || description.trim().length == 0) {
//            $("#descriptionValidation").text("Description is required");
//            isValid = false;
//          }
//          if ((categoryId == null) || (categoryId == "0")) {
//            $("#categoryNameValidation").text("Product category name is required");
//            isValid = false;
//          }
//          return isValid;
//        }


        /* Initialize html form validation error field*/

//        function initFormValidationMsg() {
//          $("#nameValidation").text("");
//          $("#descriptionValidation").text("");
//          $("#categoryNameValidation").text("");
//        }


        /* move to add new SubCategory div*/

//        $('#moveToAdd').on('click', function () {
//          companyGb = null;
//          $("#updateSubCategory").hide();
//          $("#saveSubCategory").show();
//          uncheckedAllCheckBox();
//          initializeSubCategoryForm();
//          initFormValidationMsg();
//          window.location.href = "#subCategoryForm";
//        });


        /* Set new created SubCategory value to DataTable*/

//        function setNewDataTableValue(productSubCategory, table) {
//          table.row.add({
//            "id": productSubCategory.id,
//            "name": productSubCategory.name,
//            "version": productSubCategory.version,
//            "description": productSubCategory.description,
//            "productCategory": productSubCategory.productCategory,
//            "createdBy": productSubCategory.createdBy,
//            "createdOn": productSubCategory.createdOn,
//            "updatedBy": productSubCategory.updatedBy,
//            "updatedOn": productSubCategory.updatedOn
//          }).draw();
//
//        };


      });
      //

    </script>
  </div>


</section>

<%@ include file="footer.jsp" %>