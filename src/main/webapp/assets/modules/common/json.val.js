techlooper.factory("jsonValue", function () {
  var instance = {
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
        color: "#76b4f0"
      }, {
        term: "DOTNET",
        label: ".Net",
        color: "#5a9dc5"
      }, {
        term: "PHP",
        label: "Php",
        color: "#c3c0c0"
      }, {
        term: "RUBY",
        label: "Ruby",
        color: "#ffa152"
      }, {
        term: "PYTHON",
        label: "Python",
        color: "#f8bd81"
      }, {
        term: "PROJECT_MANAGER",
        label: "Project Manager",
        color: "#9ea0d3"
      }, {
        term: "DBA",
        label: "Database Administrator",
        color: "#999966"
      }, {
        term: "QA",
        label: "Quality Assurance",
        color: "#dff48b"
      }, {
        term: "BA",
        label: "Business Analyst",
        color: "#ffa2fc"
      }, {
        term: "CPLUSPLUS",
        label: "C/C++",
        color: "#939191"
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

    termColor: [
      {
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
      }
    ],

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
      hiring: "/hiring",
      talentSearchResult: "/talent-search-result",
      talentProfile: "/talent-profile",
      companyProfile: "/companies",
      technicalDetail: "/technical-detail",
      salarySharing: "/salary-sharing",
      salaryReport: "/salary-report",
      salaryReview: "/salary-review",
      getPromoted: "/get-promoted",
      pie: "/pie-chart",
      bubble: "/bubble-chart",
      jobsSearch: "/jobs/search",
      analyticsSkill: "/analytics/skill",
      signIn: "/signin",
      register: "/register",
      userProfile: "/user",
      priceJob: "/price-job",
      contest: "contest",
      challengeDetail: "/challenge-detail",
      challenges: "/challenges",
      postChallenge: "/post-challenge",
      freelancerPostProject: "/freelancer/post-project",
      freelancerProjectDetail: "/freelancer/project-detail",
      freelancerProjects: "/freelancer/projects",
      whyFreelancer: "/freelancer/whyFreelancer",
      emailSetting: "/email-setting"
    },

    views: {
      landing: "landing",
      home: "home",
      hiring: "hiring",
      talentSearchResult: "talentSearchResult",
      talentProfile: "talentProfile",
      companyProfile: "companyProfile",
      jobsSearch: "jobSearch",
      jobsSearchText: "jobSearchText",
      analyticsSkill: "analyticsSkill",
      technicalDetail: "technicalDetail",
      salarySharing: "salarySharing",
      salaryReport: "salaryReport",
      salaryReview: "salaryReview",
      bubbleChart: "bubbleChart",
      pieChart: "pieChart",
      signIn: "signin",
      register: "register",
      userProfile: "userProfile",
      priceJob: "priceJob",
      getPromoted: "getPromoted",
      contest: "contest",
      postChallenge: "postChallenge",
      login: "login",
      challengeDetail: "challengeDetail",
      challenges: "challenges",
      freelancerPostProject: "freelancerPostProject",
      freelancerProjectDetail: "freelancerProjectDetail",
      freelancerProjects: "freelancerProjects",
      whyFreelancer: "whyFreelancer",
      whyChallenge: "whyChallenge",
      employerDashboard: "employerDashboard",
      userType: "userType",
      howItWorks: "howItWorks",
      jobListing: "jobListing",
      createEvent: "createEvent",
      events: "events",
      eventDetails: "eventDetails",
      topics: "topics",
      notFound: "notFound",
      emailSetting: "emailSetting"
    },

    uiViews: [
      {
        name: "rootPage",
        url: "/"
      },
      {
        name: "home",
        url: "/home",
        ignoreIfLastFoot: true
      },
      {
        name: "employerDashboard",
        url: "/employer-dashboard",
        roles: ["EMPLOYER"],
        loginUrl: "/login",
        ignoreIfLastFoot: true
      },
      {
        name: "emailSetting",
        url: "/email-setting",
        roles: ["EMPLOYER"],
        loginUrl: "/login"
      },
      {
        name: "postEvent",
        url: "/post-event",
        roles: ["EMPLOYER", "JOB_SEEKER"],
        loginUrl: "/user-type"
      },
      {
        name: "freelancerPostProject",
        url: "/freelancer/post-project",
        roles: ["EMPLOYER"],
        loginUrl: "/login"
      },
      {
        name: "postChallenge",
        url: "/post-challenge",
        roles: ["EMPLOYER"],
        loginUrl: "/login"
      },
      {
        name: "hiring",
        url: "/hiring",
        header: "EMPLOYER"
      },
      {
        name: "whyChallenge",
        url: "/why-challenge",
        header: "EMPLOYER"
      },
      {
        name: "whyFreelancer",
        url: "/freelancer/why-freelancer",
        header: "EMPLOYER"
      },
      {
        name: "priceJob",
        url: "/price-job",
        header: "EMPLOYER"
      },
      {
        name: "login",
        url: "/login",
        type: "LOGIN"
      },
      {
        name: "userType",
        url: "/user-type",
        type: "LOGIN"
      },
      {
        name: "loading",
        url: "/loading",
        type: "LOGIN"
      },
      {
        name: "eventDetails",
        url: "/event-detail",
        regex: /\/event-detail\//i,
        type: "SEO"
      },
      {
        name: "challengeDetail",
        url: "/challenge-detail",
        regex: /\/challenge-detail\//i,
        requireEmployerByParams: ["toPhase", "a"],
        loginUrl: "/login",
        type: "SEO"
      },
      {
        name: "freelancerProjectDetail",
        url: "/freelancer/project-detail",
        regex: /\/freelancer\/project-detail\//i,
        type: "SEO"
      }
    ],

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
      termStatistic: "term/statistic",
      salaryReview: "salaryReview",
      getPromoted: "getPromoted",
      contest: "contest",
      challenges: "challenges",
      whyFreelancer: "whyFreelancer",
      whyChallenge: "whyChallenge",
      employerDashboard: "employerDashboard",
      userType: "userType",
      howItWorks: "howItWorks",
      jobListing: "jobListing",
      createEvent: "createEvent",
      events: "events",
      eventDetails: "eventDetails",
      notFound: "notFound"
    },

    socketUri: {
      sockjs: "ws",

      sendTerms: "/app/jobs/terms",
      subscribeTerms: "/topic/jobs/terms",
      subscribeTerm: "/topic/jobs/term/",

      sendJobsSearch: "/app/jobs/search",
      sendSearchJobAlert: "/app/jobs/searchJobAlert",
      createSearchJobAlert: "/app/jobs/createJobAlert",
      subscribeJobsSearch: "/topic/jobs/search",
      subscribeSearchJobAlert: "/topic/jobs/searchJobAlert",

      analyticsSkill: "/app/analytics/skill",
      subscribeAnalyticsSkill: "/topic/analytics/skill",

      getUserInfoByKey: "/app/user/findByKey",
      subscribeUserInfo: "/user/queue/info",
      subscribeUserRegistration: "/topic/api/user/register/count",
      subscribeTermStatistic: "/topic/analytics/term"
    },

    technicalSkill: [{
      term: "JAVA",
      'text': 'Java',
      'logo': 'lg-java.png'
    }, {
      term: "MOBILE",
      'text': 'Mobile',
      'logo': 'lg-mobile.png'
    }, {
      term: "PROJECT_MANAGER",
      'text': 'PM',
      'logo': 'lg-pm.png'
    }, {
      term: "DOTNET",
      'text': '.Net',
      'logo': 'lg-dotnet.png'
    }, {
      'text': 'C#',
      'logo': 'lg-c-sharp.png'
    }, {
      term: "PHP",
      'text': 'Php',
      'logo': 'lg-php.png'
    }, {
      term: "RUBY",
      'text': 'Ruby',
      'logo': 'lg-ruby.png'
    }, {
      term: "QA",
      'text': 'Qa',
      'logo': 'lg-qa.png'
    }, {
      term: "BA",
      'text': 'Ba',
      'logo': 'lg-ba.png'
    }, {
      term: "PYTHON",
      'text': 'Python',
      'logo': 'lg-python.png'
    }, {
      term: "CPLUSPLUS",
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
      term: "DBA",
      'text': 'DB',
      'logo': 'lg-db.png'
    }, {
      term: "WEB",
      'text': 'Web',
      'logo': 'lg-web.png'
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
      "35": {"value": "IT - Software"},
      "1": {"value": "Accounting"},
      "2": {"value": "Administrative/Clerical"},
      "3": {"value": "Advertising/Promotion/PR"},
      "4": {"value": "Agriculture/Forestry"},
      "5": {"value": "Architecture/Interior Design"},
      "6": {"value": "Pharmaceutical/Biotech"},
      "7": {"value": "Civil/Construction"},
      "8": {"value": "Consulting"},
      "10": {"value": "Arts/Design"},
      "100": {"value": "Customer Service"},
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

    genders: [
      {id: 1, translate: "genderMale"},
      {id: 2, translate: "genderFemale"}
    ],
    hours: [
      {id: 1, value: "12:00 AM"},
      {id: 2, value: "12:30 AM"},
      {id: 3, value: "01:00 AM"},
      {id: 4, value: "01:30 AM"},
      {id: 5, value: "02:00 AM"},
      {id: 6, value: "02:30 AM"},
      {id: 7, value: "03:00 AM"},
      {id: 8, value: "03:30 AM"},
      {id: 9, value: "04:00 AM"},
      {id: 10, value: "04:30 AM"},
      {id: 11, value: "05:00 AM"},
      {id: 12, value: "05:30 AM"},
      {id: 13, value: "06:00 AM"},
      {id: 14, value: "06:30 AM"},
      {id: 15, value: "07:00 AM"},
      {id: 16, value: "07:30 AM"},
      {id: 17, value: "08:00 AM"},
      {id: 18, value: "08:30 AM"},
      {id: 19, value: "09:00 AM"},
      {id: 20, value: "09:30 AM"},
      {id: 21, value: "10:00 AM"},
      {id: 22, value: "10:30 AM"},
      {id: 23, value: "11:00 AM"},
      {id: 24, value: "11:30 AM"},
      {id: 25, value: "12:00 PM"},
      {id: 26, value: "12:30 PM"},
      {id: 27, value: "01:00 PM"},
      {id: 28, value: "01:30 PM"},
      {id: 29, value: "02:00 PM"},
      {id: 30, value: "02:30 PM"},
      {id: 31, value: "03:00 PM"},
      {id: 32, value: "03:30 PM"},
      {id: 33, value: "04:00 PM"},
      {id: 34, value: "04:30 PM"},
      {id: 35, value: "05:00 PM"},
      {id: 36, value: "05:30 PM"},
      {id: 37, value: "06:00 PM"},
      {id: 38, value: "06:30 PM"},
      {id: 39, value: "07:00 PM"},
      {id: 40, value: "07:30 PM"},
      {id: 41, value: "08:00 PM"},
      {id: 42, value: "08:30 PM"},
      {id: 43, value: "09:00 PM"},
      {id: 44, value: "09:30 PM"},
      {id: 45, value: "10:00 PM"},
      {id: 46, value: "10:30 PM"},
      {id: 47, value: "11:00 PM"},
      {id: 48, value: "11:30 PM"}
    ],
    timeToSends: [
      {id: 2, translate: "day"},
      {id: 3, translate: "week"}
    ],
    languages: {
      undefined: 2,
      en: 2,
      vi: 1
    },

    jobLevels: [
      {id: -1, name: "ALL", translate: "allLevel"},
      {id: 1, name: "ENTRY", translate: "newGradLevel", ids: [1], alertId: 1},
      {id: 5, name: "EXPERIENCED", translate: "experienced", ids: [5, 6], alertId: 5},
      {id: 7, name: "MANAGER", translate: "manager", ids: [7], alertId: 7},
      {id: 10, name: "DIRECTOR_PLUS", translate: "directorAndAbove", ids: [10, 3, 4, 8, 9], alertId: 3}
    ],

    locations: [
      {id: -1, name: "Any location"},
      {id: 29, name: "Ho Chi Minh"},
      {id: 24, name: "Ha Noi"},
      {id: 17, name: "Da Nang"},
      {id: 2, name: "An Giang"},
      {id: 3, name: "Ba Ria - Vung Tau"},
      {id: 4, name: "Bac Can"},
      {id: 5, name: "Bac Giang"},
      {id: 6, name: "Bac Lieu"},
      {id: 7, name: "Bac Ninh"},
      {id: 8, name: "Ben Tre"},
      {id: 9, name: "Bien Hoa"},
      {id: 10, name: "Binh Dinh"},
      {id: 11, name: "Binh Duong"},
      {id: 12, name: "Binh Phuoc"},
      {id: 13, name: "Binh Thuan"},
      {id: 14, name: "Ca Mau"},
      {id: 15, name: "Can Tho"},
      {id: 16, name: "Cao Bang"},
      {id: 18, name: "Dac Lac"},
      {id: 19, name: "Dong Nai"},
      {id: 20, name: "Dong Thap"},
      {id: 21, name: "Gia Lai"},
      {id: 22, name: "Ha Giang"},
      {id: 23, name: "Ha Nam"},
      {id: 25, name: "Ha Tay"},
      {id: 26, name: "Ha Tinh"},
      {id: 27, name: "Hai Duong"},
      {id: 28, name: "Hai Phong"},
      {id: 30, name: "Hoa Binh"},
      {id: 31, name: "Hue"},
      {id: 32, name: "Hung Yen"},
      {id: 33, name: "Khanh Hoa"},
      {id: 34, name: "Kon Tum"},
      {id: 35, name: "Lai Chau"},
      {id: 36, name: "Lam Dong"},
      {id: 37, name: "Lang Son"},
      {id: 38, name: "Lao Cai"},
      {id: 39, name: "Long An"},
      {id: 40, name: "Nam Dinh"},
      {id: 41, name: "Nghe An"},
      {id: 42, name: "Ninh Binh"},
      {id: 43, name: "Ninh Thuan"},
      {id: 44, name: "Phu Tho"},
      {id: 45, name: "Phu Yen"},
      {id: 46, name: "Quang Binh"},
      {id: 47, name: "Quang Nam"},
      {id: 48, name: "Quang Ngai"},
      {id: 49, name: "Quang Ninh"},
      {id: 50, name: "Quang Tri"},
      {id: 51, name: "Soc Trang"},
      {id: 52, name: "Son La"},
      {id: 53, name: "Tay Ninh"},
      {id: 54, name: "Thai Binh"},
      {id: 55, name: "Thai Nguyen"},
      {id: 56, name: "Thanh Hoa"},
      {id: 57, name: "Thua Thien-Hue"},
      {id: 58, name: "Tien Giang"},
      {id: 59, name: "Tra Vinh"},
      {id: 60, name: "Tuyen Quang"},
      {id: 61, name: "Kien Giang"},
      {id: 62, name: "Vinh Long"},
      {id: 63, name: "Vinh Phuc"},
      {id: 65, name: "Yen Bai"},
      {id: 69, name: "Dien Bien"},
      {id: 70, name: "International"},
      {id: 71, name: "Mekong Delta"},
      {id: 72, name: "Hau Giang"}
    ],
    "languagesJob": [
      {id: 38, name: "Vietnamese"},
      {id: 12, name: "English"},
      {id: 21, name: "Japanese"},
      {id: 1, name: "Arabic"},
      {id: 2, name: "Bengali"},
      {id: 3, name: "Bulgarian"},
      {id: 4, name: "Burmese"},
      {id: 5, name: "Cambodian"},
      {id: 6, name: "Cebuano"},
      {id: 7, name: "Chinese (Cantonese)"},
      {id: 8, name: "Chinese (Mandarin)"},
      {id: 9, name: "Czech"},
      {id: 10, name: "Danish"},
      {id: 11, name: "Dutch"},
      {id: 13, name: "Finnish"},
      {id: 14, name: "French"},
      {id: 15, name: "German"},
      {id: 16, name: "Greek"},
      {id: 17, name: "Hindi"},
      {id: 18, name: "Hungarian"},
      {id: 19, name: "Indonesian"},
      {id: 20, name: "Italian"},
      {id: 22, name: "Javanese"},
      {id: 23, name: "Korean"},
      {id: 24, name: "Laotian"},
      {id: 25, name: "Malay"},
      {id: 26, name: "Norwegian"},
      {id: 27, name: "Polish"},
      {id: 28, name: "Portuguese"},
      {id: 29, name: "Romanian"},
      {id: 30, name: "Russian"},
      {id: 31, name: "Spanish"},
      {id: 32, name: "Swedish"},
      {id: 33, name: "Tagolog"},
      {id: 34, name: "Taiwanese"},
      {id: 35, name: "Thai"},
      {id: 36, name: "Turkish"},
      {id: 37, name: "Ukranian"},
      {id: 39, name: "Other"}
    ],
    "educationLevel": [
      {id: 12, name: "Doctorate"},
      {id: 9, name: "Master"},
      {id: 3, name: "Degree"},
      {id: 4, name: "Diploma"},
      {id: 2, name: "High school"},
      {id: 15, name: "Primary School"},
      {id: 1, name: "None"},
      {id: 15, name: "Others"}
    ],
    "yearsOfExperience": [
      {id: 1, name: "New to workforce"},
      {id: 2, name: "Less than one year"},
      {id: 3, name: "1 year"},
      {id: 4, name: "2 years"},
      {id: 5, name: "3 years"},
      {id: 6, name: "4 years"},
      {id: 7, name: "5 years"},
      {id: 8, name: "6 years"},
      {id: 9, name: "7 years"},
      {id: 10, name: "8 years"},
      {id: 11, name: "9 years"},
      {id: 12, name: "10 years"},
      {id: 12, name: "11 years"},
      {id: 12, name: "12 years"},
      {id: 12, name: "13 years"},
      {id: 12, name: "14 years"},
      {id: 12, name: "15 years"},
      {id: 12, name: "16 years"},
      {id: 12, name: "17 years"},
      {id: 12, name: "18 years"},
      {id: 12, name: "19 years"},
      {id: 12, name: "> 20 years"}
    ],
    "benefitIcons": [
      {id: '1', iconClass: 'fa-dollar'},
      {id: '2', iconClass: 'fa-user-md'},
      {id: '3', iconClass: 'fa-file-image-o'},
      {id: '4', iconClass: 'fa-graduation-cap'},
      {id: '5', iconClass: 'fa-trophy'},
      {id: '6', iconClass: 'fa-book'},
      {id: '7', iconClass: 'fa-laptop'},
      {id: '8', iconClass: 'fa-mobile'},
      {id: '9', iconClass: 'fa-plane'},
      {id: '10', iconClass: 'fa-glass'},
      {id: '11', iconClass: 'fa-cab'},
      {id: '12', iconClass: 'fa-coffee'},
      {id: '13', iconClass: 'fa-gift'},
      {id: '14', iconClass: 'fa-child'},
      {id: '15', iconClass: 'fa-check-square-o'}
    ],
    "companyPromotion": {
      "title": "companyTitle",
      "tagLine": "companyMessages",
      "images": "/images/banner-citibank.png",
      "minSalary": 10000000,
      "AcceptedCity": [24, 29]
    },

    postContestConfig: {
      technologies: {
        required: true
      }
    },

    dateFormat: "DD/MM/YYYY",
    dateTimeFormat: "DD/MM/YYYY hh:mm A",

    status: {
      notStarted: {id: "notStarted", translate: "notStart", timeLeftTranslate: "moreDayToNotStarted"},
      registration: {id: "registration", translate: "registration", timeLeftTranslate: "moreDayToRegistration"},
      progress: {id: "progress", translate: "inProgress", timeLeftTranslate: "moreDayToSubmit"},
      closed: {id: "closed", translate: "closed", timeLeftTranslate: "moreDayToClosed"}
    },

    feedbackStatus: {
      cannotSendMail: {id: "cannotSendMail", translate: "cannotSendMail"},
      errorSystem: {id: "errorSystem", translate: "errorSystem"}
    },
    "crawlSources": [
      {id: "vietnamworks", name: "VIETNAMWORKS"},
      {id: "careerbuilder-jobs-api-pagination", name: "CAREERBUILDER"},
      {id: "jobstreet-jobs-api-pagination", name: "JOBSTREET"},
      {id: "tuyendung-jobs-api-pagination", name: "TUYENDUNG"},
      {id: "itviec-jobs-api-pagination", name: "ITVIEC"},
      {id: "careerlink-jobs-api-pagination", name: "CAREERLINK"},
      {id: "vieclam24h-jobs-api-pagination", name: "VIECLAM24H"},
      {id: "linkedin-jobs-api-pagination", name: "LINKEDIN"},
      {id: "vietnamworks-jobs-api", name: "VIETNAMWORKS"},
      {id: "jobstreet-jobs-api", name: "JOBSTREET"},
      {id: "careerbuilder-jobs-api", name: "CAREERBUILDER"},
      {id: "itviec-jobs-api", name: "ITVIEC"},
      {id: "itviec-jobs-api-v2", name: "ITVIEC"}
    ],

    //@see com.techlooper.model.ChallengePhaseEnum
    challengePhase: {
      values: [
        {
          enum: "REGISTRATION",
          challengeProp: "registrationDateTime",
          phaseItem: {translate: {countJoiner: "registrantsNumberPhase", countSubmission: "submissionsNumber", countUnread: "unReadNumber"}},
          registrantTable: {templateUrl: "modules/contest-detail/registrants/phaseRegistrationTable.html"},
          isRegistration: true
        },
        {
          enum: "IDEA",
          challengeProp: "ideaSubmissionDateTime",
          phaseItem: {translate: {countJoiner: "participantsNumber", countSubmission: "submissionsNumber", countUnread: "unReadNumber"}},
          registrantTable: {templateUrl: "modules/contest-detail/registrants/phaseIdeaTable.html"},
          isIdea: true
        },
        {
          enum: "UIUX",
          challengeProp: "uxSubmissionDateTime",
          phaseItem: {translate: {countJoiner: "participantsNumber", countSubmission: "submissionsNumber", countUnread: "unReadNumber"}},
          registrantTable: {templateUrl: "modules/contest-detail/registrants/phaseIdeaTable.html"},
          isUiux: true
        },
        {
          enum: "PROTOTYPE",
          challengeProp: "prototypeSubmissionDateTime",
          phaseItem: {translate: {countJoiner: "participantsNumber", countSubmission: "submissionsNumber", countUnread: "unReadNumber"}},
          registrantTable: {templateUrl: "modules/contest-detail/registrants/phaseIdeaTable.html"},
          isPrototype: true
        },
        {
          enum: "FINAL",
          challengeProp: "submissionDateTime",
          phaseItem: {translate: {countJoiner: "participantsNumber", countSubmission: "submissionsNumber", countUnread: "unReadNumber"}},
          registrantTable: {templateUrl: "modules/contest-detail/registrants/phaseFinalTable.html"},
          isFinal: true
        },
        {
          enum: "WINNER",
          phaseItem: {translate: {countJoiner: "finalistNumber", countSubmission: "winnersNumber", countUnread: "unReadNumber"}},
          registrantTable: {templateUrl: "modules/contest-detail/registrants/phaseWinnerTable.html"},
          isSpecial: true,
          isWinner: true
        }
      ]
    },

    summerNoteConfig: {
      height: 150,
      toolbar: [
        ['fontface', ['fontname']],
        ['textsize', ['fontsize']],
        ['style', ['bold', 'italic', 'underline', 'strikethrough']],
        ['fontclr', ['color']],
        ['alignment', ['ul', 'ol', 'paragraph', 'lineheight']],
        ['height', ['height']],
        ['table', ['table']],
        ['insert', ['link', 'hr']],
        ['view', ['fullscreen']]
      ]
    },

    /**
     * @see com.techlooper.model.RewardEnum
     * */
    rewards: {
      values: [
        {enum: "FIRST_PLACE"},
        {enum: "SECOND_PLACE"},
        {enum: "THIRD_PLACE"}
      ],

      firstPlaceEnum: function () {
        return instance.rewards.values[0].enum;
      },

      secondPlaceEnum: function () {
        return instance.rewards.values[1].enum;
      },

      thirdPlaceEnum: function () {
        return instance.rewards.values[2].enum;
      }
    }
  }

  $.each(instance.challengePhase.values, function (i, r) {instance.challengePhase[r.enum] = r;});

  instance.companySizesArray = $.map(instance.companySizes, function (value, key) {
    return {id: parseInt(key), size: value.value}
  });

  instance.industriesArray = $.map(instance.industries, function (value, key) {
    return {id: parseInt(key), name: value.value};
  });

  instance.jobLevelsMap = {};
  $.each(instance.jobLevels, function (i, jobLevel) {instance.jobLevelsMap[jobLevel.id] = jobLevel;});

  instance.yearsOfExperienceMap = {};
  $.each(instance.yearsOfExperience, function (i, item) {instance.yearsOfExperienceMap[item.name] = item;});

  instance.locationsMap = {};
  $.each(instance.locations, function (i, location) {instance.locationsMap[location.id] = location;});

  var currentYear = new Date().getFullYear();//yrs old from 15 to 99
  instance.yobs = Array.apply(0, Array(84)).map(function (x, y) { return {value: currentYear - (y + 15)}; });

  instance.benefitIconsMap = {};
  $.each(instance.benefitIcons, function (i, icon) {instance.benefitIconsMap[icon.id] = icon;});

  instance.crawlSourcesMap = {};
  $.each(instance.crawlSources, function (i, crawlSource) {instance.crawlSourcesMap[crawlSource.id] = crawlSource;});

  return instance;
});