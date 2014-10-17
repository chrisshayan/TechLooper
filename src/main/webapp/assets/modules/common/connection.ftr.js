angular.module("Common").factory("connectionFactory", ["jsonValue", "$cacheFactory", function (jsonValue, $cacheFactory) {
  var socketUri = jsonValue.socketUri;
  var events = jsonValue.events;
  var callbacks = [];
  var scope;
  var terms = [];
  var subscriptions = {};
  var isConnecting = false;

  var stompClient = Stomp.over(new SockJS(socketUri.sockjs));
  stompClient.debug = function () {};

  var clearCache = function() {
    for (var uri in subscriptions) {
      subscriptions[uri].unsubscribe();
    }
    callbacks.length = 0;
  }

  var runCallbacks = function() {
    $.each(callbacks, function (index, callback) {
      callback.fn.call(callback.args);
    });
    callbacks.length = 0;
  }

  var instance = {

    // json = { "terms": "java .net", "pageNumber" : "3" }
    findJobs : function(json) {
      if (!stompClient.connected) {
        callbacks.push({
          fn: instance.findJobs,
          args: json
        });
        return instance.connectSocket();
      }

      var subscription = stompClient.subscribe(socketUri.subscribeJobsSearch, function (response) {
        scope.$emit(events.foundJobs, JSON.parse(response.body));
        subscription.unsubscribe();
      });
      stompClient.send(socketUri.sendJobsSearch, JSON.stringify(json));
    },

    /** must to call when controller initialized */
    initialize: function ($scope) {
      clearCache();
      scope = $scope;
    },

    registerTermsSubscription: function () {
      $.each(terms, function (index, term) {
        var uri = socketUri.subscribeTerm + term.term;
        var subscription = subscriptions[uri];
        if (subscription !== undefined) {
          return true;
        }
        subscription = stompClient.subscribe(uri, function (response) {
          scope.$emit(events.term + term.term, {
            count: JSON.parse(response.body).count,
            term: term.term,
            termName: term.name
          });
        });
        subscriptions[uri] = subscription;
      });
    },

    connectSocket: function () {
      if (isConnecting) {
        return;
      }
      stompClient.connect({}, function (frame) {
        isConnecting = false;
        runCallbacks();
      }, function (errorFrame) {
        console.log("Erorr: " + errorFrame);
      });
      isConnecting = true;
    },

    receiveTechnicalTerms: function () {
      if (!stompClient.connected) {
        callbacks.push({
          fn: instance.receiveTechnicalTerms,
          args: undefined
        });
        return instance.connectSocket();
      }
      var subscribeTerms = stompClient.subscribe(socketUri.subscribeTerms, function (response) {
        terms = JSON.parse(response.body);
        scope.$emit(events.terms, terms);
        instance.registerTermsSubscription();
        subscribeTerms.unsubscribe();
      });
      stompClient.send(socketUri.sendTerms);
    },

    isConnected: function () {
      if (stompClient !== undefined) {
        return stompClient.connected;
      }
      return false;
    },

    getStompClient: function () {
      return stompClient;
    }
  }
  return instance;
}]);
