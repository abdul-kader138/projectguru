<%@ include file="header.jsp" %>
<section class="content">
    <div class="container-fluid">
        <div class="row clearfix" id="userForm">
            <div class="col-xs-8 col-xs-offset-2">
                <div class="card">
                    <div class="header" style="background-color:#a5a5a5">
                        <h2><strong>&nbsp;</strong></h2>
                    </div>
                    <div class="body" style="border:solid; border-width: 1px; border-color:#a5a5a5;">
                        <form class="form-horizontal" id="userDetails">
                            <fieldset>

                                <!-- Form Name -->
                                <legend><strong>Decline Request</strong></legend>

                                <!-- Select input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="company">Request Name:</label>

                                    <div class="col-md-4">
                                        <label id="company" class="form-control">${request.name}</label>
                                    </div>
                                </div>

                                <!-- Text input-->
                                <div class="form-group">
                                    <label class="col-md-4 control-label" for="cause">Decline Cause :</label>

                                    <div class="col-md-6">
                                        <textarea id="cause" class="form-control input-md"
                                                  style="border-color:#808080; border-width:1px; border-style:solid;"
                                                  name="description" rows="6" cols="20"></textarea>
                                        <label id="causeValidation" style="color:red; font-size: 11px;"
                                               class="form-control"></label>

                                    </div>
                                </div>
                            </fieldset>
                        </form>

                    </div>
                </div>

            </div>
        </div>
    </div>

    <script>
        $(document).ready(function () {
        });

    </script>
</section>


<%@ include file="footer.jsp" %>