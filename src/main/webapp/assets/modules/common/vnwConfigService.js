techlooper.factory("vnwConfigService", function (jsonValue, $translate, $rootScope, utils) {

  var translateConfigBase = {
    valueField: 'id',
    labelField: 'translate',
    delimiter: '|',
    maxItems: 1,
    searchField: ['translate']
  }

  var vnwLang = $translate.use() === "en" ? "en" : "vn";

  var locations = [
    {
      "location_id": "29",
      "lang_vn": "Hồ Chí Minh",
      "lang_en": "Ho Chi Minh"
    },
    {
      "location_id": "24",
      "lang_vn": "Hà Nội",
      "lang_en": "Ha Noi"
    },
    {
      "location_id": "71",
      "lang_vn": "ĐBSCL",
      "lang_en": "Mekong Delta"
    },
    {
      "location_id": "2",
      "lang_vn": "An Giang",
      "lang_en": "An Giang"
    },
    {
      "location_id": "3",
      "lang_vn": "Bà Rịa - Vũng Tàu",
      "lang_en": "Ba Ria - Vung Tau"
    },
    {
      "location_id": "4",
      "lang_vn": "Bắc cạn",
      "lang_en": "Bac Can"
    },
    {
      "location_id": "5",
      "lang_vn": "Bắc Giang",
      "lang_en": "Bac Giang"
    },
    {
      "location_id": "6",
      "lang_vn": "Bạc Liêu",
      "lang_en": "Bac Lieu"
    },
    {
      "location_id": "7",
      "lang_vn": "Bắc Ninh",
      "lang_en": "Bac Ninh"
    },
    {
      "location_id": "8",
      "lang_vn": "Bến Tre",
      "lang_en": "Ben Tre"
    },
    {
      "location_id": "9",
      "lang_vn": "Biên Hòa",
      "lang_en": "Bien Hoa"
    },
    {
      "location_id": "10",
      "lang_vn": "Bình Định",
      "lang_en": "Binh Dinh"
    },
    {
      "location_id": "11",
      "lang_vn": "Bình Dương",
      "lang_en": "Binh Duong"
    },
    {
      "location_id": "12",
      "lang_vn": "Bình Phước",
      "lang_en": "Binh Phuoc"
    },
    {
      "location_id": "13",
      "lang_vn": "Bình Thuận",
      "lang_en": "Binh Thuan"
    },
    {
      "location_id": "14",
      "lang_vn": "Cà Mau",
      "lang_en": "Ca Mau"
    },
    {
      "location_id": "15",
      "lang_vn": "Cần Thơ",
      "lang_en": "Can Tho"
    },
    {
      "location_id": "16",
      "lang_vn": "Cao Bằng",
      "lang_en": "Cao Bang"
    },
    {
      "location_id": "17",
      "lang_vn": "Đà Nẵng",
      "lang_en": "Da Nang"
    },
    {
      "location_id": "18",
      "lang_vn": "Đắc Lắc",
      "lang_en": "Dac Lac"
    },
    {
      "location_id": "69",
      "lang_vn": "Điện Biên",
      "lang_en": "Dien Bien"
    },
    {
      "location_id": "19",
      "lang_vn": "Đồng Nai",
      "lang_en": "Dong Nai"
    },
    {
      "location_id": "20",
      "lang_vn": "Đồng Tháp",
      "lang_en": "Dong Thap"
    },
    {
      "location_id": "21",
      "lang_vn": "Gia Lai",
      "lang_en": "Gia Lai"
    },
    {
      "location_id": "22",
      "lang_vn": "Hà Giang",
      "lang_en": "Ha Giang"
    },
    {
      "location_id": "23",
      "lang_vn": "Hà Nam",
      "lang_en": "Ha Nam"
    },
    {
      "location_id": "25",
      "lang_vn": "Hà Tây",
      "lang_en": "Ha Tay"
    },
    {
      "location_id": "26",
      "lang_vn": "Hà Tĩnh",
      "lang_en": "Ha Tinh"
    },
    {
      "location_id": "27",
      "lang_vn": "Hải Dương",
      "lang_en": "Hai Duong"
    },
    {
      "location_id": "28",
      "lang_vn": "Hải Phòng",
      "lang_en": "Hai Phong"
    },
    {
      "location_id": "30",
      "lang_vn": "Hòa Bình",
      "lang_en": "Hoa Binh"
    },
    {
      "location_id": "31",
      "lang_vn": "Huế",
      "lang_en": "Hue"
    },
    {
      "location_id": "32",
      "lang_vn": "Hưng Yên",
      "lang_en": "Hung Yen"
    },
    {
      "location_id": "33",
      "lang_vn": "Khánh Hòa",
      "lang_en": "Khanh Hoa"
    },
    {
      "location_id": "34",
      "lang_vn": "Kon Tum",
      "lang_en": "Kon Tum"
    },
    {
      "location_id": "35",
      "lang_vn": "Lai Châu",
      "lang_en": "Lai Chau"
    },
    {
      "location_id": "36",
      "lang_vn": "Lâm Đồng",
      "lang_en": "Lam Dong"
    },
    {
      "location_id": "37",
      "lang_vn": "Lạng Sơn",
      "lang_en": "Lang Son"
    },
    {
      "location_id": "38",
      "lang_vn": "Lào Cai",
      "lang_en": "Lao Cai"
    },
    {
      "location_id": "39",
      "lang_vn": "Long An",
      "lang_en": "Long An"
    },
    {
      "location_id": "40",
      "lang_vn": "Nam Định",
      "lang_en": "Nam Dinh"
    },
    {
      "location_id": "41",
      "lang_vn": "Nghệ An",
      "lang_en": "Nghe An"
    },
    {
      "location_id": "42",
      "lang_vn": "Ninh Bình",
      "lang_en": "Ninh Binh"
    },
    {
      "location_id": "43",
      "lang_vn": "Ninh Thuận",
      "lang_en": "Ninh Thuan"
    },
    {
      "location_id": "44",
      "lang_vn": "Phú Thọ",
      "lang_en": "Phu Tho"
    },
    {
      "location_id": "45",
      "lang_vn": "Phú Yên",
      "lang_en": "Phu Yen"
    },
    {
      "location_id": "46",
      "lang_vn": "Quảng Bình",
      "lang_en": "Quang Binh"
    },
    {
      "location_id": "47",
      "lang_vn": "Quảng Nam",
      "lang_en": "Quang Nam"
    },
    {
      "location_id": "48",
      "lang_vn": "Quảng Ngãi",
      "lang_en": "Quang Ngai"
    },
    {
      "location_id": "49",
      "lang_vn": "Quảng Ninh",
      "lang_en": "Quang Ninh"
    },
    {
      "location_id": "50",
      "lang_vn": "Quảng Trị",
      "lang_en": "Quang Tri"
    },
    {
      "location_id": "51",
      "lang_vn": "Sóc Trăng",
      "lang_en": "Soc Trang"
    },
    {
      "location_id": "52",
      "lang_vn": "Sơn La",
      "lang_en": "Son La"
    },
    {
      "location_id": "53",
      "lang_vn": "Tây Ninh",
      "lang_en": "Tay Ninh"
    },
    {
      "location_id": "54",
      "lang_vn": "Thái Bình",
      "lang_en": "Thai Binh"
    },
    {
      "location_id": "55",
      "lang_vn": "Thái Nguyên",
      "lang_en": "Thai Nguyen"
    },
    {
      "location_id": "56",
      "lang_vn": "Thanh Hóa",
      "lang_en": "Thanh Hoa"
    },
    {
      "location_id": "57",
      "lang_vn": "Thừa Thiên-Huế",
      "lang_en": "Thua Thien-Hue"
    },
    {
      "location_id": "58",
      "lang_vn": "Tiền Giang",
      "lang_en": "Tien Giang"
    },
    {
      "location_id": "59",
      "lang_vn": "Trà Vinh",
      "lang_en": "Tra Vinh"
    },
    {
      "location_id": "60",
      "lang_vn": "Tuyên Quang",
      "lang_en": "Tuyen Quang"
    },
    {
      "location_id": "61",
      "lang_vn": "Kiên Giang",
      "lang_en": "Kien Giang"
    },
    {
      "location_id": "62",
      "lang_vn": "Vĩnh Long",
      "lang_en": "Vinh Long"
    },
    {
      "location_id": "63",
      "lang_vn": "Vĩnh Phúc",
      "lang_en": "Vinh Phuc"
    },
    {
      "location_id": "65",
      "lang_vn": "Yên Bái",
      "lang_en": "Yen Bai"
    },
    {
      "location_id": "66",
      "lang_vn": "Khác",
      "lang_en": "Other"
    },
    {
      "location_id": "70",
      "lang_vn": "Quốc tế",
      "lang_en": "International"
    },
    {
      "location_id": "72",
      "lang_vn": "Hậu Giang",
      "lang_en": "Hau Giang"
    }
  ];

  //TODO 1. Translation, 2. Validation

  var instance = {

    jobLevelsSelectize: {
      items: $.extend(true, [], jsonValue.jobLevels.filter(function (jobLevel) {return jobLevel.id > 0;})),
      config: $.extend(true, {}, translateConfigBase)
    },

    gendersSelectize: {
      items: jsonValue.genders,
      config: $.extend(true, {}, translateConfigBase)
    },

    yobsSelectize: {
      items: jsonValue.yobs,
      config: {
        valueField: 'value',
        labelField: 'value',
        delimiter: '|',
        searchField: ['value'],
        maxItems: 1
      }
    },

    locationsSelectize: {
      items: locations.map(function (location) {
        return {id: location.location_id, translate: location["lang_" + vnwLang]};
      }),
      config: $.extend(true, {}, translateConfigBase)
    },

    companySizeSelectize: {
      items: jsonValue.companySizesArray,
      config: $.extend(true, translateConfigBase, {
        valueField: 'id',
        labelField: 'size',
        searchField: ['size']
      })
    },

    industriesSelectize: {
      items: jsonValue.industriesArray,
      config: {
        valueField: 'id',
        labelField: 'name',
        delimiter: '|',
        maxItems: 3,
        plugins: ['remove_button'],
        searchField: ['name']
      }
    },

    timeToSendsSelectize: {
      items: $.extend(true, [], jsonValue.timeToSends),
      config: {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 1,
        searchField: ['translate']
      }
    }
  }

  var doTranslate = function(i, item) {
    $translate(item.translate).then(function(translate) {item.translate = translate;});
  }

  $.each(instance.jobLevelsSelectize.items, doTranslate);
  $.each(instance.gendersSelectize.items, doTranslate);
  $.each(instance.timeToSendsSelectize.items, doTranslate);

  $translate(["exManager", "exMale", "exYob", "exHoChiMinh", "ex149", "exItSoftware", "exDay"]).then(function(trans) {
    instance.jobLevelsSelectize.config.placeholder = trans.exManager;
    instance.gendersSelectize.config.placeholder = trans.exMale;
    instance.yobsSelectize.config.placeholder = trans.exYob;
    instance.locationsSelectize.config.placeholder = trans.exHoChiMinh;
    instance.companySizeSelectize.config.placeholder = trans.ex149;
    instance.industriesSelectize.config.placeholder = trans.exItSoftware;
    instance.timeToSendsSelectize.config.placeholder = trans.exDay;
  });
  return instance;
});