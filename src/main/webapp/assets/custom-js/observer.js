var observer = new function() {
  var notification = {};

  this.registerNotification = function(name, fn) {
    if (notification[name] === undefined) {
      notification[name] = [];
    }
    return notification[name].push(fn);
  }

  this.sendNotification = function(name, object) {
    var notifies  = notification[name];
    if (notifies === undefined) {
      return false;
    }
    $.each(notifies, function(index, notify) {
      notify.apply(null, object);
    });
  }
}
