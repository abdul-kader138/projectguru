<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title></title>
    <link rel="stylesheet" href="resources/node_modules/bootstrap/dist/css/bootstrap.min.css">
    <link rel="stylesheet" href="resources/node_modules/custom-css/app.css">
</head>

<body ng-app="myApp">
<div id="mainContent" class="hideAll">
    <nav class="navbar navbar-inverse">
        <div class="container-fluid" ng-controller="mainController">
            <a class="navbar-brand" href="#/other">RM Traders</a>
            <ul class="nav navbar-nav">

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="1"><span
                            class="glyphicon glyphicon-cog"></span> Users Setting
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')">
                            <li><a href="#/createUser"><span class="glyphicon glyphicon-plus"></span> Create New
                                User</a></li>
                        </sec:authorize>
                        <li><a href="#/changePassword"><span class="glyphicon glyphicon-wrench"></span> Change Password</a>
                        </li>
                        <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')">
                            <li><a href="#/updateUser"><span class="glyphicon glyphicon-floppy-remove"></span> Edit User</a>
                            </li>
                        </sec:authorize>
                    </ul>
                </li>

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="1"><span
                            class="glyphicon glyphicon-cog"></span> Customer Setting
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')">
                            <li><a href="#/createCustomer"><span class="glyphicon glyphicon-plus"></span> Create New
                                Customer</a></li>
                        </sec:authorize>
                        <sec:authorize access="hasAnyRole('ROLE_ADMIN','ROLE_SUPER_ADMIN')">
                            <li><a href="#/updateCustomer"><span class="glyphicon glyphicon-floppy-remove"></span> Edit
                                Customer</a>
                            </li>
                        </sec:authorize>
                        <li><a href="#/customerDetails"><span class="glyphicon glyphicon-wrench"></span> Customer
                            Details</a>
                        </li>
                    </ul>
                </li>
                <li>
                    <a href="#/other">Link</a>
                </li>
            </ul>
            <ul class="nav navbar-nav pull-right">

                <li class="dropdown">
                    <a class="dropdown-toggle" data-toggle="dropdown" href="1"><span
                            class="glyphicon glyphicon-user"></span> {{loggedUser}}
                        <span class="caret"></span></a>
                    <ul class="dropdown-menu">
                        <li><a href="<c:url value="j_spring_security_logout" />"> Logout</a></li>
                        <li><a href="#">Page 1-2</a></li>
                        <li><a href="#">Page 1-3</a></li>
                    </ul>
                </li>
            </ul>
            </ul>
        </div>
    </nav>

    <div ng-view></div>
</div>


<script src="resources/node_modules/jquery/dist/jquery.min.js"></script>
<script src="resources/node_modules/bootstrap/dist/js/bootstrap.min.js"></script>
<script src="resources/node_modules/angular/angular.min.js"></script>
<script src="resources/node_modules/angular-route/angular-route.min.js"></script>
<script src="resources/node_modules/ngstorage/ngStorage.min.js"></script>
<script src="resources/node_modules/custom-js/app.js"></script>
<script src="resources/node_modules/custom-js/controller.js"></script>

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
