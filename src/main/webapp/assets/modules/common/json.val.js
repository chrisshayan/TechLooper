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
      changeLang: "Change language",
      changeUrl: "Change URL"
  },

  viewTerms: {
      unlistedItems: {
          label: function(obj) {
              return obj.label === undefined ? obj.term : obj.label;
          },
          color: "white"
      },

      listedItems: [{
              term: "JAVA",
              label: "Java",
              color: "#bf06b7"
          }, {
              term: "DOTNET",
              label: ".Net",
              color: "#d50708"
          }, {
              term: "PHP",
              label: "Php",
              color: "#450770"
          }, {
              term: "RUBY",
              label: "Ruby",
              color: "#d5876a"
          }, {
              term: "PYTHON",
              label: "Python",
              color: "#d7c500"
          }, {
              term: "PROJECT_MANAGER",
              label: "Project Manager",
              color: "#17875f"
          }, {
              term: "DBA",
              label: "DBA",
              color: "#666600"
          }, {
              term: "QA",
              label: "QA",
              color: "#0576cd"
          }, {
              term: "BA",
              label: "BA",
              color: "#996600"
          },{
            term: "CPLUSPLUS",
            label: "C++",
            color: "#00CFA1"
          },
          {
            term: "WEB", 
            label: "Web Development",
            color: "#AA9439"
          },
          {
            term: "MOBILE", 
            label: "Mobile Development",
            color: "#6F256F"
          }
      ]
  },

  termColor: [{
      "Name": "JAVA",
      "color": "#bf06b7"
  }, {
      "Name": "DOTNET",
      "color": "#d50708"
  }, {
      "Name": "PHP",
      "color": "#450770"
  }, {
      "Name": "PROJECT_MANAGER",
      "color": "#17875f"
  }, {
      "Name": "RUBY",
      "color": "#d5876a"
  }, {
      "Name": "PYTHON",
      "color": "#d7c500"
  }, {
      "Name": "DBA",
      "color": "#666600"
  }, {
      "Name": "QA",
      "color": "#1fb43f"
  }, {
      "Name": "BA",
      "color": "#996600"
  }],

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
      signIn: "signin",
      register: "register"
  },

  views: {
      jobsSearch: "jobSearch",
      jobsSearchText: "jobSearchText",
      analyticsSkill: "analyticsSkill",
      bubbleChart: "bubbleChart",
      pieChart: "pieChart",
      signIn: "signin",
      register: "register"
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
  technicalSkill: [{
      'text': 'Java',
      'logo': 'lg-java.png'
  }, {
      'text': '.Net',
      'logo': 'lg-dotnet.png'
  }, {
      'text': 'Php',
      'logo': 'lg-php.png'
  }, {
      'text': 'Ruby',
      'logo': 'lg-ruby.png'
  }, {
      'text': 'Python',
      'logo': 'lg-python.png'
  }, {
      'text': 'C++',
      'logo': 'lg-cplusplus.png'
  }, {
      'text': 'Html5',
      'logo': 'lg-html5.png'
  }, {
      'text': 'Bootstrap',
      'logo': 'lg-bootstrap.png'
  }, {
      'text': 'AngularJS',
      'logo': 'lg-angularjs.png'
  }, {
      'text': 'Jquery',
      'logo': 'lg-jquery.png'
  }, {
      'text': 'Spring',
      'logo': 'lg-spring.png'
  }, {
      'text': 'Sass',
      'logo': 'lg-sass.png'
  }, {
      'text': 'Magento',
      'logo': 'lg-magento.png'
  }, {
      'text': 'Node',
      'logo': 'lg-notejs.png'
  }, {
      'text': 'Photoshop',
      'logo': 'lg-photoshop.png'
  }, {
      'text': 'Less',
      'logo': 'lg-less.png'
  }, {
      'text': 'Joomla',
      'logo': 'lg-joomla.png'
  }, {
      'text': 'Responsive Design',
      'logo': 'lg-responsive-design.png'
  }, {
      'text': 'Css3',
      'logo': 'lg-css3.png'
  }, {
      'text': 'Drupal',
      'logo': 'lg-drupal.png'
  }, {
      'text': 'Ajax',
      'logo': 'lg-ajax.png'
  }, {
      'text': 'Backbone',
      'logo': 'lg-backbone.png'
  }, {
      'text': 'Illustrator',
      'logo': 'lg-illustrator.png'
  }],
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
  accountSignin: [{
      "site": "VietnamWork",
      "urlSignin": "http://www.vietnamworks.com/",
      "logo": "images/vietnamwork.png",
      "magnitude": "level1",
      "bgcolor": "#1fbaec"
  }, {
      "site": "Linkedin",
      "urlSignin": "http://linkedin.com/",
      "logo": "images/linkedin.png",
      "magnitude": "level2",
      "bgcolor": "#007bb6"
  }, {
      "site": "Facebook",
      "urlSignin": "http://facebook.com/",
      "logo": "images/facebook.png",
      "magnitude": "level3",
      "bgcolor": "#3b5a9b"
  }, {
      "site": "Stackoverflow",
      "urlSignin": "http://stackoverflow.com/",
      "logo": "images/stackoverflow.png",
      "magnitude": "level4",
      "bgcolor": "#fda428"
  }, {
      "site": "Gmail",
      "urlSignin": "https://mail.google.com/",
      "logo": "images/gmail.png",
      "magnitude": "level5",
      "bgcolor": "#ac2e25"
  }, {
      "site": "Github",
      "urlSignin": "https://github.com/",
      "logo": "images/github.png",
      "magnitude": "level6",
      "bgcolor": "#333333"
  }, {
      "site": "Bitbucket",
      "urlSignin": "https://bitbucket.org/",
      "logo": "images/bitbucket.png",
      "magnitude": "level7",
      "bgcolor": "#fff"
  }],
  introTour:{
    template: "<div class='popover tour'><div class='arrow'></div><h3 class='popover-title'></h3><div class='popover-content'></div><div class='popover-navigation'><button class='btn btn-default prev-tour' data-role='prev'><i class='fa fa-caret-square-o-left'></i></button><button class='btn btn-default next-tour' data-role='next'><i class='fa fa-caret-square-o-right'></i></button><button class='btn btn-default close-tour' data-role='end'><i class='fa fa-close'></i></button></div></nav></div>",
    pieHomePage: [
      {
        element: ".setting-content",
        placement: "right",
        title: "Setting",
        content: "You can change chart type (Bubble/Pie) or language here."
      },
      {
        element: ".signin-signout-container",
        placement: "left",
        title: "Sign In/Sign Out",
        content: "You can sign in with Vietnamworks account, Github account, LinkedIn account..."
      },
      {
        element: ".highcharts-container",
        placement: "top",
        title: "Pie Chart",
        content: "In this chart, you can see how many jobs of the Term"
      },
      {
        element: ".find-jobs-button",
        placement: "top",
        title: "Find Jobs",
        content: "Click to this button, you can find job on search job page"
      }
    ],
    bubbleHomePage: [
      {
        element: ".setting-content",
        placement: "right",
        title: "Setting",
        content: "You can change chart type (Bubble/Pie) or language here."
      },
      {
        element: ".signin-signout-container",
        placement: "left",
        title: "Sign In/Sign Out",
        content: "You can sign in with Vietnamworks account, Github account, LinkedIn account..."
      },
      {
        element: "#box",
        placement: "top",
        title: "Bubble Chart",
        content: "In this chart, you can see how many jobs of the Term"
      },
      {
        element: ".find-jobs-button",
        placement: "top",
        title: "Find Jobs",
        content: "Click to this button, you can find job on search job page"
      }
    ],
    technicalDetail:[
       {
        element: ".term-infor",
        placement: "right",
        title: "Term chart",
        content: "The number of jobs of The Term, and job percent of term on total jobs."
      },
      {
        element: "#circle-1",
        placement: "right",
        title: "Skill chart",
        content: "The number of jobs of skill"
      },
      {
        element: ".chart-management ul",
        placement: "bottom",
        title: "Period of Line chart",
        content: "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
      },
      {
        element: ".highcharts-container",
        placement: "top",
        title: "Line chart",
        content: "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
      },
      {
        element: ".rwd-table",
        placement: "top",
        title: "table chart",
        content: "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
      },
      {
        element: ".term-useful-links",
        placement: "top",
        title: "Useful link for the Term",
        content: "Lorem Ipsum is simply dummy text of the printing and typesetting industry."
      }
    ],
    signIn:[
      {
        element: ".signin-contianer",
        placement: "left",
        title: "Sign with social account",
        content: "You can choose one account you like to sign in."
      }
    ],
    searchForm:[
      {
        element: ".selectize-input ",
        placement: "bottom",
        title: "Search Box",
        content: "Enter text to search..."
      },
      {
        element: ".technical-Skill-List",
        placement: "top",
        title: "choose Term logo for search box ",
        content: "You can click to Term logo to selection for the search."
      },
      {
        element: ".btn-close",
        placement: "left",
        title: "Close",
        content: "Click here to close Search page and back to Home page."
      }
    ]
  }
});