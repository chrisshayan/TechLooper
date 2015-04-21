angular.module("Common").factory("connectionFactory",
  function (jsonValue, utils, $http, $q, $location, localStorageService, $rootScope, $timeout) {
    var socketUri = jsonValue.socketUri;
    var events = jsonValue.events;
    var callbacks = [];
    var rootScopeCallbacks = [];
    var scope;
    var subscriptions = {};
    var isConnecting = false;

    //var contextUrl = window.location.protocol + '//' + window.location.host + paths.join('/');
    var stompUrl = baseUrl + '/' + socketUri.sockjs;
    var broadcastClient = Stomp.over(new SockJS(stompUrl));
    broadcastClient.debug = function () {};

    var stompClient = {
      stomp: Stomp.over(new SockJS(stompUrl)),
      deferred: $q.defer()
    };

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
            try{subscriptions[uri].unsubscribe();}catch(e){};
          }
        }
        callbacks.length = 0;
        subscriptions = {};
      },

      runCallbacks: function () {
        $.each(callbacks, function (index, callback) {
          callback.fn(callback.args);
        });
        $.each(rootScopeCallbacks, function (index, callback) {
          callback.fn(callback.args);
        });
        rootScopeCallbacks.length = 0;
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
          return utils.sendNotification(jsonValue.notifications.loginFailed);
        }
      }
    }

    var instance = {
      verifyUserLogin: function () {
        return $$.post(jsonValue.httpUri.verifyUserLogin, {
          emailAddress: $rootScope.userInfo !== undefined ? $rootScope.userInfo.emailAddress : ""
        });
      },

      login: function () {
        var key = localStorageService.cookie.get(jsonValue.storage.key);
        if (key === null) {
          return utils.sendNotification(jsonValue.notifications.loginFailed);
        }

        $$.post(jsonValue.httpUri.login,
          $.param({key: key}),
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
        //HTTP version
        $$.post(jsonValue.httpUri.getUserInfoByKey)
          .then(function (userInfo) {
            $rootScope.userInfo = userInfo;
            utils.sendNotification(jsonValue.notifications.userInfo, userInfo);
          })
          .catch(function () {
            utils.sendNotification(jsonValue.notifications.notUserInfo);
          });


        // Websocket version - for nginx which support websocket version
        //if (!broadcastClient.connected) {
        //  rootScopeCallbacks.push({
        //    fn: instance.findUserInfoByKey,
        //    args: undefined
        //  });
        //  return instance.connectSocket();
        //}
        //
        //var subscription = broadcastClient.subscribe(socketUri.subscribeUserInfo, function (response) {
        //  var userInfo = JSON.parse(response.body);
        //  $rootScope.userInfo = userInfo;
        //  utils.sendNotification(jsonValue.notifications.userInfo, userInfo);
        //  subscription.unsubscribe();
        //});
        //
        //broadcastClient.send(socketUri.getUserInfoByKey, {},
        //  JSON.stringify({key: localStorageService.get(jsonValue.storage.key)}));
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

      subscribeTerm: function (term) {
        var uri = socketUri.subscribeTerm + term.term;
        var subscription = subscriptions[uri];
        if (subscription !== undefined) {
          return true;
        }
        subscription = broadcastClient.subscribe(uri, function (response) {
          scope.$emit(events.term + term.term, JSON.parse(response.body));
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

      reconnectSocket: function () {
        if (stompClient.isConnecting === true) {
          return stompClient.deferred.promise;
        }
        else if (stompClient.stomp.connected === true) {
          $timeout(function () {stompClient.deferred.resolve();}, 100);
          return stompClient.deferred.promise;
        }
        stompClient.stomp = Stomp.over(new SockJS(stompUrl));
        stompClient.stomp.debug = function () {};
        stompClient.isConnecting = true;
        stompClient.stomp.connect({}, function (frame) {
          stompClient.isConnecting = false;
          stompClient.deferred.resolve();
        }, function (error) {
          stompClient.isConnecting = false;
          $$.errorHandler(error);
        });
        return stompClient.deferred.promise;
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

      initialize: function () {},

      subscribeUserRegistration: function () {
        var uri = jsonValue.socketUri.subscribeUserRegistration;
        if (subscriptions[uri] === undefined) {
          subscriptions[uri] = {};
        }
        instance.reconnectSocket().then(function () {
          if (subscriptions[uri].subscribe === undefined) {
            subscriptions[uri].subscribe = stompClient.stomp.subscribe(uri, function (response) {
              utils.sendNotification(jsonValue.notifications.userRegistrationCount, response.body);
            });
          }
        });
      },

      /**
       * Get Tem statistic by name
       *
       * @param {object} request
       * @param {string} request.term - The name of term , see jsonValue.viewTerms.listedItems
       * @param {string[]} [request.skills] - List of skill name, ex: "spring framework", "log4j"...
       * @param {int} [request.jobLevelId] - The level of Jobs
       * @return {object} $http object
       */
      termStatisticInOneYear: function(request) {
        return $http.post(jsonValue.httpUri.termStatistic, request);
      }
    }
    utils.registerNotification(jsonValue.notifications.logoutSuccess, instance.connectSocket);
    utils.registerNotification(jsonValue.notifications.loginSuccess, instance.connectSocket);
    utils.registerNotification(jsonValue.notifications.switchScope, $$.initialize);
    return instance;
  });
