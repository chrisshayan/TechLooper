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

  var categories = [
    {
      "category_id": "1",
      "lang_vn": "Kế toán",
      "lang_en": "Accounting"
    },
    {
      "category_id": "58",
      "lang_vn": "Kiểm toán",
      "lang_en": "Auditing"
    },
    {
      "category_id": "67",
      "lang_vn": "Ô tô",
      "lang_en": "Automotive"
    },
    {
      "category_id": "2",
      "lang_vn": "Hành chánh/Thư ký",
      "lang_en": "Administrative/Clerical"
    },
    {
      "category_id": "3",
      "lang_vn": "Quảng cáo/Khuyến mãi/Đối ngoại",
      "lang_en": "Advertising/Promotion/PR"
    },
    {
      "category_id": "4",
      "lang_vn": "Nông nghiệp/Lâm nghiệp",
      "lang_en": "Agriculture/Forestry"
    },
    {
      "category_id": "5",
      "lang_vn": "Kiến trúc/Thiết kế nội thất ",
      "lang_en": "Architecture/Interior Design"
    },
    {
      "category_id": "22",
      "lang_vn": "Y tế/Chăm sóc sức khỏe",
      "lang_en": "Health/Medical Care"
    },
    {
      "category_id": "10",
      "lang_vn": "Mỹ thuật/Thiết kế",
      "lang_en": "Arts/Design"
    },
    {
      "category_id": "42",
      "lang_vn": "Ngân hàng",
      "lang_en": "Banking"
    },
    {
      "category_id": "43",
      "lang_vn": "Hóa học/Hóa sinh",
      "lang_en": "Chemical/Biochemical"
    },
    {
      "category_id": "7",
      "lang_vn": "Xây dựng",
      "lang_en": "Civil/Construction"
    },
    {
      "category_id": "68",
      "lang_vn": "Sản phẩm công nghiệp",
      "lang_en": "Industrial Products"
    },
    {
      "category_id": "8",
      "lang_vn": "Tư vấn",
      "lang_en": "Consulting"
    },
    {
      "category_id": "11",
      "lang_vn": "Dịch vụ khách hàng",
      "lang_en": "Customer Service"
    },
    {
      "category_id": "12",
      "lang_vn": "Giáo dục/Đào tạo",
      "lang_en": "Education/Training"
    },
    {
      "category_id": "64",
      "lang_vn": "Điện/Điện tử",
      "lang_en": "Electrical/Electronics"
    },
    {
      "category_id": "15",
      "lang_vn": "Mới tốt nghiệp",
      "lang_en": "Entry level"
    },
    {
      "category_id": "16",
      "lang_vn": "Môi trường/Xử lý chất thải",
      "lang_en": "Environment/Waste Services"
    },
    {
      "category_id": "17",
      "lang_vn": "Cấp quản lý điều hành",
      "lang_en": "Executive management"
    },
    {
      "category_id": "18",
      "lang_vn": "Người nước ngoài/Việt Kiều",
      "lang_en": "Expatriate Jobs in Vietnam"
    },
    {
      "category_id": "19",
      "lang_vn": "Xuất nhập khẩu",
      "lang_en": "Export-Import"
    },
    {
      "category_id": "54",
      "lang_vn": "Thực phẩm & Đồ uống",
      "lang_en": "Food & Beverage"
    },
    {
      "category_id": "59",
      "lang_vn": "Tài chính/Đầu tư",
      "lang_en": "Finance/Investment"
    },
    {
      "category_id": "63",
      "lang_vn": "Thời trang/Lifestyle",
      "lang_en": "Fashion/Lifestyle"
    },
    {
      "category_id": "66",
      "lang_vn": "Công nghệ cao",
      "lang_en": "High Technology"
    },
    {
      "category_id": "23",
      "lang_vn": "Nhân sự",
      "lang_en": "Human Resources"
    },
    {
      "category_id": "24",
      "lang_vn": "Bảo hiểm",
      "lang_en": "Insurance"
    },
    {
      "category_id": "47",
      "lang_vn": "Biên phiên dịch",
      "lang_en": "Interpreter/Translator"
    },
    {
      "category_id": "55",
      "lang_vn": "IT-Phần cứng/Mạng",
      "lang_en": "IT - Hardware/Networking"
    },
    {
      "category_id": "57",
      "lang_vn": "Internet/Online Media",
      "lang_en": "Internet/Online Media"
    },
    {
      "category_id": "35",
      "lang_vn": "IT - Phần mềm",
      "lang_en": "IT - Software"
    },
    {
      "category_id": "25",
      "lang_vn": "Pháp lý",
      "lang_en": "Legal/Contracts"
    },
    {
      "category_id": "62",
      "lang_vn": "Hàng cao cấp",
      "lang_en": "Luxury Goods"
    },
    {
      "category_id": "65",
      "lang_vn": "Cơ khí",
      "lang_en": "Mechanical"
    },
    {
      "category_id": "27",
      "lang_vn": "Marketing",
      "lang_en": "Marketing"
    },
    {
      "category_id": "21",
      "lang_vn": "Phi chính phủ/Phi lợi nhuận",
      "lang_en": "NGO/Non-Profit"
    },
    {
      "category_id": "28",
      "lang_vn": "Dầu khí",
      "lang_en": "Oil/Gas"
    },
    {
      "category_id": "6",
      "lang_vn": "Dược Phẩm/Công nghệ sinh học",
      "lang_en": "Pharmaceutical/Biotech"
    },
    {
      "category_id": "69",
      "lang_vn": "Hoạch định/Dự án",
      "lang_en": "Planning/Projects"
    },
    {
      "category_id": "26",
      "lang_vn": "Sản Xuất",
      "lang_en": "Production/Process"
    },
    {
      "category_id": "49",
      "lang_vn": "Vật Tư/Cung vận",
      "lang_en": "Purchasing/Supply Chain"
    },
    {
      "category_id": "30",
      "lang_vn": "Bất động sản",
      "lang_en": "Real Estate"
    },
    {
      "category_id": "32",
      "lang_vn": "Bán lẻ/Bán sỉ",
      "lang_en": "Retail/Wholesale"
    },
    {
      "category_id": "33",
      "lang_vn": "Bán hàng",
      "lang_en": "Sales"
    },
    {
      "category_id": "34",
      "lang_vn": "Bán hàng kỹ thuật",
      "lang_en": "Sales Technical"
    },
    {
      "category_id": "56",
      "lang_vn": "Chứng khoán",
      "lang_en": "Securities & Trading"
    },
    {
      "category_id": "41",
      "lang_vn": "Viễn Thông",
      "lang_en": "Telecommunications"
    },
    {
      "category_id": "51",
      "lang_vn": "Thời vụ/Hợp đồng ngắn hạn",
      "lang_en": "Temporary/Contract"
    },
    {
      "category_id": "52",
      "lang_vn": "Dệt may/Da giày",
      "lang_en": "Textiles/Garments/Footwear"
    },
    {
      "category_id": "37",
      "lang_vn": "Hàng không/Du lịch/Khách sạn",
      "lang_en": "Airlines/Tourism/Hotel"
    },
    {
      "category_id": "48",
      "lang_vn": "Truyền hình/Truyền thông/Báo chí",
      "lang_en": "TV/Media/Newspaper"
    },
    {
      "category_id": "36",
      "lang_vn": "Vận chuyển/Giao nhận",
      "lang_en": "Freight/Logistics"
    },
    {
      "category_id": "53",
      "lang_vn": "Kho vận",
      "lang_en": "Warehouse"
    },
    {
      "category_id": "70",
      "lang_vn": "QA/QC",
      "lang_en": "QA/QC"
    },
    {
      "category_id": "71",
      "lang_vn": "Overseas Jobs",
      "lang_en": "Overseas Jobs"
    },
    {
      "category_id": "39",
      "lang_vn": "Khác",
      "lang_en": "Other"
    }
  ]

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
      config: {
        valueField: 'id',
        labelField: 'size',
        delimiter: '|',
        maxItems: 1,
        searchField: ['size']
      }
    },

    industriesSelectize: {
      items: categories.map(function (cate) {
        return {id: cate.category_id, translate: cate["lang_" + vnwLang]};
      }),
      config: $.extend(true, {plugins: ['remove_button'], maxItems: 3}, translateConfigBase)
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

  var doTranslate = function (i, item) {
    $translate(item.translate).then(function (translate) {item.translate = translate;});
  }

  $.each(instance.jobLevelsSelectize.items, doTranslate);
  $.each(instance.gendersSelectize.items, doTranslate);
  $.each(instance.timeToSendsSelectize.items, doTranslate);
  $.each(instance.companySizeSelectize.items, function (i, item) {
    $translate(item.size).then(function (trans) {
      if (!trans) {return true;}
      item.size = trans;
    });
  });

  $translate(["exManager", "exMale", "exYob", "exHoChiMinh", "ex149", "exItSoftware", "exDay"]).then(function (trans) {
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