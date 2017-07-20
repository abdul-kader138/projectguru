<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">
        <div class="row clearfix">
            <div class="col-xs-12 card">
                <br/>

                <div><span class="glyphicon glyphicon-user"></span><b style="font-size: 20px"> &nbsp;User Details</b></div>
                <hr/>
                <br/>

                <div class="table-responsive">
                    <table class="display nowrap table table-striped" cellspacing="0" width="100%">
                        <tbody>
                        <c:if test="${user.company.name != null}">
                            <tr class="label_color">
                                <td><b>Company Name</b></td>
                                <td>${user.company.name}</td>
                            </tr>
                        </c:if>
                        <c:if test="${user.name != null}">
                            <tr class="label_color">
                                <td><b>Name</b></td>
                                <td>${user.name}</td>
                            </tr>
                        </c:if>
                        <c:if test="${user.email != null}">
                            <tr class="label_color">
                                <td><b>Email</b></td>
                                <td>${user.email}</td>
                            </tr>
                        </c:if>
                        <c:if test="${user.phone != null}">
                            <tr class="label_color">
                                <td><b>Phone</b></td>
                                <td>${user.phone}</td>
                            </tr>
                        </c:if>
                        <c:if test="${user.designation != null}">
                            <tr class="label_color">
                                <td><b>Designation</b></td>
                                <td>${user.designation}</td>
                            </tr>
                        </c:if>
                        <c:if test="${user.role != null}">
                            <tr class="label_color">
                                <td><b>Role</b></td>
                                <td>${user.role}</td>
                            </tr>
                        </c:if>
                        <c:if test="${user.imagePath != null}">
                            <tr class="label_color">
                                <td><b>Photo</b></td>
                                <td><img id="uploadedPhotoSrc"
                                         src="${user.imagePath}" width="70" height="70" border="0"></td>
                            </tr>
                        </c:if>
                        </tbody>
                    </table>
                </div>

                <a class="btn bg-grey waves-war" id="back" value="1" title="Back" href="/home"><img
                        src="resources/images/back.png" width="16" height="16" border="0">&nbsp;Home
                </a>
                <br/>
                <br/>
            </div>
        </div>
    </div>
    </div>

    <script>
        $(document).ready(function () {

            /* set nav bar color */
            changeNavColor();
            var colorName = localStorage.colorName;
            setNavColor(colorName);


            /* set user image path */
            var imageVal = $("#uploadedPhotoSrc").attr("src");
            var mainPath = window.location.origin + "/pg";
            $("#uploadedPhotoSrc").attr("src", mainPath + imageVal);
        });

    </script>
</section>


<%@ include file="footer.jsp" %>