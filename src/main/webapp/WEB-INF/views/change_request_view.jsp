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
                <legend><strong>Request Info</strong></legend>

                <!-- label-->
                <div class="form-group label_color">
                  <label class="col-md-4 control-label" for="company">Company Name:</label>

                  <div class="col-md-4">
                    <label id="company" class="form-control label_color">${changeRequest.company.name}</label>
                  </div>
                </div>


                <!-- label-->
                <div class="form-group label_color">
                  <label class="col-md-4 control-label" for="company">Product Name:</label>

                  <div class="col-md-4">
                    <label id="product" class="form-control label_color">${changeRequest.product.name}</label>
                  </div>
                </div>

                <!-- label-->
                <div class="form-group label_color">
                  <label class="col-md-4 control-label" for="company">Category Name:</label>

                  <div class="col-md-4">
                    <label id="category" class="form-control label_color">${changeRequest.category.name}</label>
                  </div>
                </div>
                <!-- label-->
                <div class="form-group label_color">
                  <label class="col-md-4 control-label" for="name">Name :</label>
                  <div class="col-md-6 label_color">
                    <label id="name" class="form-control label_color">${changeRequest.name}</label>
                  </div>
                </div>

                <!-- label-->
                <div class="form-group label_color">
                  <label class="col-md-4 control-label" for="name">Description :</label>
                  <div class="col-md-6 label_color">
                    <label id="name" class="form-control" style="color:#000000;">${changeRequest.description}</label>
                  </div>
                </div>
              </fieldset>
            </form>
            <br/>
            <br/>

          </div>
        </div>

      </div>
    </div>
  </div>

  <script>
    $(document).ready(function () {
//      var imageVal = $("#uploadedPhotoSrc").attr("src");
//      var mainPath = document.origin + "/PG";
//      $("#uploadedPhotoSrc").attr("src", mainPath + imageVal);
    });

  </script>
</section>


<%@ include file="footer.jsp" %>