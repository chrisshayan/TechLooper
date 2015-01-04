angular.module("Common").factory("connectionFactory",
  function (jsonValue, utils, $http, $q, $location, localStorageService, $rootScope) {
    var socketUri = jsonValue.socketUri;
    var events = jsonValue.events;
    var callbacks = [];
    var scope;
    var subscriptions = {};
    var isConnecting = false;

    //var contextUrl = window.location.protocol + '//' + window.location.host + paths.join('/');
    var stompUrl = baseUrl + '/' + socketUri.sockjs;
    var broadcastClient = Stomp.over(new SockJS(stompUrl));
    broadcastClient.debug = function () {};

    // private functions
    var $$ = {
      /** must to call when controller initialized */
      initialize: function ($scope) {
        $$.clearCache();
        scope = $scope;
      },

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
      },

      post: function (uri, params, header) {
        var deferred = $q.defer();
        setTimeout(function () {
          $http.post(uri, params, header)
            .success(function (data, status, headers, config) {
              deferred.resolve(data, status, headers, config);
            })
            .error(function (data, status, headers, config) {
              deferred.reject(data, status, headers, config);
            });
        }, 2000);
        return deferred.promise;
      },

      errorHandler: function (error) {
        if (error.headers !== undefined && error.headers.message.indexOf("AuthenticationCredentialsNotFoundException") >= 0) {
          localStorageService.set(jsonValue.storage.back2Me, "true");
          utils.sendNotification(jsonValue.notifications.loginFailed);
        }
      }
    }

    var instance = {
      verifyUserLogin: function() {
        return $$.post(jsonValue.httpUri.verifyUserLogin,
          {key: localStorageService.get(jsonValue.storage.key)});
      },

      login: function () {
        if (localStorageService.get(jsonValue.storage.key) === null) {
          return utils.sendNotification(jsonValue.notifications.loginFailed);
        }

        $$.post(jsonValue.httpUri.login,
          $.param({key: localStorageService.get(jsonValue.storage.key)}),
          {headers: {'Content-Type': 'application/x-www-form-urlencoded; charset=UTF-8'}})
          .then(function (data, status, headers, config) {
            utils.sendNotification(jsonValue.notifications.loginSuccess, data, status, headers, config);
          })
          .catch(function (data, status, headers, config) {
            utils.sendNotification(jsonValue.notifications.loginFailed, data, status, headers, config);
          });
      },

      saveUserInfo: function (json) {
        return $$.post(jsonValue.httpUri.userSave, json);
      },

      findUserInfoByKey: function () {
        if (!broadcastClient.connected) {
          callbacks.push({
            fn: instance.findUserInfoByKey,
            args: undefined
          });
          return instance.connectSocket();
        }

        var subscription = broadcastClient.subscribe(socketUri.subscribeUserInfo, function (response) {
          var userInfo = JSON.parse(response.body);
          $rootScope.userInfo = userInfo;
          //scope.$emit(events.userInfo, userInfo);
          utils.sendNotification(jsonValue.notifications.userInfo, userInfo);
          subscription.unsubscribe();
        });

        broadcastClient.send(socketUri.getUserInfoByKey, {},
          JSON.stringify({key: localStorageService.get(jsonValue.storage.key)}));
      },

      /* @subscription */
      analyticsSkill: function (analyticJson) {
        if (!broadcastClient.connected) {
          callbacks.push({
            fn: instance.analyticsSkill,
            args: analyticJson
          });
          return instance.connectSocket();
        }

        var uri = socketUri.subscribeAnalyticsSkill;
        //var subscription = subscriptions[uri];
        //if (subscription !== undefined) { return true; }

        var subscription = broadcastClient.subscribe(uri, function (response) {
          scope.$emit(events.analyticsSkill, JSON.parse(response.body));
          subscription.unsubscribe(); // TODO no need to support real-time now
          utils.sendNotification(jsonValue.notifications.gotData);
        });
        //subscriptions[uri] = subscription;

        broadcastClient.send(socketUri.analyticsSkill, {},
          JSON.stringify({term: analyticJson.term, histograms: analyticJson.histograms}));
      },

      /* @subscription */
      findJobs: function (json) {
        if (!broadcastClient.connected) {
          callbacks.push({
            fn: instance.findJobs,
            args: json
          });
          return instance.connectSocket();
        }

        var subscription = broadcastClient.subscribe(socketUri.subscribeJobsSearch, function (response) {
          scope.$emit(events.foundJobs, JSON.parse(response.body));
          subscription.unsubscribe();
        });
        broadcastClient.send(socketUri.sendJobsSearch, {}, JSON.stringify(json));
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
        subscription = broadcastClient.subscribe(uri, function (response) {
          scope.$emit(events.term + term.term, {
            count: JSON.parse(response.body).count,
            term: term.term
          });
        });
        subscriptions[uri] = subscription;
      },

      connectSocket: function () {
        if (isConnecting) { return; }

        if (instance.isConnected()) {
          broadcastClient.disconnect();
        }
        subscriptions = {};
        broadcastClient = Stomp.over(new SockJS(stompUrl));
        broadcastClient.debug = function () {};

        broadcastClient.connect({}, function (frame) {
          isConnecting = false;
          $$.runCallbacks();
        }, function (error) {
          isConnecting = false;
          $$.errorHandler(error);
        });//onreceipt
        isConnecting = true;
      },

      /* @subscription */
      receiveTechnicalTerms: function () {
        if (!broadcastClient.connected) {
          callbacks.push({
            fn: instance.receiveTechnicalTerms,
            args: undefined
          });
          return instance.connectSocket();
        }
        var subscribeTerms = broadcastClient.subscribe(socketUri.subscribeTerms, function (response) {
          scope.$emit(events.terms, JSON.parse(response.body));
          instance.registerTermsSubscription(JSON.parse(response.body));
          subscribeTerms.unsubscribe();
          utils.sendNotification(jsonValue.notifications.gotData);
        });
        broadcastClient.send(socketUri.sendTerms);
      },

      isConnected: function () {
        if (broadcastClient !== undefined) {
          return broadcastClient.connected;
        }
        return false;
      },

      initialize: function () {}
    }
    utils.registerNotification(jsonValue.notifications.loginSuccess, instance.connectSocket);
    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize);
    return instance;
  });
