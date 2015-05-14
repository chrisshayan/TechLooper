<%@page language="java" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<head>

    <!-- Meta-Information -->
    <title>Techlooper | Career Analytics. Open Source. Awesome!</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="shortcut icon" href="icon.ico" type="image/x-icon"/>
    <meta name="description"
          content="Techlooper is a Career Analytics">
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport'/>

    <!-- Facebook & LinkedIn -->
    <meta property="og:title" content="TechLooper is a career analytics platform."/>
    <meta property="og:description"
          content="Tôi kiếm được nhiều tiền hơn ${report.getPercentRank()} những người giống như tôi. Bạn thì sao?"/>
    <meta property="og:image" content="http://techlooper.com/images/logo-social.png"/>
    <meta property="og:image:width" content="200"/>
    <!-- Twitter -->
</head>
<body>
    <%response.sendRedirect("/#/salary-review");%>
</body>
</html>