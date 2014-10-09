angular.module("Common").factory("connectionFactory", [ "jsonValue", function(jsonValue) {
   var sockketUri = jsonValue.socketUri;
   var events = jsonValue.events;
   var callbacks = [];
   var scope;
   var terms = [];
   var stompClient;
   var subscriptions = "";
   var onEvents = [];

   var instance = {
      initialize : function($scope) {
         if (scope !== undefined) {
            scope.$destroy();
         }
         scope = $scope;
      },

      registerTermsSubscription : function() {
         $.each(terms, function(index, term) {
            var uri = sockketUri.subscribeTerm + term.term;
            if (subscriptions[uri] !== undefined) {
               return true;
            }
            subscriptions[uri] = stompClient.subscribe(uri, function(response) {
               scope.$emit(events.term + term.term, {
                  count : JSON.parse(response.body).count,
                  term : term.term,
                  termName : term.name
               });
            });
         });
      },

      connectSocket : function() {
         stompClient = Stomp.over(new SockJS(sockketUri.sockjs));
         stompClient.debug = function() {};

         stompClient.connect({}, function() {
            $.each(callbacks, function(index, callback) {
               callback.fn.call(callback.args);
            });
            callbacks.length = 0;
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
         var subscribeTerms = stompClient.subscribe(sockketUri.subscribeTerms, function(response) {
            terms = JSON.parse(response.body);
            scope.$emit(events.terms, terms);
            instance.registerTermsSubscription();
            subscribeTerms.unsubscribe();
         });
         stompClient.send(sockketUri.sendTerms);
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