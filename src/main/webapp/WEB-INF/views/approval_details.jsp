<%@ include file="header.jsp" %>

<section class="content">
    <div class="container-fluid">

        <%--start of table div--%>

        <div id="viewTableData"></div>
        <div class="row clearfix">
            <div class="col-xs-10 col-xs-offset-1 card">
                <br/>

                <div><h4>Category List</h4></div>
                <hr/>
                <br/><br/>
                <table id="approveTable" class="display nowrap" cellspacing="0" width="100%">
                    <thead>
                    <tr>
                        <th width="15px">id</th>
                        <th width="100px">Name</th>
                        <th width="200px">Description</th>
                    </tr>
                    </thead>
                </table>
                <br/>
                <button type="button" class="btn bg-grey waves-war" id="moveToAdd" value="1" title="Edit"><img
                        src="resources/images/add.png" width="16" height="16" border="0">&nbsp;Add New
                </button>
                &nbsp;
                &nbsp;

                <button type="button" class="btn bg-grey waves-war" id="editCategory" value="1" title="Edit"><img
                        src="resources/images/edit.gif" width="16" height="16" border="0">&nbsp;Edit
                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="deleteCategory" value="1" title="Delete"><img
                        src="resources/images/delete.gif" width="16" height="16" border="0">&nbsp;Delete

                </button>
                &nbsp;
                &nbsp;
                <button type="button" class="btn bg-grey waves-war" id="refreshCategory" value="1" title="Refresh"><img
                        src="resources/images/refresh.png" width="16" height="16" border="0">&nbsp;Refresh
                </button>
                &nbsp;<br/><br/>
                &nbsp;<br/><br/>
            </div>

            <br/>
        </div>

        <%--end of table div--%>


        <br/><br/><br/>


    </div>

    <script>
        $(document).ready(function () {

            var loading = $.loading();
            var companyGb;


            /* populate Category list when page load */

            $('#approveTable').DataTable({
                "sAjaxSource": "http://localhost:8080/change_request/change_requestList",
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
                    {"mData": "name", 'sWidth': '150px'},
                    {"mData": "description", 'sWidth': '250px'}
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
        });

    </script>

</section>

<%@ include file="footer.jsp" %>
