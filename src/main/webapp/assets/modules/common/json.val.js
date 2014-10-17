angular.module("Common").constant("jsonValue", {

  events: {
    terms: "Terms",
    term: "Term",
    changeChart: "Change Chart",
    foundJobs : "Found Jobs"
  },

  routerUris: {
    pie: "/pie-chart",
    bubble: "/bubble-chart",
    searchJobs: "/search-jobs"
  },

  socketUri: {
    sockjs: "ws",

    sendTerms: "/app/jobs/terms",
    subscribeTerms: "/topic/jobs/terms",
    subscribeTerm: "/topic/jobs/",

    sendJobsSearch: "/app/jobs/search",
    subscribeJobsSearch: "/topic/jobs/search"
  },

  availableLanguageKeys: [{
    "name": "EN",
    "value": "en-US"
  }, {
    "name": "VI",
    "value": "vi"
  }],
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
      "top": "150px",
      "left": "-130px"
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
      "top": "330px",
      "left": "0"
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
      'name': 'Java',
      'logo': 'lg-java.png'
    },
    {
      'name': 'DotNet',
      'logo': 'lg-dotnet.png'
    },
    {
      'name': 'Php',
      'logo': 'lg-php.png'
    },
    {
      'name': 'Ruby',
      'logo': 'lg-ruby.png'
    },
    {
      'name': 'Python',
      'logo': 'lg-python.png'
    },
    {
      'name': 'C++',
      'logo': 'lg-cplusplus.png'
    },
    {
      'name': 'Html5',
      'logo': 'lg-html5.png'
    },
    {
      'name': 'Bootstrap',
      'logo': 'lg-bootstrap.png'
    },
    {
      'name': 'AngularJS',
      'logo': 'lg-angularjs.png'
    },
    {
      'name': 'Jquery',
      'logo': 'lg-jquery.png'
    },
    {
      'name': 'Sass',
      'logo': 'lg-sass.png'
    },
    {
      'name': 'Magento',
      'logo': 'lg-magento.png'
    },
    {
      'name': 'Notejs',
      'logo': 'lg-notejs.png'
    },
    {
      'name': 'Photoshop',
      'logo': 'lg-photoshop.png'
    },
    {
      'name': 'Less',
      'logo': 'lg-less.png'
    },
    {
      'name': 'Joomla',
      'logo': 'lg-joomla.png'
    },
    {
      'name': 'Responsive Design',
      'logo': 'lg-responsive-design.png'
    },
    {
      'name': 'Css3',
      'logo': 'lg-css3.png'
    },
    {
      'name': 'Drupal',
      'logo': 'lg-drupal.png'
    },
    {
      'name': 'Ajax',
      'logo': 'lg-ajax.png'
    },
    {
      'name': 'Backbone',
      'logo': 'lg-backbone.png'
    },
    {
      'name': 'Illustrator',
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
  }]
});
