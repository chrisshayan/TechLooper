<%@page language="java" contentType="text/html; charset=utf-8" %>
<!DOCTYPE html>
<head>

    <!-- Meta-Information -->
    <title>${challenge.getChallengeName()}</title>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <link rel="shortcut icon" href="icon.ico" type="image/x-icon"/>
    <meta name="description"
          content="${challenge.getChallengeOverview()}">
    <meta content='width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0' name='viewport'/>

    <!-- Facebook & LinkedIn -->
    <meta property="og:title" content="${challenge.getChallengeName()}"/>
    <meta property="og:description" content="${challenge.getChallengeOverview()}"/>
    <meta property="og:type" content="article" />

    <meta property="og:image" content="http://techlooper.com/images/logo-social.png"/>
    <meta property="og:image:url" content="http://techlooper.com/images/logo-social.png"/>
    <meta property="og:image:width" content="200"/>
    <meta property="og:url"
          content='${baseUrl}shareChallenge/${lang}/${challenge.getChallengeId()}' />

    <meta property="article:author" content="http://techlooper.com" />
    <meta property="article:publisher" content="http://techlooper.com" />

    <!-- Twitter -->

</head>

<body onload="window.location='${baseUrl}#/contest-detail/${challenge.getChallengeName().replaceAll("\\W", "-")}-${challenge.getChallengeId()}-id?lang=${lang}'">
</body>

</html>