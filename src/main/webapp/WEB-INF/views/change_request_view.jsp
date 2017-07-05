<%@ include file="header.jsp" %>
<section class="content">
    <div id="viewTableData"></div>
    <div class="row clearfix">
        <div class="col-xs-8 col-xs-offset-2 card">
            <br/>
            <div><h4>Request Details</h4></div>
            <hr/>
            <br/>
            <table class="display nowrap table table-bordered" cellspacing="0" width="100%">
                <tbody>
                <tr class="label_color">
                    <td><b>Company Name</b></td>
                    <td>${changeRequest.company.name}</td>
                </tr>
                <tr class="label_color">
                    <td><b>Product Name</b></td>
                    <td>${changeRequest.product.name}</td>
                </tr>
                <tr class="label_color">
                    <td><b>Category Name</b></td>
                    <td>${changeRequest.category.name}</td>
                </tr>
                <tr class="label_color">
                    <td><b>Name</b></td>
                    <td>${changeRequest.name}</td>
                </tr>
                <tr class="label_color">
                    <td><b>Description</b></td>
                    <td>${changeRequest.description}</td>
                </tr>
                <c:if test="${changeRequest.declineCause != null}">
                    <tr class="label_color">
                        <td><b>Decline Cause</b></td>
                        <td>${changeRequest.declineCause}</td>
                    </tr>
                </c:if>

                <c:if test="${changeRequest.wipStatus != null}">
                    <tr class="label_color">
                        <td><b>Status(Waiting For)</b></td>
                        <td>${changeRequest.wipStatus}</td>
                    </tr>
                </c:if>

                <c:if test="${changeRequest.status != null}">
                    <tr class="label_color">
                        <td><b>Status</b></td>
                        <td>${changeRequest.status}</td>
                    </tr>
                </c:if>
                <c:if test="${changeRequest.requiredDay != null}">
                    <tr class="label_color">
                        <td><b>Day Require for change</b></td>
                        <td>${changeRequest.requiredDay}</td>
                    </tr>
                </c:if>
                <c:if test="${changeRequest.deliverDate != null}">
                    <tr class="label_color">
                        <td><b>Delivery Date</b></td>
                        <td>${changeRequest.deliverDate}</td>
                    </tr>
                </c:if>
                <c:if test="${changeRequest.createdBy != null}">
                    <tr class="label_color">
                        <td><b>Created By</b></td>
                        <td>${changeRequest.createdBy}</td>
                    </tr>
                </c:if>
                </tbody>
            </table>

            <a class="btn bg-grey waves-war" id="back" value="1" title="Back" href="/home"><img
                    src="resources/images/back.png" width="16" height="16" border="0">&nbsp;Back to Home
            </a>
            <br/>
            <br/>
        </div>
    </div>
    <script>
        $(document).ready(function () {
        });

    </script>
</section>


<%@ include file="footer.jsp" %>