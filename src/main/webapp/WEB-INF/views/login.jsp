<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Login</title>
    <link rel="stylesheet" href="resources/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <!-- Custom CSS -->
    <link href="resources/node_modules/sb-admin-2/sb-admin-2.css" rel="stylesheet">

    <!-- Custom Fonts -->
    <link href="resources/node_modules/font-awesome/css/font-awesome.min.css" rel="stylesheet" type="text/css">

</head>
<body ng-app="loginApp">


</noscript>
<%--<div id="mainContent" class="container-fluid" ng-controller="loginController">--%>
    <%--<div class="col-xs-4 col-xs-offset-4">--%>
        <%--<hr>--%>
        <%--<form action="<c:url value='j_spring_security_check' />" method="POST" role="form">--%>
            <%--<legend>Login Information</legend>--%>
            <%--<h5 style="color: red;text-decoration-style: double">${error}</h5>--%>
            <%--<c:if test="${error == null}">--%>
                <%--<h5 ng-show="successMsg" style="color: green;text-decoration-style: double">{{successMsg}}</h5>--%>
                <%--<h5 ng-show="errorMsg" style="color: red;text-decoration-style: double">{{errorMsg}}</h5>--%>
            <%--</c:if>--%>

            <%--<br/>--%>


            <%--<div class="form-group">--%>
                <%--<label for="Name">Email</label>--%>
                <%--<input type="text" class="form-control" name="j_username" placeholder="">--%>
            <%--</div>--%>
            <%--<div class="form-group">--%>
                <%--<label for="Password">Password</label>--%>
                <%--<input class="form-control" type="password" name="j_password" placeholder="">--%>
            <%--</div>--%>
            <%--<input class="btn btn-primary" type="submit" value="Login"/>--%>
        <%--</form>--%>
        <%--<hr>--%>
    <%--</div>--%>
<%--</div>--%>


<div class="container" ng-controller="loginController">
    <div class="row">
        <div class="col-md-4 col-md-offset-4">
            <c:if test="${error == null}">
                <h5 ng-show="successMsg" style="color: green;text-decoration-style: double">{{successMsg}}</h5>
                <h5 ng-show="errorMsg" style="color: red;text-decoration-style: double">{{errorMsg}}</h5>
            </c:if>
            <div class="login-panel panel panel-default">
                <div class="panel-heading">
                    <h3 class="panel-title">Please Sign In</h3>
                </div>
                <div class="panel-body">
                    <form role="form" action="<c:url value='j_spring_security_check' />" method="POST">
                        <fieldset>
                            <div class="form-group">
                                <input class="form-control" placeholder="E-mail" name="j_username"  type="email" autofocus>
                            </div>
                            <div class="form-group">
                                <input class="form-control" placeholder="Password" name="j_password" type="password" value="">
                            </div>
                            <div class="checkbox">
                                <label>
                                    <input name="remember" type="checkbox" value="Remember Me">Remember Me
                                </label>
                            </div>
                            <!-- Change this to a button or input when using this as a form -->
                            <a href="index.html" class="btn btn-lg btn-success btn-block">Login</a>
                            <input class="btn btn-lg btn-success btn-block" type="submit" value="Login"/>
                        </fieldset>
                    </form>
                </div>
            </div>
        </div>
    </div>
</div>

<script src="resources/node_modules/jquery/dist/jquery.min.js"></script>
<script src="resources/node_modules/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="resources/node_modules/angular/angular.min.js"></script>
<script src="resources/node_modules/angular-route/angular-route.min.js"></script>
<script src="resources/node_modules/ngstorage/ngStorage.min.js"></script>
<script src="resources/node_modules/custom-js/app.js"></script>
<script src="resources/node_modules/custom-js/controller.js"></script>
<script src="resources/node_modules/metisMenu/metisMenu.min.js"></script>
<script src="resources/node_modules/sb-admin-2/sb-admin-2.js"></script>


<noscript>
    <div class="container">
        <br/>
        <br/>
        <br/>
        <br/>

        <div class="col-sm-6 col-sm-offset-3">
            <div class="panel panel-danger">
                <div class="panel-body">
                    For full functionality of this site it is necessary to enable JavaScript.<br/>
                    Here are the <a href="http://www.enable-javascript.com/" target="_blank">
                    instructions how to enable JavaScript in your web browser</a>.
                </div>
            </div>
        </div>
    </div>
    <style>
        #mainContent {
            display: none;
        }
    </style>
</noscript>

</body>
</html>
