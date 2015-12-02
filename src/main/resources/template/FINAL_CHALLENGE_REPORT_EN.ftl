<?xml version="1.0" encoding="UTF-8"?>
<html>
<head xmlns="http://www.w3.org/1999/xhtml" lang="en">
  <meta content="text/html; charset=utf-8" http-equiv="Content-Type"/>
  <meta content="telephone=no" name="format-detection"/>
  <meta content="width=device-width, initial-scale=1.0;" name="viewport"/>
  <style>
    * {
      font-family: "Verdana";
    }
  </style>
</head>
<body>
<table marginheight="0" topmargin="0" marginwidth="0" width="100%" style="font-size: 14px;">
  <tr>
    <td align="center" width="1000%">
      <table width="100%" cellpadding="0" cellspacing="0">
        <tr>
          <th align="left">
            <h2 style="font-size: 22px; font-weight: 500; text-transform: capitalize;">${challengeName} <span
              style="font-size: 30px; font-weight: 500;">$${totalPlaceReward?string["0.####"]}</span></h2>
          ${challengeOverview}
          </th>
        </tr>
        <tr>
          <td align="left">
            <br/>
            <table cellpadding="0" cellspacing="0" width="100%">
              <tr>
                <td height="3px" style="background-color: #337ab7;" width="34%"></td>
                <td height="3px" style="background-color: #8a2890" width="33%"></td>
                <td height="3px" style="background-color: #8d8d8d" width="33%"></td>
              </tr>
            </table>
          </td>
        </tr>
        <tr>
          <td align="center">
            <br/>
            <span style="font-size: 18px;"><strong style="font-size: 25px; color:#bf3a2f">${winnersInfo?size}</strong> winner(s)</span>
          <#list winnersInfo as winnerInfo>
            <p style="1font-family:Times New Roman,serif">${winnerInfo.registrantFirstName?capitalize} ${winnerInfo.registrantLastName?capitalize}</p>
          </#list>
          </td>
        </tr>
        <tr>
        <#include "FINAL_CHALLENGE_REPORT_${phaseEntries?size}_PHASES_EN.ftl" encoding="UTF-8">
        </tr>
        <tr>
          <td align="left" style="height: 30px;"></td>
        </tr>
        <tr>
          <td align="left" style="height: 3px; background-color: #f4f4f4;"></td>
        </tr>
        <tr>
          <td align="left" style="height: 30px;"></td>
        </tr>
        <tr>
          <td align="left" width="100%">
            <table width="100%" style="background-color:#fff">
              <tr>
                <td align="left" width="70%">
                  <h3 style="font-size: 18px; font-weight: 700;">Challenge Online</h3>
                  Build And Grow Your Developer Community Base With Online Contests.
                </td>
                <td align="right" width="30%">
                  <img src="${baseUrl}images/logo.png" alt="" style="margin: auto"/>
                </td>
              </tr>
            </table>
          </td>
        </tr>
      </table>
    </td>
  </tr>
</table>
</body>
</html>