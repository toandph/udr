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
        <h3>1. 로그인 / 로그아웃</h3>
        <ul>
            <li>UDR System에 접근하기 위해서는 등록된 아이디로 로그인해야 합니다. 우측상단 로그인 버튼을 이용해 로그인 할 수 있습니다.</li>
            <li>로그아웃하기 위해서는 로그인 버튼과 동일한 위치에 있는 로그아웃 버튼을 누르기만 하면 됩니다. 로그인이 성공적으로 완료되면 버튼이 로그아웃으로 자동으로 바뀝니다.</li>
        </ul>
        <h3>2. 프로젝트 목록</h3>
        <p><strong>a. 프로젝트 생성하기</strong></p>
        <ul>
            <li>프로젝트 목록에 있는 프로젝트 생성 버튼을 클릭하여 프로젝트를 생성합니다. 관리자로 로그인 시 UDR 관리자 메뉴에서도 생성할 수 있습니다.</li>
            <li>프로젝트 정보에는 프로젝트명과 설명이 필요합니다.</li>
            <li>프로젝트명 : 5~64글자(영문기준)까지 지원가능하며, 제약사항은 없습니다. </li>
            <li>프로젝트 설명: 필수입력란으로 설정되어 있으며 프로젝트에 대한 설명을 기입합니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/createproject.JPG" width="515" height="170" alt="Create project" /></p>
        <p><em>Fig 1. 프로젝트 생성 화면</em></p>
        <p><strong>b. 프로젝트 삭제</strong></p>
        <ul>
            <li>프로젝트를 수정하거나 삭제하기 위해서는 관리자 권한이 필요합니다. </li>
            <li>UDR 관리자 메뉴로 들어가서 프로젝트 목록이 보이면, 삭제할 프로젝트를 선택한 후 프로젝트 삭제 버튼을 클릭합니다.</li>
            <li>필터값 입력은 프로젝트가 많을 시에 빠르게 프로젝트를 찾는것을 도와줍니다.</li>
        </ul>
        <img src="../media/images/help_ko/deleteproject.JPG" width="849" height="133" />
        <p><em>Fig 2. 프로젝트 목록 화면</em></p>
        <p><strong>c. 프로젝트 수정</strong></p>
        <ul>
            <li>프로젝트 목록에서 수정할 프로젝트의 이름을 클릭합니다.</li>
            <li>이후 프로젝트 관리자 메뉴를 클릭하면 빌드 관리와 프로젝트 정보 보기 메뉴가 나타납니다.</li>
            <li>프로젝트 정보 보기를 클릭 하고, Fig 3와 같이 프로젝트의 정보가 나타납니다. 여기에서는 오로지 프로젝트명과 설명만 수정가능하며 생성자, 생성일, 소스타입 등은 부가 정보로 표시됩니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/editproject.JPG" width="524" height="245" />
        </p>
        <p><em>Fig 3. 프로젝트 수정 화면</em></p>
        <p>&nbsp;</p>
        <h3>3. 사용자 정보</h3>
        <ul>
            <li>관리자만이 유저 생성 및 편집이 가능합니다.</li>
            <li>사용자를 생성하기 위해서는 UDR 관리자 메뉴로 들어가서 사용자 관리를 클릭합니다. </li>
            <li>사용자 목록이 표시되면 사용자 생성 버튼을 누르면 Fig 4와 같은 화면을 볼 수 있습니다.</li>
            <li>모든 필드는 필수입력 요소입니다.</li>
            <li>권한은 관리자와 사용자 권한이 모두 포함되어 나타납니다.</li>
            <li>입력한 정보가 정확히 입력되면 사용자 생성에 성공하고 사용자 목록에 보이게 될 것입니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/createuser.JPG" width="384" height="183" /></p>
        <p><em>Fig 4. 사용자 생성 화면</em></p>
        <ul>
            <li>사용자 목록에서 편집하려고하는 사용자를 클릭합니다. </li>
            <li>Fig 5에서처럼 사용자 정보가 보여지고 이를 편집하고 저장할 수 있습니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/edituser2.JPG" width="383" height="180" /></p>
        <p><em>Fig 5. 사용자 편집 화면</em></p>
        <h3>4. 필터 기능 </h3>
        <p><strong>a. 필터 생성</strong>.</p>
        <ul>
            <li>필터 이름: 필터의 이름을 입력합니다.</li>
            <li>적용할 빌드: 특정 빌드에서 적용할 것인지를 선택합니다. 각 빌드는 ',' 콤마로 구분해서 입력할 수 있습니다.</li>
            <li>(예시): 빌드 3번과 빌드 5번에 적용하고 싶다면 다음과 같이 입력합니다 : 3,5</li>
            <li>쉽게는 'all' 이라고 입력하면 모든 빌드에 적용가능 합니다.</li>
            <li>고급 필터: 공란으로 비워둘 수도 있으며 아래처럼 특정 룰을 추가할 수 있습니다</li>
            <li>상태 선택: 필터링될 상태를 입력합니다.</li>
            <li>진행상황 선택: 필터링할 진행상황을 입력합니다.</li>
            <li>디렉토리 선택: 빌드의 소스코드 트리에서 디렉토리를 선택합니다. 체크박스에 체크를 통해 특정 폴더나 특정파일을 필터링 할 수 있습니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/createfilter.JPG" width="526" height="382" /></p>
        <p><em>Fig 6. 필터 생성</em></p>
        <p><strong>b. 고급 필터:</strong></p>
        <table width="100%" border="0">
            <tbody>
            <tr>
                <td width="14%">필터 라벨:</td>
                <td width="86%">필터할 이슈의 네임을 입력하는 란입니다.</td>
            </tr>
            <tr>
                <td>라벨:</td>
                <td>사용가능: <strong>코드(code)</strong>, <strong>메소드(method)</strong>, <strong>범위(scope)</strong>, <strong>진행상황(status)</strong>, <strong>상태(state)</strong></td>
            </tr>
            <tr>
                <td>와일드카드:</td>
                <td>모든 문자값들을 대체하기 위해 쓰여집니다.</td>
            </tr>
            <tr>
                <td>포함할 라벨:</td>
                <td>'+' 를 모든 라벨의 오른쪽에 붙입니다.</td>
            </tr>
            <tr>
                <td>제외할 라벨:</td>
                <td>'-' 를 모든 라벨의 오른쪽에 붙입니다.</td>
            </tr>
            <tr>
                <td>필터 값:</td>
                <td>각 라벨의 값을 ','로 구분하여 입력합니다.</td>
            </tr>
            <tr>
                <td>필터:</td>
                <td>라벨 입력이 끝나면 ';' 세미콜론으로 입력합니다.</td>
            </tr>
            <tr>
                <td>라벨값 상세항목:</td>
                <td>이슈테이블에서 각 항목을 표시합니다.</td>
            </tr>
            <tr>
                <td>예제 1:</td>
                <td>모든 폴더 내 파일을 포함하지만 'packages/apps/Music' 폴더의 'Test.java' 파일을 제외: <strong>+scope:*packages/apps/Music*;-scope:*Test.java;</strong></td>
            </tr>
            <tr>
                <td>예제 2:</td>
                <td>상태값이 EXISTING, NEW인 이슈들만 필터:<strong> +state:EXISTING,NEW</strong></td>
            </tr>
            <tr>
                <td>예제 3:</td>
                <td>에러코드가 NPE를 포함하는 모든 이슈들만 필터: <strong>+code:*NPE*</strong></td>
            </tr>
            </tbody>
        </table>
        <h3>5. 빌드 만들기</h3>
        <ul>
            <li>빌드를 만들기 위해서는 프로젝트 관리자 메뉴에서 '빌드관리'로 들어갑니다. Fig 7과 같은 화면을 볼 수 있습니다.</li>
            <li>빌드 이름: 특정 빌드 이름을 따로 만들 수 있습니다.</li>
            <li>XML 파일 선택: XML 파일을 업로드 합니다.</li>
            <li>Source zip: Source zip 파일을 업로드 합니다. Source zipper 로 압축한 파일.</li>
            <li>Source type: Checkmarx, K9, Goanna 와 같은 분석한 소스의 툴 종류를 선택합니다. </li>
        </ul>
        <p><img src="../media/images/help_ko/createbuild.JPG" width="417" height="194" /></p>
        <p><em>Fig 7. 빌드 생성          </em></p>
        <h3>6. 리포트</h3>
        <ul>
            <li>리포트 메뉴에서 그래프 등의 리포트를 볼 수 있습니다.</li>
            <li>현재 4종류의 차트를 지원합니다. :
                <ul>
                    <li>상태 파이 차트</li>
                    <li>위험도별 파이 차트</li>
                    <li>현재상황 라인 차트</li>
                    <li>에러코드별 파이 차트</li>
                </ul>
            </li>
            <li>각 빌드의 차트를 아래 그림과 같이 드랍다운 메뉴에서 선택해서 볼 수 있습니다. </li>
        </ul>
        <p><img src="../media/images/help_ko/report1.JPG" width="473" height="410" /></p>
        <p><em>Fig 8. 리포트</em></p>
        <h3>7. 이슈 관리</h3>
        <ul>
            <li>아래 그림은 이슈 목록을 보여줍니다. 해당 빌드의 이슈들을 보여주고 있는데 우상단 드랍다운 메뉴를 통해 빌드를 변경하거나 필터를 적용할 수 있습니다.</li>
            <li>검색을 통해 원하는 이슈를 검색할 수 있습니다. (최소 3글자 이상 검색가능 합니다.)</li>
            <li>xml, csv, txt, html 파일로 목록을 저장할 수 있습니다.</li>
            <li>해당이슈의 상태를 수정할 수 있습니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/issueList.JPG" width="872" height="452" /></p>
        <p><em>Fig 9. 이슈 목록</em>          </p>
        <ul>
            <li>이슈목록에서 해당 이슈를 클릭하면 상세정보 페이지로 넘어갑니다. 상세정보에는 소스코드를 포함한 여러 정보를 볼수 있습니다. </li>
            <li>Trace 정보와 이슈 히스토리 등 정보는 좌측에서 확인할 수 있습니다.</li>
        </ul>
        <p><img src="../media/images/help_ko/issuesource.JPG" width="782" height="579" /></p>
        <p><em>Fig 10. 소스 브라우저</em></p>
        <p><img src="../media/images/help_ko/issuecomment.JPG" alt="" width="333" height="297" /></p>
        <p><em>Fig 11. 코멘트 히스토리</em></p>
    </div>

    <div class="ui-layout-west">
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
</div>
<!-- End of Layout Content -->
</div>

</body>
</html>