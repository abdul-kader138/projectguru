<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">
        <div class="row clearfix">
            <div class="col-xs-8 col-xs-offset-2 card">
                <br/>

                <div><h4>User Details</h4></div>
                <hr/>
                <br/>

                <div class="table-responsive">
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
                </div>

                <a class="btn bg-grey waves-war" id="back" value="1" title="Back" href="/home"><img
                        src="resources/images/back.png" width="16" height="16" border="0">&nbsp;Back to Home
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
            var mainPath = document.origin + "/PG";
            $("#uploadedPhotoSrc").attr("src", mainPath + imageVal);
        });

    </script>
</section>


<%@ include file="footer.jsp" %>