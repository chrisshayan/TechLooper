angular.module("Common").constant("jsonValue", {

  pieChartType: {
    salary: "SALARY",
    job: "JOB"
  },

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
      color: "#92c3f2"
    }, {
      term: "DOTNET",
      label: ".Net",
      color: "#6baed6"
    }, {
      term: "PHP",
      label: "Php",
      color: "#d9d9d9"
    }, {
      term: "RUBY",
      label: "Ruby",
      color: "#fdae6b"
    }, {
      term: "PYTHON",
      label: "Python",
      color: "#fdd0a2"
    }, {
      term: "PROJECT_MANAGER",
      label: "Project Manager",
      color: "#bcbddc"
    }, {
      term: "DBA",
      label: "DBA",
      color: "#d9f3d4"
    }, {
      term: "QA",
      label: "QA",
      color: "#c6dbef"
    }, {
      term: "BA",
      label: "BA",
      color: "#f2c56c"
    }, {
      term: "CPLUSPLUS",
      label: "C++",
      color: "#bdbdbd"
    },
      {
        term: "WEB",
        label: "Web Development",
        color: "#dadaeb"
      },
      {
        term: "MOBILE",
        label: "Mobile Development",
        color: "#a1d99b"
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
    companyProfile: "/companies",
    technicalDetail: "/technical-detail",
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
    technicalDetail: "technicalDetail",
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
    companyId: "company/id",
    userRegisterCount: "api/user/register/count",
    termStatistic: "term/statistic"
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
    subscribeUserRegistration: "/topic/api/user/register/count",
    subscribeTermStatistic: "/topic/analytics/term"
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
    'logo': 'lg-css.png'
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

  industries: {
    "1": {"value": "Accounting"},
    "2": {"value": "Administrative/Clerical"},
    "3": {"value": "Advertising/Promotion/PR"},
    "4": {"value": "Agriculture/Forestry"},
    "5": {"value": "Architecture/Interior Design"},
    "6": {"value": "Pharmaceutical/Biotech"},
    "7": {"value": "Civil/Construction"},
    "8": {"value": "Consulting"},
    "10": {"value": "Arts/Design"},
    "11": {"value": "Customer Service"},
    "12": {"value": "Education/Training"},
    "13": {"value": "Engineering"},
    "15": {"value": "Entry level"},
    "16": {"value": "Environment/Waste Services"},
    "17": {"value": "Executive management"},
    "18": {"value": "Expatriate Jobs in Vietnam"},
    "19": {"value": "Export-Import"},
    "21": {"value": "NGO/Non-Profit"},
    "22": {"value": "Health/Medical Care"},
    "23": {"value": "Human Resources"},
    "24": {"value": "Insurance"},
    "25": {"value": "Legal/Contracts"},
    "26": {"value": "Production/Process"},
    "27": {"value": "Marketing"},
    "28": {"value": "Oil/Gas"},
    "30": {"value": "Real Estate"},
    "32": {"value": "Retail/Wholesale"},
    "33": {"value": "Sales"},
    "34": {"value": "Sales Technical"},
    "35": {"value": "IT - Software"},
    "36": {"value": "Freight/Logistics"},
    "37": {"value": "Airlines/Tourism/Hotel"},
    "39": {"value": "Other"},
    "41": {"value": "Telecommunications"},
    "42": {"value": "Banking"},
    "43": {"value": "Chemical/Biochemical"},
    "47": {"value": "Interpreter/Translator"},
    "48": {"value": "TV/Media/Newspaper"},
    "49": {"value": "Purchasing/Supply Chain"},
    "51": {"value": "Temporary/Contract"},
    "52": {"value": "Textiles/Garments/Footwear"},
    "53": {"value": "Warehouse"},
    "54": {"value": "Food & Beverage"},
    "55": {"value": "IT - Hardware/Networking"},
    "56": {"value": "Securities & Trading"},
    "57": {"value": "Internet/Online Media"},
    "58": {"value": "Auditing"},
    "59": {"value": "Finance/Investment"},
    "62": {"value": "Luxury Goods"},
    "63": {"value": "Fashion/Lifestyle"},
    "64": {"value": "Electrical/Electronics"},
    "65": {"value": "Mechanical"},
    "66": {"value": "High Technology"},
    "67": {"value": "Automotive"},
    "68": {"value": "Industrial Products"},
    "69": {"value": "Planning/Projects"},
    "70": {"value": "QA/QC"},
    "71": {"value": "Overseas Jobs"}
  },

  "companySizes": {
    "1": {"value": "Less Than 10"},
    "2": {"value": "10-24"},
    "3": {"value": "25-99"},
    "4": {"value": "100-499"},
    "5": {"value": "500-999"},
    "6": {"value": "1,000-4,999"},
    "7": {"value": "5,000-9,999"},
    "8": {"value": "10,000-19,999"},
    "9": {"value": "20,000-49,999"},
    "10": {"value": "Over 50,000"}
  },

  "benefits": {
    "1": {"name": "Bonus", "iconName": "fa-dollar"},
    "2": {"name": "Healthcare Plan", "iconName": "fa-user-md"},
    "3": {"name": "Paid Leave", "iconName": "fa-file-image-o"},
    "4": {"name": "Training", "iconName": "fa-graduation-cap"},
    "5": {"name": "Awards", "iconName": "fa-trophy"},
    "6": {"name": "Library", "iconName": "fa-book"},
    "7": {"name": "Laptop", "iconName": "fa-laptop"},
    "8": {"name": "Mobile", "iconName": "fa-mobile"},
    "9": {"name": "Travel Opportunities", "iconName": "fa-plane"},
    "10": {"name": "Team Activities", "iconName": "fa-glass"},
    "11": {"name": "Transportation", "iconName": "fa-cab"},
    "12": {"name": "Canteen", "iconName": "fa-coffee"},
    "13": {"name": "Vouchers", "iconName": "fa-gift"},
    "14": {"name": "Kindergarten", "iconName": "fa-child"},
    "15": {"name": "Others", "iconName": "fa-check-square-o"}
  },

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