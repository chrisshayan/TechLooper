angular.module("Common").constant("jsonValue", {

  messages: {
    successSave: "Your data has been successfully saved",
    errorFieldsSave: "Please correct the marked field(s) above"
  },

  storage: {
    key: "key",
    back2Me: "back2Me",
    navigation: "navigation"
  },

  histograms: {
    oneWeek: "ONE_WEEK",
    sixBlocksOfFiveDays: "SIX_BLOCKS_OF_FIVE_DAYS",
    sixBlocksOfFifteenDays: "SIX_BLOCKS_OF_FIFTEEN_DAYS",
    oneMonth: "ONE_MONTH",
    eighteenBlocksOfFiveDays: "EIGHTEEN_BLOCKS_OF_FIVE_DAYS",
    sixMonths: "SIX_MONTHS",
    oneYear: "ONE_YEAR",
    twoWeeks: "TWO_WEEKS",
    twoMonths: "TWO_MONTHS",
    twoQuarters: "TWO_QUARTERS",
    twoSixMonths: "TWO_SIX_MONTHS",
    twoYears: "TWO_YEARS"
  },

  notifications: {
    switchScope: "switch to other scope",
    defaultAction: "default action",
    mouseHover: "Mouse Hover",
    gotData: "Got Data",
    changeLang: "Change language",
    changeUrl: "Change URL",
    loading: "Loading",
    loaded: "Loaded",
    loginSuccess: "Login success",
    loginFailed: "Login failed",
    userInfo: "User info",
    notUserInfo: "Not get User info",
    hideLoadingBox: "Hide loading box",
    http404: "HTTP code 404",
    logoutSuccess: "Logout success",
    cleanSession: "Clean session",
    serverError: "Server error",
    userRegistrationCount: "User registration count"
  },

  viewTerms: {
    unlistedItems: {
      label: function (obj) {
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
    }, {
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
    analyticsSkill: "Analytics Skill",
    userInfo: "UserInfo"
  },

  routerUris: {
    landing: "/landing",
    home: "/home",
    talentSearchResult: "/talent-search-result",
    talentProfile: "/talent-profile",
    companyProfile: "/companies/vietnamworks",
    pie: "/pie-chart",
    bubble: "/bubble-chart",
    jobsSearch: "/jobs/search",
    analyticsSkill: "/analytics/skill",
    signIn: "/signin",
    register: "/register",
    userProfile: "/user"
  },

  views: {
    landing: "landing",
    home: "home",
    talentSearchResult: "talentSearchResult",
    talentProfile: "talentProfile",
    companyProfile: "companyProfile",
    jobsSearch: "jobSearch",
    jobsSearchText: "jobSearchText",
    analyticsSkill: "analyticsSkill",
    bubbleChart: "bubbleChart",
    pieChart: "pieChart",
    signIn: "signin",
    register: "register",
    userProfile: "userProfile"
  },

  httpUri: {
    user: "user",
    userSave: "user/save",
    login: "login",
    logout: "logout",
    verifyUserLogin: "user/verifyUserLogin",
    getUserInfoByKey: "user/findByKey",
    searchTalent: "api/user/findTalent",
    talentProfile: "api/user/talentProfile",
    userRegister: "api/user/register",
    company: "company",
    userRegisterCount: "api/user/register/count"
  },

  socketUri: {
    sockjs: "ws",

    sendTerms: "/app/jobs/terms",
    subscribeTerms: "/topic/jobs/terms",
    subscribeTerm: "/topic/jobs/term/",

    sendJobsSearch: "/app/jobs/search",
    subscribeJobsSearch: "/topic/jobs/search",

    analyticsSkill: "/app/analytics/skill",
    subscribeAnalyticsSkill: "/topic/analytics/skill",

    getUserInfoByKey: "/app/user/findByKey",
    subscribeUserInfo: "/user/queue/info",
    subscribeUserRegistration: "/topic/api/user/register/count"
  },


  technicalSkill: [{
    'text': 'Java',
    'logo': 'lg-java.png'
  }, {
    'text': '.Net',
    'logo': 'lg-dotnet.png'
  }, {
    'text': 'C#',
    'logo': 'lg-c-sharp.png'
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
    'text': 'Html',
    'logo': 'lg-html.png'
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
    'text': 'ABAP',
    'logo': 'lg-sap.png'
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
  }, {
    'text': 'JavaScript',
    'logo': 'lg-javascript.png'
  }, {
    'text': 'C',
    'logo': 'lg-c.png'
  }, {
    'text': 'CSS',
    'logo': 'lg-css3.png'
  }, {
    'text': 'Shell',
    'logo': 'lg-shell.png'
  }, {
    'text': 'Shell',
    'logo': 'lg-shell.png'
  }, {
    'text': 'Clojure',
    'logo': 'lg-clojure.png'
  }, {
    'text': 'CoffeeScript',
    'logo': 'lg-coffeescript.png'
  }, {
    'text': 'Go',
    'logo': 'lg-go.png'
  }, {
    'text': 'Haskell',
    'logo': 'lg-haskell.png'
  }, {
    'text': 'Lua',
    'logo': 'lg-lua.png'
  }, {
    'text': 'Matlab',
    'logo': 'lg-matlab.png'
  }, {
    'text': 'Perl',
    'logo': 'lg-perl.png'
  }, {
    'text': 'R',
    'logo': 'lg-r.png'
  }, {
    'text': 'Scala',
    'logo': 'lg-scala.png'
  }, {
    'text': 'Swift',
    'logo': 'lg-swift.png'
  }, {
    'text': 'Clojure',
    'logo': 'lg-clojure.png'
  }, {
    'text': 'Tex',
    'logo': 'lg-tex.png'
  }, {
    'text': 'VimL',
    'logo': 'lg-viml.png'
  }, {
    'text': 'Objective-C',
    'logo': 'lg-objective-c.png'
  }, {
    'text': 'xml',
    'logo': 'lg-xml.png'
  }, {
    'text': 'Groovy',
    'logo': 'lg-groovy.png'
  }, {
    'text': 'Logos',
    'logo': 'lg-logos.png'
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

  authSource: [
    {
      provider: "vietnamworks",
      logo: "images/vietnamwork.png",
      magnitude: "level1",
      bgcolor: "#1fbaec",
      isNotSupported: true
    },
    {
      provider: "linkedin",
      logo: "images/linkedin.png",
      magnitude: "level1",
      bgcolor: "#007bb6"
    },
    {
      provider: "facebook",
      logo: "images/facebook.png",
      magnitude: "level3",
      bgcolor: "#3b5a9b"
    },
    {
      provider: "stackoverflow",
      logo: "images/stackoverflow.png",
      magnitude: "level4",
      bgcolor: "#fda428",
      isNotSupported: true
    },
    {
      provider: "google",
      "logo": "images/gmail.png",
      "magnitude": "level5",
      bgcolor: "#ac2e25"
    },
    {
      provider: "github",
      logo: "images/github.png",
      magnitude: "level7",
      bgcolor: "#333333"
    },
    {
      provider: "bitbucket",
      logo: "images/bitbucket.png",
      magnitude: "level7",
      bgcolor: "#1f5081",
      isNotSupported: true
    },
    {
      provider: "twitter",
      logo: "images/twitter.png",
      magnitude: "level8",
      bgcolor: "#33ccff"
    }
  ],

  cities: [
    {"text": "Vietnam"},
    {"text": "Ho Chi Minh"},
    {"text": "Ha Noi"},
    {"text": "Da Nang"},
    {"text": "Cambodia"},
    {"text": "Myanmar"},
    {"text": "Japan"},
    {"text": "Thailand"},
    {"text": "Singapore"},
    {"text": "Malaysia"},
    {"text": "Indonesia"},
    {"text": "Australia"},
    {"text": "China"},
    {"text": "India"},
    {"text": "Korea"},
    {"text": "Taiwan"},
    {"text": "Spain"},
    {"text": "Ukraine"},
    {"text": "Poland"},
    {"text": "Russia"},
    {"text": "Bulgaria"},
    {"text": "Turkey"},
    {"text": "Greece"},
    {"text": "Serbia"},
    {"text": "Romania"},
    {"text": "Belarus"},
    {"text": "Lithuania"},
    {"text": "Estonia"},
    {"text": "Italy"},
    {"text": "Portugal"},
    {"text": "Colombia"},
    {"text": "Brazil"},
    {"text": "Chile"},
    {"text": "Argentina"},
    {"text": "Venezuela"},
    {"text": "Bolivia"},
    {"text": "Mexico"}
  ],

  industries: [
    {
      "industryId": "1",
      "industryValue": "Accounting"
    },
    {
      "industryId": "2",
      "industryValue": "Administrative/Clerical"
    },
    {
      "industryId": "3",
      "industryValue": "Advertising/Promotion/PR"
    },
    {
      "industryId": "4",
      "industryValue": "Agriculture/Forestry"
    },
    {
      "industryId": "5",
      "industryValue": "Architecture/Interior Design"
    },
    {
      "industryId": "6",
      "industryValue": "Pharmaceutical/Biotech"
    },
    {
      "industryId": "7",
      "industryValue": "Civil/Construction"
    },
    {
      "industryId": "8",
      "industryValue": "Consulting"
    },
    {
      "industryId": "10",
      "industryValue": "Arts/Design"
    },
    {
      "industryId": "11",
      "industryValue": "Customer Service"
    },
    {
      "industryId": "12",
      "industryValue": "Education/Training"
    },
    {
      "industryId": "13",
      "industryValue": "Engineering"
    },
    {
      "industryId": "15",
      "industryValue": "Entry level"
    },
    {
      "industryId": "16",
      "industryValue": "Environment/Waste Services"
    },
    {
      "industryId": "17",
      "industryValue": "Executive management"
    },
    {
      "industryId": "18",
      "industryValue": "Expatriate Jobs in Vietnam"
    },
    {
      "industryId": "19",
      "industryValue": "Export-Import"
    },
    {
      "industryId": "21",
      "industryValue": "NGO/Non-Profit"
    },
    {
      "industryId": "22",
      "industryValue": "Health/Medical Care"
    },
    {
      "industryId": "23",
      "industryValue": "Human Resources"
    },
    {
      "industryId": "24",
      "industryValue": "Insurance"
    },
    {
      "industryId": "25",
      "industryValue": "Legal/Contracts"
    },
    {
      "industryId": "26",
      "industryValue": "Production/Process"
    },
    {
      "industryId": "27",
      "industryValue": "Marketing"
    },
    {
      "industryId": "28",
      "industryValue": "Oil/Gas"
    },
    {
      "industryId": "30",
      "industryValue": "Real Estate"
    },
    {
      "industryId": "32",
      "industryValue": "Retail/Wholesale"
    },
    {
      "industryId": "33",
      "industryValue": "Sales"
    },
    {
      "industryId": "34",
      "industryValue": "Sales Technical"
    },
    {
      "industryId": "35",
      "industryValue": "IT - Software"
    },
    {
      "industryId": "36",
      "industryValue": "Freight/Logistics"
    },
    {
      "industryId": "37",
      "industryValue": "Airlines/Tourism/Hotel"
    },
    {
      "industryId": "39",
      "industryValue": "Other"
    },
    {
      "industryId": "41",
      "industryValue": "Telecommunications"
    },
    {
      "industryId": "42",
      "industryValue": "Banking"
    },
    {
      "industryId": "43",
      "industryValue": "Chemical/Biochemical"
    },
    {
      "industryId": "47",
      "industryValue": "Interpreter/Translator"
    },
    {
      "industryId": "48",
      "industryValue": "TV/Media/Newspaper"
    },
    {
      "industryId": "49",
      "industryValue": "Purchasing/Supply Chain"
    },
    {
      "industryId": "51",
      "industryValue": "Temporary/Contract"
    },
    {
      "industryId": "52",
      "industryValue": "Textiles/Garments/Footwear"
    },
    {
      "industryId": "53",
      "industryValue": "Warehouse"
    },
    {
      "industryId": "54",
      "industryValue": "Food & Beverage"
    },
    {
      "industryId": "55",
      "industryValue": "IT - Hardware/Networking"
    },
    {
      "industryId": "56",
      "industryValue": "Securities & Trading"
    },
    {
      "industryId": "57",
      "industryValue": "Internet/Online Media"
    },
    {
      "industryId": "58",
      "industryValue": "Auditing"
    },
    {
      "industryId": "59",
      "industryValue": "Finance/Investment"
    },
    {
      "industryId": "62",
      "industryValue": "Luxury Goods"
    },
    {
      "industryId": "63",
      "industryValue": "Fashion/Lifestyle"
    },
    {
      "industryId": "64",
      "industryValue": "Electrical/Electronics"
    },
    {
      "industryId": "65",
      "industryValue": "Mechanical"
    },
    {
      "industryId": "66",
      "industryValue": "High Technology"
    },
    {
      "industryId": "67",
      "industryValue": "Automotive"
    },
    {
      "industryId": "68",
      "industryValue": "Industrial Products"
    },
    {
      "industryId": "69",
      "industryValue": "Planning/Projects"
    },
    {
      "industryId": "70",
      "industryValue": "QA/QC"
    },
    {
      "industryId": "71",
      "industryValue": "Overseas Jobs"
    }
  ],

  "companySizes" : [
      {
        "companySizeId":"1",
        "companySizeValue":"Less Than 10"
      },
      {
        "companySizeId":"2",
        "companySizeValue":"10-24"
      },
      {
        "companySizeId":"3",
        "companySizeValue":"25-99"
      },
      {
        "companySizeId":"4",
        "companySizeValue":"100-499"
      },
      {
        "companySizeId":"5",
        "companySizeValue":"500-999"
      },
      {
        "companySizeId":"6",
        "companySizeValue":"1,000-4,999"
      },
      {
        "companySizeId":"7",
        "companySizeValue":"5,000-9,999"
      },
      {
        "companySizeId":"8",
        "companySizeValue":"10,000-19,999"
      },
      {
        "companySizeId":"9",
        "companySizeValue":"20,000-49,999"
      },
      {
        "companySizeId":"10",
        "companySizeValue":"Over 50,000"
      }
  ],

  introTour: {
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
    ]
  }
});