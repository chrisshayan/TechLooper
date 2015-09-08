techlooper.factory("vnwConfigService", function (jsonValue, $translate, $rootScope, utils, $q) {

  var translateConfigBase = {
    valueField: 'id',
    labelField: 'translate',
    delimiter: '|',
    maxItems: 1,
    searchField: ['translate']
  };

  var vnwLang = "lang_" + ($translate.use() === "en" ? "en" : "vn");
  var companySizes = [
    {
      "id": "1",
      "value": "Less Than 10"
    },
    {
      "id": "2",
      "value": "10-24"
    },
    {
      "id": "3",
      "value": "25-99"
    },
    {
      "id": "4",
      "value": "100-499"
    },
    {
      "id": "5",
      "value": "500-999"
    },
    {
      "id": "6",
      "value": "1,000-4,999"
    },
    {
      "id": "7",
      "value": "5,000-9,999"
    },
    {
      "id": "8",
      "value": "10,000-19,999"
    },{
      "id": "9",
      "value": "20,000-49,999"
    },{
      "id": "10",
      "value": "Over 50,000"
    }
  ];
  var locations = [
    {
      "location_id": "0",
      "lang_vn": "Tất cả vị trí",
      "lang_en": "All Locations"
    },
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
  var educationLevel = [
    {
      "education_id": "1",
      "lang_vn": "Không yêu",
      "lang_en": "None"
    },
    {
      "education_id": "2",
      "lang_vn": "Trung học",
      "lang_en": "High school"
    },
    {
      "education_id": "3",
      "lang_vn": "Trung cấp",
      "lang_en": "Degree"
    },
    {
      "education_id": "4",
      "lang_vn": "Cử nhân",
      "lang_en": "Diploma"
    },
    {
      "education_id": "12",
      "lang_vn": "Cao đẳng",
      "lang_en": "College"
    },
    {
      "education_id": "14",
      "lang_vn": "Kỹ Sư",
      "lang_en": "Bachelor of Engineering"
    },
    {
      "education_id": "13",
      "lang_vn": "Sau đại học",
      "lang_en": "Post-graduate"
    },
    {
      "education_id": "5",
      "lang_vn": "Thạc Sĩ",
      "lang_en": "Masters"
    },
    {
      "education_id": "6",
      "lang_vn": "Tiến sĩ",
      "lang_en": "Doctorate"
    },
    {
      "education_id": "7",
      "lang_vn": "Thạc sĩ Quản trị Kinh doanh",
      "lang_en": "MBA"
    },
    {
      "education_id": "11",
      "lang_vn": "Khác",
      "lang_en": "Others"
    }
  ];
  var companySize = [
    {
      "companySize_id": "1",
      "lang_vn": "Ít hơn 10",
      "lang_en": "Less Than 10"
    },
    {
      "companySize_id": "2",
      "lang_vn": "10-24",
      "lang_en": "10-24"
    },
    {
      "companySize_id": "2",
      "lang_vn": "25-99",
      "lang_en": "25-99"
    },
    {
      "companySize_id": "4",
      "lang_vn": "100-499",
      "lang_en": "100-499"
    },
    {
      "companySize_id": "5",
      "lang_vn": "500-999",
      "lang_en": "500-999"
    },
    {
      "companySize_id": "6",
      "lang_vn": "1,000-4,999",
      "lang_en": "1,000-4,999"
    },
    {
      "companySize_id": "7",
      "lang_vn": "5,000-9,999",
      "lang_en": "5,000-9,999"
    },
    {
      "companySize_id": "8",
      "lang_vn": "10,000-19,999",
      "lang_en": "10,000-19,999"
    },
    {
      "companySize_id": "9",
      "lang_vn": "20,000-49,999",
      "lang_en": "20,000-49,999"
    },
    {
      "companySize_id": "10",
      "lang_vn": "Hơn 50,000",
      "lang_en": "Over 50,000"
    }
  ];
  var experiences = [
    {
      "experience_id": "1",
      "lang_vn": "Lực lượng lao động mới",
      "lang_en": "New to workforce"
    },
    {
      "experience_id": "2",
      "lang_vn": "Ít hơn một năm",
      "lang_en": "Less than one year"
    },
    {
      "experience_id": "3",
      "lang_vn": "1 năm",
      "lang_en": "1 year"
    },
    {
      "experience_id": "4",
      "lang_vn": "2 năm",
      "lang_en": "2 years"
    },
    {
      "experience_id": "5",
      "lang_vn": "3 năm",
      "lang_en": "3 years"
    },
    {
      "experience_id": "6",
      "lang_vn": "4 năm",
      "lang_en": "4 years"
    },
    {
      "experience_id": "7",
      "lang_vn": "5 năm",
      "lang_en": "5 years"
    },
    {
      "experience_id": "8",
      "lang_vn": "6 năm",
      "lang_en": "6 years"
    },
    {
      "experience_id": "9",
      "lang_vn": "7 năm",
      "lang_en": "7 years"
    },
    {
      "experience_id": "10",
      "lang_vn": "8 năm",
      "lang_en": "8 years"
    },
    {
      "experience_id": "11",
      "lang_vn": "9 năm",
      "lang_en": "9 years"
    },
    {
      "experience_id": "12",
      "lang_vn": "10 năm",
      "lang_en": "10 years"
    },
    {
      "experience_id": "13",
      "lang_vn": "11 năm",
      "lang_en": "11 years"
    },
    {
      "experience_id": "14",
      "lang_vn": "12 năm",
      "lang_en": "12 years"
    },
    {
      "experience_id": "15",
      "lang_vn": "13 năm",
      "lang_en": "13 years"
    },
    {
      "experience_id": "16",
      "lang_vn": "14 năm",
      "lang_en": "14 years"
    },
    {
      "experience_id": "17",
      "lang_vn": "15 năm",
      "lang_en": "15 years"
    },
    {
      "experience_id": "18",
      "lang_vn": "16 năm",
      "lang_en": "16 years"
    },
    {
      "experience_id": "19",
      "lang_vn": "17 năm",
      "lang_en": "17 years"
    },
    {
      "experience_id": "20",
      "lang_vn": "18 năm",
      "lang_en": "18 years"
    },
    {
      "experience_id": "21",
      "lang_vn": "19 năm",
      "lang_en": "19 years"
    },
    {
      "experience_id": "22",
      "lang_vn": "Hơn 20 năm",
      "lang_en": "Over 20 years"
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
  ];
  var languages =[
    {
      "language_id": "38",
      "lang_vn": "Việt Nam",
      "lang_en": "Vietnamese"
    },
    {
      "language_id": "12",
      "lang_vn": "Anh",
      "lang_en": "English"
    },
    {
      "language_id": "21",
      "lang_vn": "Nhật Bản",
      "lang_en": "Japanese"
    },
    {
      "language_id": "23",
      "lang_vn": "Hàn Quốc",
      "lang_en": "Korean"
    },
    {
      "language_id": "24",
      "lang_vn": "Lào",
      "lang_en": "Laotian"
    },
    {
      "language_id": "1",
      "lang_vn": "Ả Rập",
      "lang_en": "Arabic"
    },
    {
      "language_id": "2",
      "lang_vn": "Bengal",
      "lang_en": "Bengali"
    },
    {
      "language_id": "3",
      "lang_vn": "Bungary",
      "lang_en": "Bulgarian"
    },
    {
      "language_id": "4",
      "lang_vn": "Myanmar",
      "lang_en": "Burmese"
    },
    {
      "language_id": "5",
      "lang_vn": "Campuchia",
      "lang_en": "Cambodian"
    },
    {
      "language_id": "6",
      "lang_vn": "Philippin",
      "lang_en": "Cebuano"
    },
    {
      "language_id": "7",
      "lang_vn": "Trung Quốc (Quảng Đông)",
      "lang_en": "Chinese (Cantonese)"
    },
    {
      "language_id": "8",
      "lang_vn": "Trung Quốc (Quan thoại)",
      "lang_en": "Chinese (Mandarin)"
    },
    {
      "language_id": "9",
      "lang_vn": "Séc",
      "lang_en": "Czech"
    },
    {
      "language_id": "10",
      "lang_vn": "Đan Mạch",
      "lang_en": "Danish"
    },
    {
      "language_id": "11",
      "lang_vn": "Dutch",
      "lang_en": "Dutch"
    },
    {
      "language_id": "13",
      "lang_vn": "Finnish",
      "lang_en": "Finnish"
    },
    {
      "language_id": "14",
      "lang_vn": "Pháp",
      "lang_en": "French"
    },
    {
      "language_id": "15",
      "lang_vn": "Đức",
      "lang_en": "German"
    },{
      "language_id": "16",
      "lang_vn": "Hy Lạp",
      "lang_en": "Greek"
    },
    {
      "language_id": "17",
      "lang_vn": "Ấn Độ",
      "lang_en": "Hindi"
    },
    {
      "language_id": "18",
      "lang_vn": "Hungary",
      "lang_en": "Hungarian"
    },
    {
      "language_id": "19",
      "lang_vn": "Indonesia",
      "lang_en": "Indonesian"
    },
    {
      "language_id": "20",
      "lang_vn": "Ý",
      "lang_en": "Italian"
    },
    {
      "language_id": "22",
      "lang_vn": "Tiếng Java",
      "lang_en": "Javanese"
    },
    {
      "language_id": "25",
      "lang_vn": "Malaysia",
      "lang_en": "Malay"
    },
    {
      "language_id": "26",
      "lang_vn": "Na Uy",
      "lang_en": "Norwegian"
    },
    {
      "language_id": "27",
      "lang_vn": "Ba Lan",
      "lang_en": "Polish"
    },
    {
      "language_id": "28",
      "lang_vn": "Bồ Đào Nha",
      "lang_en": "Portuguese"
    },
    {
      "language_id": "29",
      "lang_vn": "Rumani",
      "lang_en": "Romanian"
    },
    {
      "language_id": "30",
      "lang_vn": "Russian",
      "lang_en": "Russian"
    },
    {
      "language_id": "31",
      "lang_vn": "Tây Ban Nha",
      "lang_en": "Spanish"
    },
    {
      "language_id": "32",
      "lang_vn": "Thụy Điển",
      "lang_en": "Swedish"
    },
    {
      "language_id": "33",
      "lang_vn": "Philippin (Tagalog)",
      "lang_en": "Tagalog"
    },
    {
      "language_id": "34",
      "lang_vn": "Đài Loan",
      "lang_en": "Taiwanese"
    },
    {
      "language_id": "35",
      "lang_vn": "Thái Lan",
      "lang_en": "Thai"
    },
    {
      "language_id": "36",
      "lang_vn": "Turkish",
      "lang_en": "Turkish"
    },
    {
      "language_id": "37",
      "lang_vn": "Ukraina",
      "lang_en": "Ukrainia"
    },{
      "language_id": "39",
      "lang_vn": "Khác",
      "lang_en": "Other"
    }
  ];

  //TODO 1. Translation, 2. Validation

  var createSelectizeConfig = function (key) {
    return {
      plugins: ["techlooper"],
      getSelectize: function () {
        instance[key].config.selectizeDeffer = $q.defer();
        return instance[key].config.selectizeDeffer.promise;
      },
      onInitialize: function (selectize) {
        instance[key].config.selectizeDeffer.resolve(selectize);
      }
    }
  };

  var instance = {
    getLang: function () {
      return ($translate.use() === "en" ? "en" : "vn");
    },

    getLocationText: function (locationId, lang) {
      if (!locationId) return undefined;
      var text = "";
      $.each(locations, function (i, location) {if (location.location_id == locationId) {return (text = location["lang_" + lang] || location[vnwLang]);}});
      return text;
    },
    getCompanySizeText: function (companySizeId) {
      if (!companySizeId) return '';
      var text = "";
      $.each(companySizes, function (i, item) {if (item.id == companySizeId) {return text = item.value;}});
      return text;
    },
    getJobLevelText: function (jobLevelId) {
      var jobLevelTitle = undefined;
      if ($.type(jobLevelId) === "string") return instance.jobLevelsSelectize.items.findFirst(parseInt(jobLevelId), "id").translate;
      else if ($.type(jobLevelId) === "array") {
        var jobLevelIds = jobLevelId;
        var matchTimes = 0;
        $.each(instance.jobLevelsSelectize.items, function (i, item) {
          $.each(jobLevelIds, function (j, id) {
            if ($.type(id) === "string") id = parseInt(id);
            if ($.inArray(id, item.ids) > -1) {
              matchTimes++;
            }
          });
          if (matchTimes === jobLevelIds.length) {
            jobLevelTitle = item.translate;
            return false;
          }
        });
      }
      return jobLevelTitle;
    },

    getJobLevelIds: function (jobLevelId) {
      if ($.type(jobLevelId) !== "string") return jobLevelId;
      return instance.jobLevelsSelectize.items.findFirst(parseInt(jobLevelId), "id").ids;
    },

    getIndustryTexts: function (industryIds) {
      if ($.type(industryIds) !== "array") return undefined;

      var ids = industryIds.map(function (id) {return "" + id;});
      var texts = [];
      $.each(instance.industriesSelectize.items, function (i, item) {
        if ($.inArray(item.id, ids) > -1) {
          texts.push(item.translate);
        }
      });
      return texts;
    },

    jobLevelsSelectize: {
      items: $.extend(true, [], jsonValue.jobLevels.filter(function (jobLevel) {return jobLevel.id > 0;})),
      config: $.extend(true, {}, createSelectizeConfig("jobLevelsSelectize"), translateConfigBase)
    },

    gendersSelectize: {
      items: jsonValue.genders,
      config: $.extend(true, {}, createSelectizeConfig("gendersSelectize"), translateConfigBase)
    },

    yobsSelectize: {
      items: jsonValue.yobs,
      config: $.extend(true, {}, {
        valueField: 'value',
        labelField: 'value',
        delimiter: '|',
        searchField: ['value'],
        maxItems: 1
      }, createSelectizeConfig("yobsSelectize"))
    },

    educationLevel: {
      items: educationLevel.map(function (education) {
        return {id: education.education_id, translate: education[vnwLang], en: education.lang_en};
      }),
        config: $.extend(true, {}, createSelectizeConfig("educationLevel"), translateConfigBase)
    },
    locationsSelectize: {
      items: locations.map(function (location) {
        return {id: location.location_id, translate: location[vnwLang], en: location.lang_en};
      }),
      config: $.extend(true, {}, createSelectizeConfig("locationsSelectize"), translateConfigBase)
    },
    yearsOfExperience: {
      items: experiences.map(function (number) {
        return {id: number.experience_id, translate: number[vnwLang], en: number.lang_en};
      }),
      config: $.extend(true, {}, createSelectizeConfig("yearsOfExperience"), translateConfigBase)
    },

    locationsSearchSelectize: {
      items: locations.map(function (location) {
        return {id: location.location_id, translate: location[vnwLang], en: location.lang_en};
      }),
      config: $.extend(true, {}, createSelectizeConfig("locationsSelectize"), translateConfigBase)
    },
    companySizeSelectize: {
      items: companySize.map(function (size) {
        return {id: size.companySize_id, translate: size[vnwLang], en: size.lang_en};
      }),
      config: $.extend(true, {}, createSelectizeConfig("companySizeSelectize"), translateConfigBase)
    },

    languagesSelectize: {
      items: languages.map(function (language) {
        return {id: language.language_id, translate: language[vnwLang]};
      }),
      config: $.extend(true, {}, {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 3,
        searchField: ['translate']
      }, createSelectizeConfig("languagesSelectize"), {plugins: ['remove_button', "techlooper"]})
    },

    industriesSelectize: {
      items: categories.map(function (cate) {
        return {id: cate.category_id, translate: cate[vnwLang]};
      }),
      config: $.extend(true, {}, {
        valueField: 'id',
        labelField: 'translate',
        delimiter: '|',
        maxItems: 3,
        searchField: ['translate']
      }, createSelectizeConfig("industriesSelectize"), {plugins: ['remove_button', "techlooper"]})
    }
  };

  var transSelectizes = [
    {key: "jobLevelsSelectize", placeholder: "exManager", translate: true},
    {key: "yobsSelectize", placeholder: "exYob"},
    {key: "gendersSelectize", placeholder: "exMale", translate: true},
    {key: "locationsSelectize", placeholder: "exHoChiMinh"},
    {key: "locationsSearchSelectize", placeholder: "allLocations"},
    {key: "industriesSelectize", placeholder: "exItSoftware"},
    {key: "companySizeSelectize", placeholder: "ex149", translate: true},
    {key: "educationLevel", placeholder: "exEducation", translate: true},
    {key: "yearsOfExperience", placeholder: "exExperience", translate: true},
    {key: "languagesSelectize", placeholder: "exLanguages", translate: true}
  ];

  $.each(transSelectizes, function (i, item) {
    var selectizeKey = item.key;

    instance[selectizeKey].config.getSelectize().then(function ($selectize) {
      $translate(item.placeholder).then(function (translate) {
        instance[selectizeKey].config.placeholder = translate;
        $selectize.setPlaceholder(translate);
      });

      if (!item.translate) return true;

      $.each(instance[selectizeKey].items, function (i, row) {
        $translate(row.translate || row.size).then(function (translate) {
          if (!translate) {return true;}
          row.translate = translate;
          row.size && (row.size = translate);
        });
      });
    });

  });

  //$q.all(transPromises).then(function () {
  //  $.each(transSelectizes, function (i, item) {
  //    if (!item.translate) return true;
  //    instance[item.key].config.getSelectize().then(function($select) {
  //      $select.refreshItems();
  //    });
  //  });
  //});

  return instance;
});