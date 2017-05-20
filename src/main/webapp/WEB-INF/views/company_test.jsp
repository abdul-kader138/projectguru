<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">

        <div class="row clearfix">

            <table id="example" class="display" cellspacing="0" width="100%">
                <thead>
                <tr>
                    <th>id</th>
                    <th>Name</th>
                </tr>
                </thead>
                <tfoot>
                <tr>
                    <th>id</th>
                    <th>Name</th>
                </tr>
                </tfoot>
            </table>
        </div>

            <div class="row clearfix">
                <%--<div class="col-lg-12 col-md-12 col-sm-12 col-xs-12">--%>
                <div class="col-xs-6 col-xs-offset-3">
                    <div class="card">
                        <div class="header">
                            <h2>Company Setup</h2>
                        </div>
                        <div class="body">
                            <form id="form_validation" method="POST">
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <input type="text" class="form-control" name="name" required>
                                        <label class="form-label">Name</label>
                                    </div>
                                </div>
                                <div class="form-group form-float">
                                    <div class="form-line">
                                        <textarea name="description" cols="30" rows="5" class="form-control no-resize"
                                                  required></textarea>
                                        <label class="form-label">Address</label>
                                    </div>
                                </div>
                                <button class="btn btn-primary waves-effect" type="submit">SUBMIT</button>
                            </form>
                        </div>
                    </div>
                </div>
            </div>




        <script type="text/javascript">


            $(document).ready(function () {

                var data;
                $('#example').DataTable({
                    "sAjaxSource": "http://localhost:8080/companyList",
                    "sAjaxDataProp": "",
                    "order": [[0, "asc"]],
                    "aoColumns": [
                        {"mData": "id"},
                        {"mData": "name"}
                    ],
                    "cache": false,
                    "bPaginate": true,
                    "bLengthChange": true,
                    "bFilter": true,
                    "bInfo": true,
                    "bAutoWidth": true,
                    "bScrollY": true

                });
            });


        </script>
    </div>


</section>

<%@ include file="footer.jsp" %>