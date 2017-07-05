<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">
        <div class="row clearfix">
            <div class="col-xs-8 col-xs-offset-2 card">
                <br/>
                <div><h4>User Details</h4></div>
                <hr/>
                <br/>
                <table class="display nowrap table table-bordered" cellspacing="0" width="100%">
                    <tbody>
                    <tr class="label_color">
                        <td><b>Company Name</b></td>
                        <td>${user.company.name}</td>
                    </tr>

                    <tr class="label_color">
                        <td><b>Name</b></td>
                        <td>${user.name}</td>
                    </tr>
                    <tr class="label_color">
                        <td><b>Email</b></td>
                        <td>${user.email}</td>
                    </tr>
                        <tr class="label_color">
                            <td><b>Phone</b></td>
                            <td>${user.phone}</td>
                        </tr>

                        <tr class="label_color">
                            <td><b>Designation</b></td>
                            <td>${user.designation}</td>
                        </tr>

                        <tr class="label_color">
                            <td><b>Role</b></td>
                            <td>${user.role}</td>
                        </tr>
                        <tr class="label_color">
                            <td><b>Photo</b></td>
                            <td><img id="uploadedPhotoSrc"
                                     src="${user.imagePath}" width="70" height="70" border="0"></td>
                        </tr>
                    </tbody>
                </table>

                <a class="btn bg-grey waves-war" id="back" value="1" title="Back" href="/home"><img
                        src="resources/images/back.png" width="16" height="16" border="0">&nbsp;Back to Home
                </a>
                <br/>
                <br/>
            </div>
        </div>




        <%--<div class="row clearfix" id="userForm">--%>
            <%--<div class="col-xs-8 col-xs-offset-2">--%>
                <%--<div class="card">--%>
                    <%--<div class="header" style="background-color:#a5a5a5">--%>
                        <%--<h2><strong>&nbsp;</strong></h2>--%>
                    <%--</div>--%>
                    <%--<div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">--%>
                        <%--<form class="form-horizontal" id="userDetails">--%>
                            <%--<fieldset>--%>

                                <%--<!-- Form Name -->--%>
                                <%--<legend><strong>Personal Info</strong></legend>--%>

                                <%--<!-- Select input-->--%>
                                <%--<div class="form-group">--%>
                                    <%--<label class="col-md-4 control-label" for="company">Company Name:</label>--%>

                                    <%--<div class="col-md-4">--%>
                                        <%--<label id="company" class="form-control">${user.company.name}</label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--<!-- Text input-->--%>
                                <%--<div class="form-group">--%>
                                    <%--<label class="col-md-4 control-label" for="name">Name :</label>--%>

                                    <%--<div class="col-md-6">--%>
                                        <%--<label id="name" class="form-control">${user.name}</label>--%>

                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--<!-- Text input-->--%>
                                <%--<div class="form-group" id="emailGroup">--%>
                                    <%--<label class="col-md-4 control-label" for="email">Email :</label>--%>

                                    <%--<div class="col-md-6">--%>

                                        <%--<label id="email" class="form-control">${user.email}</label>--%>

                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--<!-- Text input-->--%>
                                <%--<div class="form-group">--%>
                                    <%--<label class="col-md-4 control-label" for="phone">Phone :</label>--%>

                                    <%--<div class="col-md-6">--%>

                                        <%--<label id="phone" class="form-control">${user.phone}</label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--<!-- Text input-->--%>
                                <%--<div class="form-group">--%>
                                    <%--<label class="col-md-4 control-label" for="designation">Designation :</label>--%>

                                    <%--<div class="col-md-6">--%>
                                        <%--<label id="designation" class="form-control">${user.designation}</label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--&lt;%&ndash;Select Box&ndash;%&gt;--%>
                                <%--<div class="form-group">--%>
                                    <%--<label class="col-md-4 control-label" for="roleNameValidation">Role:</label>--%>

                                    <%--<div class="col-md-4">--%>
                                        <%--<label id="roleNameValidation" class="form-control">${user.role}</label>--%>
                                    <%--</div>--%>
                                <%--</div>--%>

                                <%--&lt;%&ndash;logo show&ndash;%&gt;--%>
                                <%--<div class="form-group" id="uploadedPhoto">--%>
                                    <%--<label class="col-md-4 control-label" for="uploadedPhoto">Photo:</label>--%>

                                    <%--<div class="col-md-6">--%>
                                        <%--<img id="uploadedPhotoSrc"--%>
                                             <%--src="${user.imagePath}" width="70" height="70" border="0">--%>
                                    <%--</div>--%>
                                <%--</div>--%>

                            <%--</fieldset>--%>
                        <%--</form>--%>

                    <%--</div>--%>
                <%--</div>--%>

            <%--</div>--%>
        <%--</div>--%>
    </div>

    <script>
        $(document).ready(function () {
            var imageVal = $("#uploadedPhotoSrc").attr("src");
            var mainPath = document.origin + "/PG";
            $("#uploadedPhotoSrc").attr("src", mainPath + imageVal);
        });

    </script>
</section>


<%@ include file="footer.jsp" %>