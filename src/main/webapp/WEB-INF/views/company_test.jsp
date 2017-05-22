<%@ include file="header.jsp" %>
<style>

    #companyTable tr:hover, tr.selected {
        background-color: #a2aec7;
    }

    #companyTable table tr.row_selected td {
        background-color: #a2aec7;
    }

    #companyTable table tr.selected {
        background-color: black;
        color: yellow;
    }

</style>
<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/><br/>
                <table id="companyTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>id</th>
                        <th>Name</th>
                        <th>Address</th>
                    </tr>
                    </thead>
                </table>
                <br/>

                <button type="button" class="btn bg-grey waves-war" id="editCompany" value="1" title="Edit"> Edit<img
                        src="resources/images/edit.gif" width="16" height="16" border="0">
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteCompany" value="1" title="Delete">
                    Delete<img
                        src="resources/images/delete.gif" width="16" height="16" border="0">
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshCompany" value="1" title="Refresh">
                    Refresh<img
                        src="resources/images/download.png" width="16" height="16" border="0">
                </button>
                &nbsp;<br/><br/>
            </div>
            <br/>
        </div>

        <%--end of table div--%>


        <br/><br/><br/>


        <%--start of save/update modal--%>


        <div class="row clearfix" id="companyForm">
            <div class="col-xs-6 col-xs-offset-3">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>Company Setup</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form id="form_validation" method="POST">
                            <div class="form-group form-float">
                                <div class="form-line">
                                    <input type="hidden" class="form-control" id="id" name="id" required>
                                    <input type="hidden" class="form-control" id="version" name="version" required>
                                    <label class="form-label">Name</label>
                                    <input type="text" class="form-control" id="name" name="name" required>
                                    <span>error</span>

                                </div>
                            </div>
                            <div class="form-group form-float">
                                <div class="form-line">
                                    <label class="form-label">Address</label>
                                        <textarea name="description" cols="20" rows="10" id="address"
                                                  class="form-control no-resize"
                                                  required></textarea>

                                </div>
                            </div>
                            <button class="btn btn-primary waves-effect" data-type="autoclose-timer" id="saveCompany"
                                    type="button">Save
                            </button>
                            <button class="btn btn-primary waves-effect" data-type="autoclose-timer" id="updateCompany"
                                    type="button">Update
                            </button>

                        </form>
                    </div>
                </div>

            </div>
        </div>
        <%--start of save/update modal--%>


        <script type="text/javascript">
            $(document).ready(function () {

                initializeCompanyForm();
                $("#updateCompany").hide();
                var company;



                // populate Company list when page load
                $('#companyTable').DataTable({
                    "sAjaxSource": "http://localhost:8080/companyList",
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    "aoColumns": [
                        {"mData": "id"},
                        {"mData": "name"},
                        {"mData": "address"}
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



                // company Save
                $("#saveCompany").click(function (event) {
                    var company = new Object();
                    company.name = $("#name").val();
                    company.address = $("#address").val();
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': "http://localhost:8080/company/save",
                        'data': JSON.stringify(company),
                        'dataType': 'json',
                        'success': function (d) {
                            setMessage(d);
                            initializeCompanyForm();
                            window.location.href = "#viewTableData";
                        }, 'error': function (error) {
                            alert(getErrorMessage(error));
                        }
                    });
                });


                var table = $('#companyTable').DataTable();

                //company update
                $('#editCompany').click(function () {
                    initializeCompanyForm();
                    var newCompany = new Object();
                    var newCompany = company;

                    if (newCompany == null) {
                        alert('please select a record for corresponding operation');
                    } else {
                        $("#updateCompany").show();
                        $("#saveCompany").hide();
                        $("#id").val(newCompany.id);
                        $("#name").val(newCompany.name);
                        $("#name").html(newCompany.name);
                        $("#version").val(newCompany.version);
                        $("#address").val(newCompany.address);
                        $("#address").html(newCompany.address);
                        window.location.href = "#companyForm";
                    }

                });


                $("#updateCompany").click(function (event) {
                    var company = new Object();
                    company.id = $("#id").val();
                    company.version = $("#version").val();
                    company.name = $("#name").val();
                    company.address = $("#address").val();
                    $.ajax({
                        headers: {
                            'Accept': 'application/json',
                            'Content-Type': 'application/json'
                        },
                        'type': 'POST',
                        'url': "http://localhost:8080/company/update",
                        'data': JSON.stringify(company),
                        'dataType': 'json',
                        'success': function (d) {
                            initializeCompanyForm();
                            setMessage(d);
                            window.location.href = "#viewTableData";

                        }, 'error': function (error) {
                            initializeCompanyForm();
                            alert(getErrorMessage(error));
                            window.location.href = "#viewTableData";
                        }
                    });
                    $("#updateCompany").hide();
                    $("#saveCompany").show();

                });

                /////


                // Delete Company

                $("#deleteCompany").click(function (event) {
                    var newCompany = new Object();
                    newCompany = company;
                    if (newCompany == null) {
                        alert('please select a record for corresponding operation');
                    } else {
                        $.ajax({
                            headers: {
                                'Accept': 'application/json',
                                'Content-Type': 'application/json'
                            },
                            'type': 'POST',
                            'url': "http://localhost:8080/company/delete",
                            'data': JSON.stringify(newCompany),
                            'dataType': 'json',
                            'success': function (d) {
                                initializeCompanyForm();
                                setMessage(d);
                                window.location.href = "#viewTableData";
                            },
                            'error': function (error) {
                                alert(getErrorMessage(error));
                            }
                        });
                    }
                });

                ////


                // Refresh Company

                $("#refreshCompany").click(function (event) {
                    $('#simple-dialogBox').dialogBox({
                        content: 'dialog content text,image,html file'
                    });

                    alert("test");
                });


                // clicking on table row, save data to global var company
                $('#companyTable tbody').on('click', 'tr', function () {
                    company = table.row(this).data();
                    console.log('from click button' + company.id);
                });


                ////


                // initialize form value

                function initializeCompanyForm() {
                    $("#name").val("");
                    $("#address").val("");
                }

                ////



            });


        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>