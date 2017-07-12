<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page session="true" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="utf-8">
    <title>Login</title>

    <link rel="icon" href="resources/node_modules/icon/favicon.ico" type="image/x-icon">

    <link href="resources/node_modules/adminbsb/bootstrap/css/bootstrap.css" rel="stylesheet">

    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&subset=latin,cyrillic-ext" rel="stylesheet"
          type="text/css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" type="text/css">

    <!-- Waves Effect Css -->
    <link href="resources/node_modules/adminbsb/node-waves/waves.css" rel="stylesheet"/>

    <!-- Animation Css -->
    <link href="resources/node_modules/adminbsb/animate-css/animate.css" rel="stylesheet"/>


    <!-- Custom Css -->
    <link href="resources/node_modules/adminbsb/css/style.css" rel="stylesheet">

</head>
<body class="login-page" ng-app="loginApp">


<noscript>

    <div class="col-sm-10 col-sm-offset-1">
        <div class="panel panel-danger">
            <div class="panel-body">
                <b> For full functionality of this site it is necessary to enable JavaScript.<br/>
                    Here are the instructions -></b> <a href="http://www.enable-javascript.com/" target="_blank">
                How to enable JavaScript in your web browser</a>.
            </div>
        </div>
    </div>
    <style>
        .mainContent {
            display: none;
        }
    </style>
</noscript>


<div class="login-box mainContent">
    <div class="logo">
        <div class="image">
            <a href="javascript:void(0);"><img src="resources/images/logo-pergon.gif" width="80" height="80"
                                               alt="Company"/><b>Issue
                Tracker</b></a></div>
    </div>
    <div class="card">
        <div class="body">
            <form id="sign_in" method="POST" action="<c:url value='j_spring_security_check'/>">
                <div class="msg">Sign in to start your session</div>
                <c:if test="${error !=null}">
                    <div class="msg"><label class="error" style="color: red; font-size: 12px">${error}</label></div>
                </c:if>
                <div class="input-group">
    <span class="input-group-addon">
    <i class="material-icons">person</i>
    </span>

                    <div class="form-line">
                        <input type="text" class="form-control" name="j_username" placeholder="Username" required
                               autofocus>
                    </div>
                </div>
                <div class="input-group">
    <span class="input-group-addon">
    <i class="material-icons">lock</i>
    </span>

                    <div class="form-line">
                        <input type="password" class="form-control" name="j_password" placeholder="Password" required>
                    </div>
                </div>
                <div class="row">
                    <div class="col-xs-4 col-xs-offset-4 p-t-3">
                        <input class="btn btn-block bg-pink waves-effect" type="submit" value="Login"/>
                    </div>
                </div>
            </form>
        </div>
    </div>
</div>


<script src="resources/node_modules/jquery/dist/jquery.min.js"></script>
<script src="resources/node_modules/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="resources/node_modules/custom-js/custom.js"></script>

<script src="resources/node_modules/adminbsb/node-waves/waves.js"></script>

<!-- Validation Plugin Js -->
<script src="resources/node_modules/adminbsb/jquery-validation/jquery.validate.js"></script>

<!-- Custom Js -->
<script src="resources/node_modules/adminbsb/js/admin.js"></script>
<script src="resources/node_modules/adminbsb/js/pages/examples/sign-in.js"></script>


</body>
</html>
