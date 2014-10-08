angular.module("Common").constant("jsonValue", {
   events : {
      terms : "Terms",
      term : "Term"
   },
   charts: {
      pie : "pie",
      bubble : "bubble"
   },
   socketUri : {
      sockjs : "ws",
      sendTerms : "/app/technical-job/terms",
      subscribeTerms : "/topic/technical-job/terms",
      subscribeTerm : "/topic/technical-job/"
   },
   
   availableLanguageKeys : [ {
      "name" : "EN",
      "value" : "en-US"
   }, {
      "name" : "VI",
      "value" : "vi"
   } ],
   mPositionDefault : [ {
      "data" : [ {
         "top" : "290px",
         "left" : "20px"
      }, {
         "top" : "220px",
         "left" : "120px"
      }, {
         "top" : "40px",
         "left" : "-100px"
      }, {
         "top" : "150px",
         "left" : "-130px"
      }, {
         "top" : "220px",
         "left" : "-115px"
      }, {
         "top" : "105px",
         "left" : "125px"
      }, {
         "top" : "290px",
         "left" : "-90px"
      }, {
         "top" : "10px",
         "left" : "-10px"
      }, {
         "top" : "290px",
         "left" : "70px"
      } ]
   } ],
   dPositionDefault : [ {
      "data" : [ {
         "top" : "5px",
         "left" : "50px"
      }, {
         "top" : "190px",
         "left" : "280px"
      }, {
         "top" : "75px",
         "left" : "260px"
      }, {
         "top" : "340px",
         "left" : "-100px"
      }, {
         "top" : "330px",
         "left" : "-100px"
      }, {
         "top" : "260px",
         "left" : "-140px"
      }, {
         "top" : "150px",
         "left" : "-150px"
      }, {
         "top" : "305px",
         "left" : "250px"
      }, {
         "top" : "390px",
         "left" : "190px"
      } ]
   } ],
   mBubblePosition : [ {
      "name" : "java",
      "data" : [ {
         "top" : "75px",
         "left" : "50px"
      }, {
         "top" : "230px",
         "left" : "-120px"
      }, {
         "top" : "5px",
         "left" : "-40px"
      }, {
         "top" : "75px",
         "left" : "115px"
      }, {
         "top" : "270px",
         "left" : "-100px"
      }, {
         "top" : "300px",
         "left" : "-55px"
      }, {
         "top" : "50px",
         "left" : "-120px"
      }, {
         "top" : "260px",
         "left" : "80px"
      }, {
         "top" : "300px",
         "left" : "-30px"
      } ]
   }, {
      "name" : ".net",
      "data" : [ {
         "top" : "10px",
         "left" : "10px"
      }, {
         "top" : "150px",
         "left" : "250px"
      }, {
         "top" : "300px",
         "left" : "-10px"
      }, {
         "top" : "245px",
         "left" : "105px"
      }, {
         "top" : "280px",
         "left" : "100px"
      }, {
         "top" : "5px",
         "left" : "-60px"
      }, {
         "top" : "-10px",
         "left" : "-20px"
      }, {
         "top" : "300px",
         "left" : "-10px"
      }, {
         "top" : "5px",
         "left" : "-10px"
      } ]
   }, {
      "name" : "php",
      "data" : [ {
         "top" : "20px",
         "left" : "-100px"
      }, {
         "top" : "280px",
         "left" : "-45px"
      }, {
         "top" : "-240px",
         "left" : "10px"
      }, {
         "top" : "290px",
         "left" : "10px"
      }, {
         "top" : "10px",
         "left" : "70px"
      }, {
         "top" : "5px",
         "left" : "70px"
      }, {
         "top" : "260px",
         "left" : "-120px"
      }, {
         "top" : "50px",
         "left" : "100px"
      }, {
         "top" : "230px",
         "left" : "105px"
      } ]
   }, {
      "name" : "ruby",
      "data" : [ {
         "top" : "100px",
         "left" : "120px"
      }, {
         "top" : "10px",
         "left" : "-10px"
      }, {
         "top" : "250px",
         "left" : "-120px"
      }, {
         "top" : "0px",
         "left" : "50px"
      }, {
         "top" : "200px",
         "left" : "115px"
      }, {
         "top" : "240px",
         "left" : "-115px"
      }, {
         "top" : "150px",
         "left" : "-120px"
      }, {
         "top" : "200px",
         "left" : "130px"
      }, {
         "top" : "260px",
         "left" : "-120px"
      } ]
   }, {
      "name" : "python",
      "data" : [ {
         "top" : "290px",
         "left" : "20px"
      }, {
         "top" : "220px",
         "left" : "120px"
      }, {
         "top" : "40px",
         "left" : "-120px"
      }, {
         "top" : "150px",
         "left" : "-120px"
      }, {
         "top" : "290px",
         "left" : "15px"
      }, {
         "top" : "285px",
         "left" : "25px"
      }, {
         "top" : "290px",
         "left" : "-30px"
      }, {
         "top" : "20px",
         "left" : "-10px"
      }, {
         "top" : "290px",
         "left" : "70px"
      } ]
   }, {
      "name" : "dba",
      "data" : [ {
         "top" : "50px",
         "left" : "100px"
      }, {
         "top" : "30px",
         "left" : "-70px"
      }, {
         "top" : "280px",
         "left" : "50px"
      }, {
         "top" : "15px",
         "left" : "75px"
      }, {
         "top" : "315px",
         "left" : "-45px"
      }, {
         "top" : "240px",
         "left" : "110px"
      }, {
         "top" : "220px",
         "left" : "110px"
      }, {
         "top" : "65px",
         "left" : "-125px"
      }, {
         "top" : "70px",
         "left" : "110px"
      } ]
   }, {
      "name" : "pm",
      "data" : [ {
         "top" : "280px",
         "left" : "-80px"
      }, {
         "top" : "260px",
         "left" : "50px"
      }, {
         "top" : "110px",
         "left" : "-125px"
      }, {
         "top" : "10px",
         "left" : "-120px"
      }, {
         "top" : "-5px",
         "left" : "-100px"
      }, {
         "top" : "-50px",
         "left" : "210px"
      }, {
         "top" : "30px",
         "left" : "60px"
      }, {
         "top" : "250px",
         "left" : "-110px"
      }, {
         "top" : "20px",
         "left" : "-120px"
      } ]
   }, {
      "name" : "ba",
      "data" : [ {
         "top" : "210px",
         "left" : "-115px"
      }, {
         "top" : "100px",
         "left" : "-120px"
      }, {
         "top" : "20px",
         "left" : "60px"
      }, {
         "top" : "225px",
         "left" : "-120px"
      }, {
         "top" : "80px",
         "left" : "-120px"
      }, {
         "top" : "70px",
         "left" : "-120px"
      }, {
         "top" : "300px",
         "left" : "80px"
      }, {
         "top" : "55px",
         "left" : "-110px"
      }, {
         "top" : "-50px",
         "left" : "150px"
      } ]
   }, {
      "name" : "qa",
      "data" : [ {
         "top" : "230px",
         "left" : "100px"
      }, {
         "top" : "60px",
         "left" : "110px"
      }, {
         "top" : "220px",
         "left" : "110px"
      }, {
         "top" : "-12px",
         "left" : "10px"
      }, {
         "top" : "290px",
         "left" : "30px"
      }, {
         "top" : "110px",
         "left" : "125px"
      }, {
         "top" : "250px",
         "left" : "130px"
      }, {
         "top" : "20px",
         "left" : "50px"
      }, {
         "top" : "140px",
         "left" : "130px"
      } ]
   } ],
   dBubblePosition : [ {
      "name" : "java",
      "data" : [ {
         "top" : "75px",
         "left" : "50px"
      }, {
         "top" : "340px",
         "left" : "-100px"
      }, {
         "top" : "75px",
         "left" : "260px"
      }, {
         "top" : "340px",
         "left" : "-100px"
      }, {
         "top" : "330px",
         "left" : "-100px"
      }, {
         "top" : "300px",
         "left" : "-140px"
      }, {
         "top" : "150px",
         "left" : "-150px"
      }, {
         "top" : "355px",
         "left" : "250px"
      }, {
         "top" : "390px",
         "left" : "190px"
      } ]
   }, {
      "name" : ".net",
      "data" : [ {
         "top" : "420px",
         "left" : "110px"
      }, {
         "top" : "150px",
         "left" : "250px"
      }, {
         "top" : "60px",
         "left" : "-100px"
      }, {
         "top" : "380px",
         "left" : "200px"
      }, {
         "top" : "10px",
         "left" : "80px"
      }, {
         "top" : "50px",
         "left" : "230px"
      }, {
         "top" : "10px",
         "left" : "200px"
      }, {
         "top" : "350px",
         "left" : "-80px"
      }, {
         "top" : "0px",
         "left" : "100px"
      } ]
   }, {
      "name" : "php",
      "data" : [ {
         "top" : "40px",
         "left" : "-60px"
      }, {
         "top" : "10px",
         "left" : "250px"
      }, {
         "top" : "-240px",
         "left" : "10px"
      }, {
         "top" : "230px",
         "left" : "280px"
      }, {
         "top" : "230px",
         "left" : "-190px"
      }, {
         "top" : "115px",
         "left" : "-180px"
      }, {
         "top" : "230px",
         "left" : "280px"
      }, {
         "top" : "230px",
         "left" : "-130px"
      }, {
         "top" : "205px",
         "left" : "-130px"
      } ]
   }, {
      "name" : "ruby",
      "data" : [ {
         "top" : "340px",
         "left" : "-150px"
      }, {
         "top" : "30px",
         "left" : "30px"
      }, {
         "top" : "380px",
         "left" : "210px"
      }, {
         "top" : "0px",
         "left" : "50px"
      }, {
         "top" : "0px",
         "left" : "190px"
      }, {
         "top" : "410px",
         "left" : "-90px"
      }, {
         "top" : "55px",
         "left" : "-70px"
      }, {
         "top" : "165px",
         "left" : "290px"
      }, {
         "top" : "20px",
         "left" : "250px"
      } ]
   }, {
      "name" : "python",
      "data" : [ {
         "top" : "100px",
         "left" : "290px"
      }, {
         "top" : "235px",
         "left" : "280px"
      }, {
         "top" : "30px",
         "left" : "30px"
      }, {
         "top" : "80px",
         "left" : "-110px"
      }, {
         "top" : "150px",
         "left" : "200px"
      }, {
         "top" : "150px",
         "left" : "295px"
      }, {
         "top" : "220px",
         "left" : "-110px"
      }, {
         "top" : "60px",
         "left" : "260px"
      }, {
         "top" : "180px",
         "left" : "290px"
      } ]
   }, {
      "name" : "dba",
      "data" : [ {
         "top" : "295px",
         "left" : "270px"
      }, {
         "top" : "50px",
         "left" : "200px"
      }, {
         "top" : "240px",
         "left" : "290px"
      }, {
         "top" : "50px",
         "left" : "240px"
      }, {
         "top" : "320px",
         "left" : "260px"
      }, {
         "top" : "320px",
         "left" : "250px"
      }, {
         "top" : "380px",
         "left" : "210px"
      }, {
         "top" : "130px",
         "left" : "190px"
      }, {
         "top" : "120px",
         "left" : "-140px"
      } ]
   }, {
      "name" : "pm",
      "data" : [ {
         "top" : "-5px",
         "left" : "160px"
      }, {
         "top" : "380px",
         "left" : "250px"
      }, {
         "top" : "350px",
         "left" : "-90px"
      }, {
         "top" : "120px",
         "left" : "290px"
      }, {
         "top" : "15px",
         "left" : "-100px"
      }, {
         "top" : "-50px",
         "left" : "210px"
      }, {
         "top" : "330px",
         "left" : "-110px"
      }, {
         "top" : "10px",
         "left" : "-80px"
      }, {
         "top" : "300px",
         "left" : "-140px"
      } ]
   }, {
      "name" : "ba",
      "data" : [ {
         "top" : "230px",
         "left" : "-160px"
      }, {
         "top" : "40px",
         "left" : "-110px"
      }, {
         "top" : "250px",
         "left" : "-160px"
      }, {
         "top" : "250px",
         "left" : "-150px"
      }, {
         "top" : "110px",
         "left" : "-150px"
      }, {
         "top" : "60px",
         "left" : "-100px"
      }, {
         "top" : "100px",
         "left" : "280px"
      }, {
         "top" : "100px",
         "left" : "-100px"
      }, {
         "top" : "-50px",
         "left" : "150px"
      } ]
   }, {
      "name" : "qa",
      "data" : [ {
         "top" : "60px",
         "left" : "245px"
      }, {
         "top" : "420px",
         "left" : "110px"
      }, {
         "top" : "40px",
         "left" : "210px"
      }, {
         "top" : "20px",
         "left" : "10px"
      }, {
         "top" : "150px",
         "left" : "290px"
      }, {
         "top" : "-10px",
         "left" : "-30px"
      }, {
         "top" : "250px",
         "left" : "130px"
      }, {
         "top" : "25px",
         "left" : "190px"
      }, {
         "top" : "80px",
         "left" : "-80px"
      } ]
   } ],
   shortcuts : [ {
      "id" : 1,
      "name" : "Career Analytics",
      "keyShort" : "Ctrl + Alt + A"
   }, {
      "id" : 2,
      "name" : "Find Job",
      "keyShort" : "Ctrl + Alt + F"
   }, {
      "id" : 3,
      "name" : "Function Name 1",
      "keyShort" : "Ctrl + Alt + 1"
   }, {
      "id" : 4,
      "name" : "Function Name 2",
      "keyShort" : "Ctrl + Alt + 2"
   } ],
   companies : [ {
      "id" : 1,
      "img" : "images/cp-logo-atlassian.png",
      "url" : "https://www.atlassian.com/"
   }, {
      "id" : 2,
      "img" : "images/cp-logo-google.png",
      "url" : "https://www.google.com/"
   }, {
      "id" : 3,
      "img" : "images/cp-logo-techcombank.png",
      "url" : "https://www.techcombank.com.vn/"
   }, {
      "id" : 4,
      "img" : "images/cp-logo-fpt.png",
      "url" : "https://www.fpt.vn/"
   }, {
      "id" : 5,
      "img" : "images/cp-logo-harveynash.png",
      "url" : "https://www.harveynash.vn/"
   }, {
      "id" : 6,
      "img" : "images/cp-logo-vtv.png",
      "url" : "https://www.vtv.vn/"
   } ]
});
