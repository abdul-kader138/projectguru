<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>

<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=Edge">
    <meta content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no" name="viewport">
    <title>Welcome To | Paragon Solutions</title>
    <!-- Favicon-->
    <link rel="icon" href="favicon.ico" type="image/x-icon">

    <!-- Google Fonts -->
    <link href="https://fonts.googleapis.com/css?family=Roboto:400,700&subset=latin,cyrillic-ext" rel="stylesheet"
          type="text/css">
    <link href="https://fonts.googleapis.com/icon?family=Material+Icons" rel="stylesheet" type="text/css">

    <!-- Bootstrap Core Css -->
    <link href="resources/node_modules/adminbsb/bootstrap/css/bootstrap.css" rel="stylesheet">
    <link rel="stylesheet" type="text/css" href="resources/node_modules/datatable/jquery.dataTables.min.css">

    <!-- Waves Effect Css -->
    <link href="resources/node_modules/adminbsb/node-waves/waves.css" rel="stylesheet"/>

    <!-- Animation Css -->
    <link href="resources/node_modules/adminbsb/animate-css/animate.css" rel="stylesheet"/>

    <!-- Morris Chart Css-->
    <link href="resources/node_modules/adminbsb/morrisjs/morris.css" rel="stylesheet"/>

    <!-- Custom Css -->
    <link href="resources/node_modules/adminbsb/css/style.css" rel="stylesheet">

    <!-- AdminBSB Themes. You can choose a theme from css/themes instead of get all themes -->
    <link href="resources/node_modules/adminbsb/css/themes/all-themes.css" rel="stylesheet"/>


    <link href="resources/node_modules/adminbsb/dialog/css/dialogbox.css" rel="stylesheet"/>
    <link href="resources/node_modules/imageloader/loadimg.min.css" rel="stylesheet"/>

    <link href="resources/node_modules/custom-css/app.css" rel="stylesheet"/>

    <script src="resources/node_modules/datatable/jquery.js"></script>
</head>

<body class="theme-red">


<!-- Page Loader -->
<div class="page-loader-wrapper">
    <div class="loader">
        <div class="preloader">
            <div class="spinner-layer pl-red">
                <div class="circle-clipper left">
                    <div class="circle"></div>
                </div>
                <div class="circle-clipper right">
                    <div class="circle"></div>
                </div>
            </div>
        </div>
        <p>Please wait...</p>
    </div>
</div>
<!-- #END# Page Loader -->
<!-- Overlay For Sidebars -->
<div class="overlay"></div>
<!-- #END# Overlay For Sidebars -->
<!-- Search Bar -->
<div class="search-bar">
    <div class="search-icon">
        <i class="material-icons">search</i>
    </div>
    <input type="text" placeholder="START TYPING...">

    <div class="close-search">
        <i class="material-icons">close</i>
    </div>
</div>
<!-- #END# Search Bar -->
<!-- Top Bar -->
<nav class="navbar" id="menu">
    <div class="container-fluid">
        <div class="navbar-header">
            <a href="javascript:void(0);" class="navbar-toggle collapsed" data-toggle="collapse"
               data-target="#navbar-collapse" aria-expanded="false"></a>
            <a href="javascript:void(0);" class="bars"></a>
            <a class="navbar-brand" href="#">Paragon Solutions - Issue Tracker</a>
        </div>
        <div class="collapse navbar-collapse" id="navbar-collapse">
            <ul class="nav navbar-nav navbar-right">
                <!-- Call Search -->
                <li><a href="javascript:void(0);" class="js-search" data-close="true"><i
                        class="material-icons">search</i></a></li>
                <!-- #END# Call Search -->
                <!-- Notifications -->
                <c:if test="${user.userType != 'developer'}">
                    <li class="dropdown" id="approval_details_notification">
                        <a href="approval_details" id="notification">
                            <i class="material-icons">notifications</i>
                            <span class="label-count" title="Waiting For Approve"
                                  id="notificationCount">${notificationCount}</span>
                        </a></li>
                </c:if>
                <!-- #END# Tasks -->
                <li class="pull-right"><a href="javascript:void(0);" class="js-right-sidebar" data-close="true"><i
                        class="material-icons">more_vert</i></a></li>
            </ul>
        </div>
    </div>
