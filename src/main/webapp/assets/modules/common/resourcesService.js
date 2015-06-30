techlooper.factory("resourcesService", function ($translate) {
  var reviewStyle = [{translate: "contestOwnerSignOff"}];

  var instance = {
    reviewStyleConfig: {
      options: resourcesService.reviewStyle(),
      create: false,
      placeholder: "",
      maxItems: 1
    }

    //reviewStyle: function() {
    //  switch ($translate.use()) {
    //    case "en":
    //      return ["Contest Owner Sign-Off"];
    //
    //    case "vi":
    //      return ["Nhà Tổ Chức Cuộc Thi chịu trách nhiệm phê duyệt sản phẩm"];
    //  }
    //  return [];
    //}
  }

  return instance;
});