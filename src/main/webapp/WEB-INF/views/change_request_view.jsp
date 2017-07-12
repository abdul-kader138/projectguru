<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ include file="header.jsp" %>
<section class="content">
    <div id="viewTableData"></div>
    <div class="row clearfix">
        <div class="col-xs-8 col-xs-offset-2 card">
            <br/>

            <div><h4>Request Details</h4></div>
            <hr/>
            <br/>

            <div class="table-responsive">
                <table class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <tbody>
                    <tr class="label_color">
                        <td><b>Company Name</b></td>
                        <td>${changeRequest.category.company.name}</td>
                    </tr>
                    <tr class="label_color">
                        <td><b>Product Name</b></td>
                        <td>${changeRequest.category.product.name}</td>
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
                        <td>
                            <c:forEach items="${changeRequest.description}" var="description">
                                <c:set var="desVal" value="${fn:substring(description, 0, 40)}"/>
                                <c:set var="desVal1" value="${fn:substring(description, 40, 80)}"/>
                                <c:set var="desVal2" value="${fn:substring(description, 80, 120)}"/>
                                ${desVal}</br>${desVal1}</br>${desVal2}
                            </c:forEach>
                        </td>
                    </tr>
                    <c:if test="${changeRequest.declineCause != null}">
                        <c:set var="declineVal" value="${fn:substring(changeRequest.declineCause, 0, 40)}"/>
                        <c:set var="declineVal1" value="${fn:substring(changeRequest.declineCause, 40, 80)}"/>
                        <c:set var="declineVal2" value="${fn:substring(changeRequest.declineCause, 80, 120)}"/>
                        <tr class="label_color">
                            <td><b>Decline Cause</b></td>
                            <td>${declineVal}</br>${declineVal1}</br>${declineVal2}</td>
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
                    <c:if test="${changeRequest.deployedOn != null}">
                        <tr class="label_color">
                            <td><b>Deployed On</b></td>
                            <td>${changeRequest.deployedOn}</td>
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
            </div>

            <a class="btn bg-grey waves-war" id="back" value="1" title="Back" href="/home"><img
                    src="resources/images/back.png" width="16" height="16" border="0">&nbsp;Back to Home
            </a>
            <br/>
            <br/>
        </div>
    </div>
    <script>
        $(document).ready(function () {
            /* set nav bar color */
            changeNavColor();
            var colorName = localStorage.colorName;
            setNavColor(colorName);
        });

    </script>
</section>


<%@ include file="footer.jsp" %>