angular.module("Common").factory("connectionFactory", [ "jsonValue", "$cacheFactory",function(jsonValue, $cacheFactory) {
   var socketUri = jsonValue.socketUri;
   var events = jsonValue.events;
   var callbacks = [];
   var scope;
   var terms = [];
   var stompClient;
   var cache = $cacheFactory("subscriptions");

   var instance = {
      initialize : function($scope) {
         scope = $scope;
      },

      registerTermsSubscription : function() {
         $.each(terms, function(index, term) {
            var uri = socketUri.subscribeTerm + term.term;
            var subscription = cache.get(uri);
            if (subscription !== undefined) {
               return true;
            }
            subscription = stompClient.subscribe(uri, function(response) {
               scope.$emit(events.term + term.term, {
                  count : JSON.parse(response.body).count,
                  term : term.term,
                  termName : term.name
               });
            });
            cache.put(uri, subscription);
         });
      },

      connectSocket : function() {
         stompClient = Stomp.over(new SockJS(socketUri.sockjs));
         stompClient.onclose
         stompClient.debug = function() {};
         stompClient.connect({}, function(frame) {
            $.each(callbacks, function(index, callback) {
               callback.fn.call(callback.args);
            });
            callbacks.length = 0;
         }, function(errorFrame) {
            console.log("Erorr: " + errorFrame);
         });
      },

      receiveTechnicalTerms : function() {
         if (!stompClient.connected) {
            callbacks.push({
               fn : this.receiveTechnicalTerms,
               args : undefined
            });
            return;
         }
         var subscribeTerms = stompClient.subscribe(socketUri.subscribeTerms, function(response) {
            terms = JSON.parse(response.body);
            scope.$emit(events.terms, terms);
            instance.registerTermsSubscription();
            subscribeTerms.unsubscribe();
         });
         stompClient.send(socketUri.sendTerms);
      },

      isConnected : function() {
         if (stompClient !== undefined) {
            return stompClient.connected;
         }
         return false;
      },

      getStompClient : function() {
         return stompClient;
      }
   }
   return instance;
} ]);
