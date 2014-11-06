angular.module("Common").factory("connectionFactory", function (jsonValue, $cacheFactory, $location) {
  var socketUri = jsonValue.socketUri;
  var events = jsonValue.events;
  var callbacks = [];
  var scope;
  var subscriptions = {};
  var isConnecting = false;

  var paths = window.location.pathname.split('/');
  paths.pop();
  var contextUrl = window.location.protocol + '//' + window.location.host + paths.join('/');
  var stompUrl = contextUrl + '/' + socketUri.sockjs;
  var stompClient = Stomp.over(new SockJS(stompUrl));
  stompClient.debug = function () {};

  // private functions
  var $$ = {
    clearCache: function () {
      for (var uri in subscriptions) {
        if ($.type(subscriptions[uri]) !== "number") {
          subscriptions[uri].unsubscribe();
        }
      }
      callbacks.length = 0;
      subscriptions = {};
    },

    runCallbacks: function () {
      $.each(callbacks, function (index, callback) {
        callback.fn(callback.args);
      });
      callbacks.length = 0;
    }
  }

  var instance = {

    /* @subscription */
    analyticsSkill: function (term) {
      if (!stompClient.connected) {
        callbacks.push({
          fn: instance.analyticsSkill,
          args: term
        });
        return instance.connectSocket();
      }

      var uri = socketUri.subscribeAnalyticsSkill;
      var subscription = subscriptions[uri];
      if (subscription !== undefined) { return true; }

      subscription = stompClient.subscribe(uri, function (response) {
        scope.$emit(events.analyticsSkill, JSON.parse(response.body));
      });
      subscriptions[uri] = subscription;
      stompClient.send(socketUri.analyticsSkill, {}, JSON.stringify({term: term, period: "week"}));
    },

    /* @subscription */
    findJobs: function (json) {
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
      stompClient.send(socketUri.sendJobsSearch, {}, JSON.stringify(json));
    },

    /** must to call when controller initialized */
    initialize: function ($scope) {
      $$.clearCache();
      scope = $scope;
    },

    /* @subscription */
    registerTermsSubscription: function (terms) {
      $.each(terms, function (index, term) {
        instance.subscribeTerm(term);
      });
    },

    // {term: "JAVA", name: "java"}
    subscribeTerm: function (term) {
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
    },

    connectSocket: function () {
      if (isConnecting) { return; }

      if (instance.isConnected()) {
        stompClient.disconnect();
      }
      stompClient = Stomp.over(new SockJS(stompUrl));
      stompClient.debug = function () {};

      stompClient.connect({}, function (frame) {
        isConnecting = false;
        $$.runCallbacks();
      }, function (errorFrame) {
        console.log("Erorr: " + errorFrame);
        isConnecting = false;
      });
      isConnecting = true;
    },

    /* @subscription */
    receiveTechnicalTerms: function () {
      if (!stompClient.connected) {
        callbacks.push({
          fn: instance.receiveTechnicalTerms,
          args: undefined
        });
        return instance.connectSocket();
      }
      var subscribeTerms = stompClient.subscribe(socketUri.subscribeTerms, function (response) {
        scope.$emit(events.terms, JSON.parse(response.body));
        instance.registerTermsSubscription(JSON.parse(response.body));
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
});
