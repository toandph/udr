<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
    <head>
        <title>UDR - <spring:message code="help"/></title>

        <link type="text/css" href="../media/js/menus/menu.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/my-theme/jquery-ui-1.9.2.custom.css" rel="stylesheet" />
        <link type="text/css" href="../media/js/DataTables-1.9.1../media/css/demo_table_jui.css" rel="stylesheet" />

        <link type="text/css" href="../media/css/layout-default.css" rel="stylesheet" />
        <link type="text/css" href="../media/css/custom-accordion.css" rel="stylesheet" />

        <![if IE]><link href="../media/css/main-ie.css" rel="stylesheet" type="text/css" /><![endif]>
        <![if !IE]><link type="text/css" href="../media/css/main.css" rel="stylesheet" /><![endif]>

        <script type="text/javascript" src="../media/js/jquery-1.8.3.js"></script>
        <script type="text/javascript" src="../media/js/jquery-ui-1.9.2.custom.min.js"></script>
        <script type="text/javascript" src="../media/js/jquery.multi-open-accordion.js"></script>


        <script type="text/javascript" language="javascript" src="../media/js/DataTables-1.9.1../media/js/jquery.dataTables.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.layout-latest.min.js"></script>
        <script type="text/javascript" language="javascript" src="../media/js/jquery.textchange.min.js"></script>

        <script language="javascript" src="../media/js/init-common-2-columns-layout.js"></script>
        <script language="javascript" src="../media/js/init-button.js"></script>
        <script language="javascript" src="../media/js/init-left-menu-multi.js"></script>

    </head>

    <body>
        <div class="wrapper">
            <!-- Header and Menu -->
            <%@include file="../include/header.jsp" %>
            <%@include file="../include/outside-menu.jsp" %>
            <!-- End of Header Menu -->

            <!-- Layout Content -->
            <div id="myPane">
                <div class="ui-layout-center">
                    <h3>1. Login / Logout</h3>
                    <ul>
                        <li>To access the UDR system. We should login with the appropriate role. To login, click on the Login menu on the right.</li>
                        <li>To logout, you just click on the logout menu at the same place at login. Login menu is changed to logout when user is successfully login.</li>
                    </ul>
                    <h3><a name="create-project">2. Project List</a></h3>
                    <p><strong>a. How to create project</strong></p>
                    <ul>
                        <li>Create project can be created by select the Create project button in the Project List menu. It also can be created under UDR Admin menu. However to enter the UDR Admin we should login as Administrator. </li>
                        <li>The project information requires the project name and the description.</li>
                        <li>The project name : from 5-64 characters. It can be any name that represent for the project. </li>
                        <li>The project description: It is required. You can put any things here to decribe your project.</li>
                    </ul>
                    <p><img src="../media/images/help/createproject.JPG" width="515" height="170" alt="Create project" /></p>
                    <p><em>Fig 1. Create project forms.</em></p>
                    <p><strong>b. How to delete project</strong></p>
                    <ul>
                        <li>To modify or delete project, you should have the administrator role.</li>
                        <li>Then click on the UDR Admin. You will see the project list. Then you can select the project that will be deleted. And choose delete button as shown in the Fig 2.</li>
                        <li>The filter input is using for quick search the project.</li>
                    </ul>
                    <img src="../media/images/help/deleteproject.JPG" width="849" height="133" />
                    <p><em>Fig 2. Project list</em></p>
                    <p><strong><a name="modify-project">c. How to modify project</a></strong></p>
                    <ul>
                        <li>Select the project by click into the project link in the list.</li>
                        <li> The menu is changed to Project Menu that includes the menu navigate to the project management page.</li>
                        <li>Then we can choose the Project Admin in the menu. </li>
                        <li>Select View Project Info, and from here we can edit the information for the project as in Fig 3. We only can edit the project name and the description. The created by, created date, and source type is the extra information.</li>
                    </ul>
                    <p><img src="../media/images/help/editproject.JPG" width="524" height="245" />
                    </p>
                    <p><em>Fig 3. Project Edit Information</em></p>
                    <p>&nbsp;</p>
                    <h3><a name="create-user">3. User Information</a></h3>
                    <ul>
                        <li>Only the Admin can have the privilege to create and modify the user information.</li>
                        <li>To create user, we select the UDR Admin menu, and go to User Management section. </li>
                        <li>From the list of the user, we click to the Create User button. And the form is shown as Fig 4.</li>
                        <li>All these input is required to fill. </li>
                        <li>The role is include the Admin and User Role.</li>
                        <li>Click the Create User button. If the information is valid, then a new user will be created. You will see it in the list.</li>
                    </ul>
                    <p><img src="../media/images/help/createuser.JPG" width="384" height="183" /></p>
                    <p><em>Fig 4. Create user form</em></p>
                    <ul>
                        <a name="modify-user"></a>
                        <li>In the list of the user, click on the user we want to modified. </li>
                        <li>The Fig 5 shows the user information. We can change it and click save.</li>
                    </ul>
                    <p><img src="../media/images/help/edituser2.JPG" width="383" height="180" /></p>
                    <p><em>Fig 5. Edit user form</em></p>
                    
                    <h3><a name="create-filter">4. Filter</a></h3>

                    <p><strong>a. How to create a filter</strong>.</p>
                    <ul>
                        <li>Filter name: This is the name of the filter. We can put any thing here.</li>
                        <li>Apply on build: we specified which build can be applied with this filter. We spcified each build id seperated by the comma ','.</li>
                        <li>Example: If we want this filter to be applied on build 3 and build 5. You can write: 3,5</li>
                        <li>To be easy, we can specified only 'all' to specified this filter is apply on all build.</li>
                        <li>Advance value: you can left this field blank. Or you can see the rule in the table below.</li>
                        <li>State choose: select the state that you want to filter.</li>
                        <li>Status choose: select the status that you want to filter.</li>
                        <li>Choose directory: the directory is the source tree of the build. We can set it scope to search in which file, which folder by check in the checkbox.</li>
                    </ul>
                    <p><img src="../media/images/help/createfilter.JPG" width="526" height="382" /></p>
                    <p><em>Fig 6. Create Filter</em></p>
                    <p><strong><a name="advance-value">b. Advance filter rules:</a></strong></p>
                    <table width="100%" border="0">
                        <tbody>
                        <tr>
                            <td width="14%">Filter Label:</td>
                            <td width="86%">The column names of issues that we want to filter</td>
                        </tr>
                        <tr>
                            <td>Labels:</td>
                            <td>Available for: <strong>code</strong>, <strong>method</strong>, <strong>scope</strong>, <strong>status</strong>, <strong>state</strong></td>
                        </tr>
                        <tr>
                            <td>Wildcard:</td>
                            <td>* using to replace all string value</td>
                        </tr>
                        <tr>
                            <td>Include only:</td>
                            <td>put '+' right before every label</td>
                        </tr>
                        <tr>
                            <td>Exclude all:</td>
                            <td>put '-' right before every label</td>
                        </tr>
                        <tr>
                            <td>Filter value:</td>
                            <td>seperate each label's value by comma ','</td>
                        </tr>
                        <tr>
                            <td>Filter:</td>
                            <td>end every label with semi-colon ';'</td>
                        </tr>
                        <tr>
                            <td>Detail value of Label:</td>
                            <td>Please consider in issue table</td>
                        </tr>
                        <tr>
                            <td>Example 1:</td>
                            <td>Filter all file in folder 'packages/apps/Music' except files contain name 'Test.java': <strong>+scope:*packages/apps/Music*;-scope:*Test.java;</strong></td>
                        </tr>
                        <tr>
                            <td>Example 2:</td>
                            <td>Filter all State that are EXISTING and NEW but exclude the issues that has severity level 1 and 2:<strong> +state:EXISTING,NEW</strong></td>
                        </tr>
                        <tr>
                            <td>Example 3:</td>
                            <td>Filter all issues that contain error code NPE and severity level are 2 and 4: <strong>+code:*NPE*</strong></td>
                        </tr>
                        </tbody>
                    </table>

                    <h3><a name="create-build">5. Create build</a></h3>
                    <ul>
                        <li>To create build, we select the Project Admin, and under the Build Management, we will see the build form as Fig 7.</li>
                        <li>Build name: we specify any name for the build here.</li>
                        <li>XML file select: we select the xml file.</li>
                        <li>Source zip: we select the souce zip file. To optimize we can zip the source by the SourceZipper tool.</li>
                        <li>Source type: current version is support the Checkmarx, K9, and Goanna XML.                  </li>
                    </ul>
                    <p><img src="../media/images/help/createbuild.JPG" width="417" height="194" /></p>
                    <p><em>Fig 7. Create build          </em></p>
                    <h3><a name="report">6. Report</a></h3>
                    <ul>
                        <li>We can view the report under the Report menu.</li>
                        <li>Current version support 4 kind of charts:
                            <ul>
                                <li>State Pie Chart </li>
                                <li>Severity Level Pie Chart </li>
                                <li>State Line Chart</li>
                                <li> Error Code Pie Chart</li>
                            </ul>
                        </li>
                        <li>We can change the build directly in the build dropbox as Fig 8.</li>
                    </ul>
                    <p><img src="../media/images/help/report1.JPG" width="473" height="410" /></p>
                    <p><em>Fig 8. Report</em></p>
                    <h3><a name="issue">7. Issue Management</a></h3>
                    <ul>
                        <li>Fig 9 shows the issue management. This page show the current issue of the current build. We can change the build and filter by select in the dropbox.</li>
                        <li>We can search for a issue by enter the search. The search field must be 3 characters or more.</li>
                        <li>We can export the issue into xml, csv, text, html format.</li>
                        <li>We can edit the status of the issue.</li>
                    </ul>
                    <p><img src="../media/images/help/issueList.JPG" width="872" height="452" /></p>
                    <p><em>Fig 9. Issue List</em>          </p>
                    <ul>
                        <li>Click in the issue in the issue list will show the detail. The detail include the source view as shown in Fig 10. </li>
                        <li>The trace will be shown in the left side. And also, the history of comment is in the left side Fig 11.</li>
                    </ul>
                    <p><img src="../media/images/help/issuesource.JPG" width="782" height="579" /></p>
                    <p><em>Fig 10. Source Browser</em></p>
                    <p><img src="../media/images/help/issuecomment.JPG" alt="" width="333" height="297" /></p>
                    <p><em>Fig 11. Comment history</em></p>
                </div>

                <div class="ui-layout-west">
                    <div id="accordion">
                        <h3 class="accordionMenu"><strong><spring:message code="project"/></strong></h3>
                        <div>
                            <div><a href="#create-project"><spring:message code="create-project"/></a></div>
                            <div><a href="#modify-project"><spring:message code="edit-project"/></a></div>
                        </div>

                        <h3 class="accordionMenu"><strong><spring:message code="user"/></strong></h3>
                        <div>
                            <div><a href="#create-user"><spring:message code="create-user"/></a></div>
                            <div><a href="#modify-user"><spring:message code="edit-user"/></a></div>
                        </div>

                        <h3 class="accordionMenu"><strong><spring:message code="filter"/></strong></h3>
                        <div>
                            <div><a href="#create-filter"><spring:message code="create-filter"/></a></div>
                            <div><a href="#advance-value"><spring:message code="advance-value-brief"/></a></div>
                        </div>

                        <h3 class="accordionMenu"><strong><spring:message code="build"/></strong></h3>
                        <div>
                            <div><a href="#create-build"><spring:message code="create-build"/></a></div>
                            <div><a href="#create-build"><spring:message code="edit-build"/></a></div>
                        </div>

                        <h3 class="accordionMenu"><strong><spring:message code="issue-management"/></strong></h3>
                        <div>
                            <div><a href="#report"><spring:message code="report"/></a></div>
                            <div><a href="#issue"><spring:message code="issue-management"/></a></div>
                        </div>
                    </div>

                </div>
            </div>
            <!-- End of Layout Content -->
        </div>

    </body>
</html>