<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">

            <div style="margin-left:20px" class="row clearfix">
                <table id="jqGrid"></table>
                <div id="jqGridPager"></div>


                <br/><br/>

                <input class="btn btn-default" type="button" value="Select row  with ID 1014" onclick="selectRow()" />&nbsp;
                <input class="btn btn-default" type="button" value="Get Selected Row" onclick="getSelectedRow()" />

                <br/><br/>
            </div>


        <script type="text/javascript">

            $(document).ready(function () {
                $("#jqGrid").jqGrid({
                    url: 'http://localhost:8080/companyList',
                    datatype: "json",
                    colModel: [
                        { label: 'ID', name: 'id', width: 45, key: true },
                        { label: 'Company Name', name: 'name', width: 75 },
                    ],
                    loadonce: true,
                    viewrecords: true,
                    width: 780,
                    height: 200,
                    rowNum: 20,
                    rowList : [20,30,50],
                    pager: "#jqGridPager"
                });
            });

            function getSelectedRow() {
                var grid = $("#jqGrid");
                var rowKey = grid.jqGrid('getGridParam',"selrow");

                if (rowKey)
                    alert("Selected row primary key is: " + rowKey);
                else
                    alert("No rows are selected");
            }


            function selectRow() {
                jQuery('#jqGrid').jqGrid('setSelection','1014');
            }

        </script>
        </div>


</section>

<%@ include file="footer.jsp" %>