</nav>
<!-- #Top Bar -->
<section>
    <!-- Left Sidebar -->
    <aside id="leftsidebar" class="sidebar">
        <!-- User Info -->
        <div class="user-info">
            <div class="image">
                <img src="resources/images/user.png" width="48" height="48" alt="User"/>
                <%--<img src="http://localhost:8080 ${user.imagePath}" width="48" height="48" alt="User"/>--%>
            </div>
            <div class="info-container">
                <div class="name" data-toggle="dropdown" aria-haspopup="true" aria-expanded="false">${user.name}</div>
                <div class="email">${user.email}</div>
                <div class="btn-group user-helper-dropdown" id="settings">
                    <i class="material-icons" data-toggle="dropdown" aria-haspopup="true" aria-expanded="true">keyboard_arrow_down</i>
                    <ul class="dropdown-menu pull-right">
                        <li><a href="profile"><i class="material-icons">person</i>Profile</a></li>
                        <li role="seperator" class="divider"></li>
                        <li><a href="change_password"><i class="material-icons">build</i>Change Password &nbsp;</a></li>
                        <li><a href="<c:url value="j_spring_security_logout" />" class="logout"><i class="material-icons">input</i>Sign
                            Out</a></li>
                    </ul>
                </div>
            </div>
        </div>
        <!-- #User Info -->
        <!-- Menu -->
        <div class="menu">
            <ul class="list" id="menuList">
                <li class="header">&nbsp;</li>
                <li class="active">
                    <a href="home">
                        <i class="material-icons">home</i>
                        <span>Home</span>
                    </a>
                </li>

                <%--only visible for admin and super admin user--%>
                <c:if test="${user.role == 'ROLE_ADMIN' || user.role == 'ROLE_SUPER_ADMIN'}">
                    <li>
                        <a href="javascript:void(0);" class="menu-toggle">
                            <i class="material-icons">settings</i>
                            <span>All Settings</span>
                        </a>
                        <ul class="ml-menu">
                            <li>
                                <a href="javascript:void(0);" class="menu-toggle">
                                    <span>Company Settings</span>
                                </a>
                                <ul class="ml-menu">
                                    <li>
                                        <a href="company">Company</a>
                                    </li>
                                    <li>
                                        <a href="department">Department</a>
                                    </li>
                                </ul>
                            </li>
                            <li>
                                <a href="javascript:void(0);" class="menu-toggle">
                                    <span>Product Settings</span>
                                </a>
                                <ul class="ml-menu">
                                    <li>
                                        <a href="product">Product</a>
                                    </li>
                                    <li>
                                        <a href="category">Category</a>
                                    </li>
                                        <%--<li>--%>
                                        <%--<a href="subcategory">Sub-Category</a>--%>
                                        <%--</li>--%>
                                </ul>
                            </li>
                            <li>
                                <a href="javascript:void(0);" class="menu-toggle">
                                    <span>Team Member Settings</span>
                                </a>
                                <ul class="ml-menu">

                                        <%--only visible for super admin user--%>
                                    <c:if test="${user.role == 'ROLE_SUPER_ADMIN' && user.userType == 'vendor'}">
                                        <li>
                                            <a href="roles">Roles</a>

                                        </li>
                                        <li>
                                            <a href="role_right">Role & Rights</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${user.userType == 'vendor'}">
                                        <li>
                                            <a href="user">Team Member</a>
                                            <a href="user_allocation">Team Member Allocation</a>
                                        </li>
                                    </c:if>
                                    <c:if test="${user.userType == 'client'}">
                                        <li>
                                            <a href="team">Team Member</a>
                                            <a href="team_allocation">Team Member Allocation</a>
                                        </li>
                                    </c:if>
                                </ul>
                            </li>
                        </ul>
                    </li>
                    <br/>
                </c:if>
                <c:if test="${user.userType == 'client' && hasChangeRequest=='Yes'}">
                    <li class="header">Request</li>
                    <li>
                        <a href="change_request">
                            <i class="material-icons col-red">donut_large</i>
                            <span>Product Changes Request</span>
                        </a>
                    </li>
                </c:if>
                <br/>
                <c:if test="${user.userType == 'vendor' && user.role == 'ROLE_ADMIN'}">
                    <li class="header">Request</li>
                    <li>
                        <a href="set_priority">
                            <i class="material-icons col-amber">donut_large</i>
                            <span>Set Request Priority</span>
                        </a>
                    </li>
                </c:if>
                <br/>
                <br/>
            </ul>
        </div>
        <!-- #Menu -->
        <!-- Footer -->
        <div class="legal">
            <div class="copyright">
                &copy; 2017 <a href="javascript:void(0);">Paragon Solutions</a>.
            </div>
            <div class="version">
                <b>Version: </b> 1.0.4
            </div>
        </div>
        <!-- #Footer -->
    </aside>
    <!-- #END# Left Sidebar -->
    <!-- Right Sidebar -->
    <aside id="rightsidebar" class="right-sidebar">
        <ul class="nav nav-tabs tab-nav-right" role="tablist">
            <li role="presentation" class="active"><a href="#skins" data-toggle="tab">SKINS</a></li>
            <li role="presentation"><a href="#settings" data-toggle="tab">SETTINGS</a></li>
        </ul>
        <div class="tab-content">
            <div role="tabpanel" class="tab-pane fade in active in active" id="skins">
                <ul class="demo-choose-skin list">
                    <li data-theme="red" class="active colorName">
                        <div class="red"></div>
                        <span>Red</span>
                    </li>
                    <li data-theme="pink" class="colorName">
                        <div class="pink"></div>
                        <span>Pink</span>
                    </li>
                    <li data-theme="purple" class="colorName">
                        <div class="purple"></div>
                        <span>Purple</span>
                    </li>
                    <li data-theme="indigo" class="colorName">
                        <div class="indigo"></div>
                        <span>Indigo</span>
                    </li>
                    <li data-theme="blue" class="colorName">
                        <div class="blue"></div>
                        <span>Blue</span>
                    </li>
                    <li data-theme="cyan" class="colorName">
                        <div class="cyan"></div>
                        <span>Cyan</span>
                    </li>
                    <li data-theme="teal" class="colorName">
                        <div class="teal"></div>
                        <span>Teal</span>
                    </li>
                    <li data-theme="green" class="colorName">
                        <div class="green"></div>
                        <span>Green</span>
                    </li>
                    <li data-theme="lime" class="colorName">
                        <div class="lime"></div>
                        <span>Lime</span>
                    </li>
                    <li data-theme="yellow" class="colorName">
                        <div class="yellow"></div>
                        <span>Yellow</span>
                    </li>
                    <li data-theme="orange" class="colorName">
                        <div class="orange"></div>
                        <span>Orange</span>
                    </li>
                    <li data-theme="brown" class="colorName">
                        <div class="brown"></div>
                        <span>Brown</span>
                    </li>
                    <li data-theme="grey" class="colorName">
                        <div class="grey"></div>
                        <span>Grey</span>
                    </li>
                    <li data-theme="black" class="colorName">
                        <div class="black"></div>
                        <span>Black</span>
                    </li>
                </ul>
            </div>
            <%--<div role="tabpanel" class="tab-pane fade" id="settings">--%>
                <%--<div class="demo-settings">--%>
                    <%--<p>GENERAL SETTINGS</p>--%>
                    <%--<ul class="setting-list">--%>
                        <%--<li>--%>
                            <%--<span>Report Panel Usage</span>--%>

                            <%--<div class="switch">--%>
                                <%--<label><input type="checkbox" checked><span class="lever"></span></label>--%>
                            <%--</div>--%>
                        <%--</li>--%>
                        <%--<li>--%>
                            <%--<span>Email Redirect</span>--%>

                            <%--<div class="switch">--%>
                                <%--<label><input type="checkbox"><span class="lever"></span></label>--%>
                            <%--</div>--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                    <%--<p>SYSTEM SETTINGS</p>--%>
                    <%--<ul class="setting-list">--%>
                        <%--<li>--%>
                            <%--<span>Notifications</span>--%>

                            <%--<div class="switch">--%>
                                <%--<label><input type="checkbox" checked><span class="lever"></span></label>--%>
                            <%--</div>--%>
                        <%--</li>--%>
                        <%--<li>--%>
                            <%--<span>Auto Updates</span>--%>

                            <%--<div class="switch">--%>
                                <%--<label><input type="checkbox" checked><span class="lever"></span></label>--%>
                            <%--</div>--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                    <%--<p>ACCOUNT SETTINGS</p>--%>
                    <%--<ul class="setting-list">--%>
                        <%--<li>--%>
                            <%--<span>Offline</span>--%>

                            <%--<div class="switch">--%>
                                <%--<label><input type="checkbox"><span class="lever"></span></label>--%>
                            <%--</div>--%>
                        <%--</li>--%>
                        <%--<li>--%>
                            <%--<span>Location Permission</span>--%>

                            <%--<div class="switch">--%>
                                <%--<label><input type="checkbox" checked><span class="lever"></span></label>--%>
                            <%--</div>--%>
                        <%--</li>--%>
                    <%--</ul>--%>
                <%--</div>--%>
            <%--</div>--%>
        </div>
    </aside>
    <!-- #END# Right Sidebar -->
</section>
<script>
    $(document).ready(function(){
        $('.logout').click(function () {
            localStorage.colorName ="";
        });
    })
</script>