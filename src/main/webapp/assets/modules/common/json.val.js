angular.module("Common").constant("jsonValue", {

  histograms: {
    oneWeek: "ONE_WEEK",
    sixBlocksOfFiveDays: "SIX_BLOCKS_OF_FIVE_DAYS",
    sixBlocksOfFifteenDays: "SIX_BLOCKS_OF_FIFTEEN_DAYS",
    oneMonth: "ONE_MONTH",
    eighteenBlocksOfFiveDays: "EIGHTEEN_BLOCKS_OF_FIVE_DAYS",
    twoWeeks: "TWO_WEEKS",
    twoMonths: "TWO_MONTHS",
    twoQuarters: "TWO_QUARTERS"
  },

  notifications: {
    switchScope: "switch to other scope",
    defaultAction: "default action",
    mouseHover: "Mouse Hover",
    gotData: "Got Data",
    changeLang: "Change language"
  },

  viewTerms: {
    unlistedItems: {
      label: function(obj){return obj.term;},
      color: "white"
    },

    listedItems: [
      {
        term: "JAVA", label: "Java", color: "#bf06b7"
      },
      {
        term: "DOTNET", label: ".Net", color: "#d50708"
      },
      {
        term: "PHP", label: "Php", color: "#450770"
      },
      {
        term: "RUBY", label: "Ruby", color: "#d5876a"
      },
      {
        term: "PYTHON", label: "Python", color: "#d7c500"
      },
      {
        term: "PROJECT_MANAGER", label: "Project Manager", color: "#17875f"
      },
      {
        term: "DBA", label: "DBA", color: "#666600"
      },
      {
        term: "QA", label: "QA", color: "#0071bb"
      },
      {
        term: "BA", label: "BA", color: "#996600"
      }
      //{
      //  term: "WEB", label: "Web Development"
      //},
      //{
      //  term: "MOBILE", label: "Mobile Development"
      //}
    ]
  },

  termColor: [
    {
      "Name": "JAVA",
      "color": "#bf06b7"
    },
    {
      "Name": "DOTNET",
      "color": "#d50708"
    },
    {
      "Name": "PHP",
      "color": "#450770"
    },
    {
      "Name": "PROJECT_MANAGER",
      "color": "#17875f"
    },
    {
      "Name": "RUBY",
      "color": "#d5876a"
    },
    {
      "Name": "PYTHON",
      "color": "#d7c500"
    },
    {
      "Name": "DBA",
      "color": "#666600"
    },
    {
      "Name": "QA",
      "color": "#0071bb"
    },
    {
      "Name": "BA",
      "color": "#996600"
    }
  ],

  skillColors: ["#bf06b7", "#9ed701", "#3c6373", "#006600", "#c53046", "#fbb425", "#666600", "#0071bb", "#996600", "#00eaff", "#10F4FB", "#2404FB", "#35E811"],

  events: {
    terms: "Terms",
    term: "Term",
    changeChart: "Change Chart",
    foundJobs: "Found Jobs",
    analyticsSkill: "Analytics Skill"
  },

  routerUris: {
    pie: "/pie-chart",
    bubble: "/bubble-chart",
    jobsSearch: "/jobs/search",
    analyticsSkill: "/analytics/skill",
    signIn: "signin"
  },

  views: {
    jobsSearch: "jobSearch",
    jobsSearchText: "jobSearchText",
    analyticsSkill: "analyticsSkill",
    bubbleChart: "bubbleChart",
    pieChart: "pieChart"
  },

  socketUri: {
    sockjs: "ws",

    sendTerms: "/app/jobs/terms",
    subscribeTerms: "/topic/jobs/terms",
    subscribeTerm: "/topic/jobs/term/",

    sendJobsSearch: "/app/jobs/search",
    subscribeJobsSearch: "/topic/jobs/search",

    analyticsSkill: "/app/analytics/skill",
    subscribeAnalyticsSkill: "/topic/analytics/skill"
  },

  mPositionDefault: [{
    "data": [{
      "top": "290px",
      "left": "20px"
    }, {
      "top": "220px",
      "left": "215px"
    }, {
      "top": "25px",
      "left": "100px"
    }, {
      "top": "250px",
      "left": "180px"
    }, {
      "top": "220px",
      "left": "-10px"
    }, {
      "top": "105px",
      "left": "230px"
    }, {
      "top": "290px",
      "left": "90px"
    }, {
      "top": "10px",
      "left": "-10px"
    }, {
      "top": "20px",
      "left": "190px"
    }]
  }],
  dPositionDefault: [{
    "data": [{
      "top": "5px",
      "left": "150px"
    }, {
      "top": "190px",
      "left": "370px"
    }, {
      "top": "75px",
      "left": "360px"
    }, {
      "top": "340px",
      "left": "0"
    }, {
      "top": "380px",
      "left": "70px"
    }, {
      "top": "260px",
      "left": "-40px"
    }, {
      "top": "150px",
      "left": "-50px"
    }, {
      "top": "305px",
      "left": "350px"
    }, {
      "top": "390px",
      "left": "290px"
    }]
  }],
  mBubblePosition: [{
    "name": "java",
    "data": [{
      "top": "75px",
      "left": "150px"
    }, {
      "top": "280px",
      "left": "50px"
    }, {
      "top": "5px",
      "left": "80px"
    }, {
      "top": "75px",
      "left": "215px"
    }, {
      "top": "270px",
      "left": "100px"
    }, {
      "top": "265px",
      "left": "-5px"
    }, {
      "top": "280px",
      "left": "180px"
    }, {
      "top": "260px",
      "left": "180px"
    }, {
      "top": "280px",
      "left": "170px"
    }]
  }, {
    "name": ".net",
    "data": [{
      "top": "10px",
      "left": "10px"
    }, {
      "top": "150px",
      "left": "250px"
    }, {
      "top": "290px",
      "left": "180px"
    }, {
      "top": "290px",
      "left": "105px"
    }, {
      "top": "230px",
      "left": "70px"
    }, {
      "top": "25px",
      "left": "-10px"
    }, {
      "top": "40px",
      "left": "0px"
    }, {
      "top": "280px",
      "left": "70px"
    }, {
      "top": "5px",
      "left": "50px"
    }]
  }, {
    "name": "php",
    "data": [{
      "top": "10px",
      "left": "80px"
    }, {
      "top": "250px",
      "left": "-15px"
    }, {
      "top": "-240px",
      "left": "10px"
    }, {
      "top": "290px",
      "left": "10px"
    }, {
      "top": "10px",
      "left": "70px"
    }, {
      "top": "5px",
      "left": "70px"
    }, {
      "top": "255px",
      "left": "-10px"
    }, {
      "top": "50px",
      "left": "210px"
    }, {
      "top": "200px",
      "left": "-25px"
    }]
  }, {
    "name": "ruby",
    "data": [{
      "top": "100px",
      "left": "220px"
    }, {
      "top": "10px",
      "left": "-10px"
    }, {
      "top": "250px",
      "left": "0"
    }, {
      "top": "0px",
      "left": "50px"
    }, {
      "top": "200px",
      "left": "230px"
    }, {
      "top": "200px",
      "left": "-15px"
    }, {
      "top": "150px",
      "left": "-20px"
    }, {
      "top": "30px",
      "left": "130px"
    }, {
      "top": "260px",
      "left": "10px"
    }]
  }, {
    "name": "python",
    "data": [{
      "top": "290px",
      "left": "20px"
    }, {
      "top": "280px",
      "left": "180px"
    }, {
      "top": "140px",
      "left": "-20px"
    }, {
      "top": "150px",
      "left": "-20px"
    }, {
      "top": "290px",
      "left": "15px"
    }, {
      "top": "285px",
      "left": "80px"
    }, {
      "top": "90px",
      "left": "230px"
    }, {
      "top": "140px",
      "left": "-20px"
    }, {
      "top": "290px",
      "left": "70px"
    }]
  }, {
    "name": "dba",
    "data": [{
      "top": "50px",
      "left": "210px"
    }, {
      "top": "50px",
      "left": "20px"
    }, {
      "top": "280px",
      "left": "50px"
    }, {
      "top": "15px",
      "left": "175px"
    }, {
      "top": "80px",
      "left": "215px"
    }, {
      "top": "240px",
      "left": "220px"
    }, {
      "top": "220px",
      "left": "215px"
    }, {
      "top": "65px",
      "left": "-125px"
    }, {
      "top": "70px",
      "left": "10px"
    }]
  }, {
    "name": "pm",
    "data": [{
      "top": "270px",
      "left": "80px"
    }, {
      "top": "0",
      "left": "180px"
    }, {
      "top": "0",
      "left": "-15px"
    }, {
      "top": "10px",
      "left": "20px"
    }, {
      "top": "280px",
      "left": "180px"
    }, {
      "top": "-50px",
      "left": "210px"
    }, {
      "top": "0",
      "left": "60px"
    }, {
      "top": "250px",
      "left": "-20px"
    }, {
      "top": "20px",
      "left": "190px"
    }]
  }, {
    "name": "ba",
    "data": [{
      "top": "210px",
      "left": "-25px"
    }, {
      "top": "100px",
      "left": "-20px"
    }, {
      "top": "20px",
      "left": "160px"
    }, {
      "top": "225px",
      "left": "220px"
    }, {
      "top": "80px",
      "left": "-20px"
    }, {
      "top": "70px",
      "left": "220px"
    }, {
      "top": "300px",
      "left": "80px"
    }, {
      "top": "55px",
      "left": "-10px"
    }, {
      "top": "-50px",
      "left": "150px"
    }]
  }, {
    "name": "qa",
    "data": [{
      "top": "230px",
      "left": "210px"
    }, {
      "top": "0",
      "left": "110px"
    }, {
      "top": "220px",
      "left": "210px"
    }, {
      "top": "70px",
      "left": "-20px"
    }, {
      "top": "290px",
      "left": "30px"
    }, {
      "top": "30px",
      "left": "155px"
    }, {
      "top": "250px",
      "left": "130px"
    }, {
      "top": "20px",
      "left": "50px"
    }, {
      "top": "140px",
      "left": "230px"
    }]
  }],
  dBubblePosition: [{
    "name": "java",
    "data": [{
      "top": "75px",
      "left": "150px"
    }, {
      "top": "340px",
      "left": "0"
    }, {
      "top": "115px",
      "left": "350px"
    }, {
      "top": "340px",
      "left": "0"
    }, {
      "top": "330px",
      "left": "-30px"
    }, {
      "top": "300px",
      "left": "-40px"
    }, {
      "top": "150px",
      "left": "-50px"
    }, {
      "top": "305px",
      "left": "350px"
    }, {
      "top": "320px",
      "left": "0"
    }]
  }, {
    "name": ".net",
    "data": [{
      "top": "390px",
      "left": "230px"
    }, {
      "top": "150px",
      "left": "300px"
    }, {
      "top": "60px",
      "left": "0"
    }, {
      "top": "380px",
      "left": "300px"
    }, {
      "top": "35px",
      "left": "0"
    }, {
      "top": "50px",
      "left": "310px"
    }, {
      "top": "10px",
      "left": "300px"
    }, {
      "top": "350px",
      "left": "20px"
    }, {
      "top": "20px",
      "left": "280px"
    }]
  }, {
    "name": "php",
    "data": [{
      "top": "40px",
      "left": "10px"
    }, {
      "top": "110px",
      "left": "350px"
    }, {
      "top": "-240px",
      "left": "160px"
    }, {
      "top": "250px",
      "left": "360px"
    }, {
      "top": "390px",
      "left": "140px"
    }, {
      "top": "165px",
      "left": "-40px"
    }, {
      "top": "230px",
      "left": "360px"
    }, {
      "top": "250px",
      "left": "-40px"
    }, {
      "top": "225px",
      "left": "-35px"
    }]
  }, {
    "name": "ruby",
    "data": [{
      "top": "400px",
      "left": "120px"
    }, {
      "top": "130px",
      "left": "-20px"
    }, {
      "top": "380px",
      "left": "310px"
    }, {
      "top": "0px",
      "left": "150px"
    }, {
      "top": "20px",
      "left": "290px"
    }, {
      "top": "390px",
      "left": "190px"
    }, {
      "top": "5px",
      "left": "170px"
    }, {
      "top": "370px",
      "left": "280px"
    }, {
      "top": "70px",
      "left": "10px"
    }]
  }, {
    "name": "python",
    "data": [{
      "top": "150px",
      "left": "360px"
    }, {
      "top": "235px",
      "left": "380px"
    }, {
      "top": "20px",
      "left": "130px"
    }, {
      "top": "80px",
      "left": "20px"
    }, {
      "top": "150px",
      "left": "300px"
    }, {
      "top": "150px",
      "left": "395px"
    }, {
      "top": "250px",
      "left": "-20px"
    }, {
      "top": "200px",
      "left": "360px"
    }, {
      "top": "180px",
      "left": "390px"
    }]
  }, {
    "name": "dba",
    "data": [{
      "top": "295px",
      "left": "370px"
    }, {
      "top": "50px",
      "left": "300px"
    }, {
      "top": "240px",
      "left": "390px"
    }, {
      "top": "50px",
      "left": "340px"
    }, {
      "top": "280px",
      "left": "360px"
    }, {
      "top": "320px",
      "left": "350px"
    }, {
      "top": "380px",
      "left": "310px"
    }, {
      "top": "130px",
      "left": "290px"
    }, {
      "top": "20px",
      "left": "90px"
    }]
  }, {
    "name": "pm",
    "data": [{
      "top": "-5px",
      "left": "260px"
    }, {
      "top": "350px",
      "left": "300px"
    }, {
      "top": "350px",
      "left": "190px"
    }, {
      "top": "120px",
      "left": "350px"
    }, {
      "top": "325px",
      "left": "260px"
    }, {
      "top": "-50px",
      "left": "310px"
    }, {
      "top": "330px",
      "left": "-10px"
    }, {
      "top": "10px",
      "left": "10px"
    }, {
      "top": "330px",
      "left": "300px"
    }]
  }, {
    "name": "ba",
    "data": [{
      "top": "280px",
      "left": "-50px"
    }, {
      "top": "0",
      "left": "110px"
    }, {
      "top": "250px",
      "left": "-60px"
    }, {
      "top": "200px",
      "left": "-40px"
    }, {
      "top": "150px",
      "left": "-35px"
    }, {
      "top": "60px",
      "left": "-10px"
    }, {
      "top": "100px",
      "left": "380px"
    }, {
      "top": "0",
      "left": "140px"
    }, {
      "top": "95px",
      "left": "360px"
    }]
  }, {
    "name": "qa",
    "data": [{
      "top": "160px",
      "left": "-35px"
    }, {
      "top": "420px",
      "left": "210px"
    }, {
      "top": "40px",
      "left": "310px"
    }, {
      "top": "20px",
      "left": "110px"
    }, {
      "top": "150px",
      "left": "390px"
    }, {
      "top": "-10px",
      "left": "130px"
    }, {
      "top": "250px",
      "left": "230px"
    }, {
      "top": "25px",
      "left": "290px"
    }, {
      "top": "260px",
      "left": "360px"
    }]
  }],
  technicalSkill: [
    {
      'text': 'Java',
      'logo': 'lg-java.png'
    },
    {
      'text': '.Net',
      'logo': 'lg-dotnet.png'
    },
    {
      'text': 'Php',
      'logo': 'lg-php.png'
    },
    {
      'text': 'Ruby',
      'logo': 'lg-ruby.png'
    },
    {
      'text': 'Python',
      'logo': 'lg-python.png'
    },
    {
      'text': 'C++',
      'logo': 'lg-cplusplus.png'
    },
    {
      'text': 'Html5',
      'logo': 'lg-html5.png'
    },
    {
      'text': 'Bootstrap',
      'logo': 'lg-bootstrap.png'
    },
    {
      'text': 'AngularJS',
      'logo': 'lg-angularjs.png'
    },
    {
      'text': 'Jquery',
      'logo': 'lg-jquery.png'
    },
    {
      'text': 'Spring',
      'logo': 'lg-spring.png'
    },
    {
      'text': 'Sass',
      'logo': 'lg-sass.png'
    },
    {
      'text': 'Magento',
      'logo': 'lg-magento.png'
    },
    {
      'text': 'Node',
      'logo': 'lg-notejs.png'
    },
    {
      'text': 'Photoshop',
      'logo': 'lg-photoshop.png'
    },
    {
      'text': 'Less',
      'logo': 'lg-less.png'
    },
    {
      'text': 'Joomla',
      'logo': 'lg-joomla.png'
    },
    {
      'text': 'Responsive Design',
      'logo': 'lg-responsive-design.png'
    },
    {
      'text': 'Css3',
      'logo': 'lg-css3.png'
    },
    {
      'text': 'Drupal',
      'logo': 'lg-drupal.png'
    },
    {
      'text': 'Ajax',
      'logo': 'lg-ajax.png'
    },
    {
      'text': 'Backbone',
      'logo': 'lg-backbone.png'
    },
    {
      'text': 'Illustrator',
      'logo': 'lg-illustrator.png'
    }

  ],
  shortcuts: [{
    "id": 1,
    "name": "Career Analytics",
    "keyShort": "Ctrl + Alt + A"
  }, {
    "id": 2,
    "name": "Find Job",
    "keyShort": "Ctrl + Alt + F"
  }, {
    "id": 3,
    "name": "Function Name 1",
    "keyShort": "Ctrl + Alt + 1"
  }, {
    "id": 4,
    "name": "Function Name 2",
    "keyShort": "Ctrl + Alt + 2"
  }],
  companies: [{
    "id": 1,
    "img": "images/cp-logo-atlassian.png",
    "url": "https://www.atlassian.com/"
  }, {
    "id": 2,
    "img": "images/cp-logo-google.png",
    "url": "https://www.google.com/"
  }, {
    "id": 3,
    "img": "images/cp-logo-techcombank.png",
    "url": "https://www.techcombank.com.vn/"
  }, {
    "id": 4,
    "img": "images/cp-logo-fpt.png",
    "url": "https://www.fpt.vn/"
  }, {
    "id": 5,
    "img": "images/cp-logo-harveynash.png",
    "url": "https://www.harveynash.vn/"
  }, {
    "id": 6,
    "img": "images/cp-logo-vtv.png",
    "url": "https://www.vtv.vn/"
  }],
  accountSignin: [
    {
      "site": "VietnamWork",
      "urlSignin": "http://www.vietnamworks.com/",
      "logo": "images/vietnamwork.png",
      "magnitude": "level1",
      "bgcolor": "#1fbaec"
    },
    {
      "site": "Linkedin",
      "urlSignin": "http://linkedin.com/",
      "logo": "images/linkedin.png",
      "magnitude": "level2",
      "bgcolor": "#007bb6"
    },
    {
      "site": "Facebook",
      "urlSignin": "http://facebook.com/",
      "logo": "images/facebook.png",
      "magnitude": "level3",
      "bgcolor": "#3b5a9b"
    },
    {
      "site": "Stackoverflow",
      "urlSignin": "http://stackoverflow.com/",
      "logo": "images/stackoverflow.png",
      "magnitude": "level4",
      "bgcolor": "#fda428"
    },
    {
      "site": "Gmail",
      "urlSignin": "https://mail.google.com/",
      "logo": "images/gmail.png",
      "magnitude": "level5",
      "bgcolor": "#ac2e25"
    },
    {
      "site": "Github",
      "urlSignin": "https://github.com/",
      "logo": "images/github.png",
      "magnitude": "level6",
      "bgcolor": "#333333"
    },
    {
      "site": "Bitbucket",
      "urlSignin": "https://bitbucket.org/",
      "logo": "images/bitbucket.png",
      "magnitude": "level7",
      "bgcolor": "#fff"
    }
  ]
});
