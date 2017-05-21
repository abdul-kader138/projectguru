<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">

        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/><br/>
                <table id="example" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th>id</th>
                        <th>version</th>
                        <th>Name</th>
                        <th>Address</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button class="btn btn-danger waves-war" id="deleteCompany" type="button">SUBMIT
                </button>
            </div>
            <br/>
        </div>
        <br/><br/><br/>

        <div class="row clearfix">
            <%--<div class="col-xs-6 col-xs-offset-3">--%>
            <%--<div class="alert alert-info" id="successMessage">--%>
            <%--&lt;%&ndash;<strong>Record has been inserted successfully...</strong>&ndash;%&gt;--%>
            <%--&nbsp;--%>
            <%--&nbsp;--%>
            <%--</div>--%>
            <%--</div>--%>
            <div class="col-xs-6 col-xs-offset-3">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>Company Setup</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form id="form_validation" method="POST">
                            <div class="form-group form-float">
                                <div class="form-line">
                                    <input type="text" class="form-control" id="name" name="name" required>
                                    <label class="form-label">Name</label>
                                </div>
                            </div>
                            <div class="form-group form-float">
                                <div class="form-line">
                                        <textarea name="description" cols="20" rows="10" id="address"
                                                  class="form-control no-resize"
                                                  required></textarea>
                                    <label class="form-label">Address</label>
                                </div>
                            </div>
                            <button class="btn btn-primary waves-effect" data-type="autoclose-timer" id="saveCompany"
                                    type="button">SUBMIT
                            </button>
                        </form>
                    </div>
                </div>
            </div>
        </div>


        <script type="text/javascript">
            //            var table = $('#example').DataTable();
            //            console.log(table);
            $(document).ready(function () {

                //  var table1 = $('#example').DataTable();

//                $('#example tbody').on('click', 'tr', function () {
//                    var idx = table.row().index();
                //    console.log(table1);
//                });

                $('#example').DataTable({
                    "sAjaxSource": "http://localhost:8080/companyList",
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    "aoColumns": [
                        {"mData": "id"},
                        {"mData": "version"},
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
                    "scrollY": 400,
                    "scrollX": true

                });


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
                        'url': "http://localhost:8080/save",
                        'data': JSON.stringify(company),
                        'dataType': 'json',
                        'success': function (d) {
                            console.log(d);
                            console.log(d.successMsg)
                        }
                    });
                });

                var table = $('#example').DataTable();
                console.log(table);

                $('#deleteCompany').click(function () {
                    alert(table.row('.selected'));
                    alert('test');
                    var idx = table.cell('.selected', 0).index();
//                    console.log(table.row('.selected',0).index());
                    console.log(idx);
                    var data = table.row( idx.row ).data();
                    console.log(data);
                });
            });


        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>