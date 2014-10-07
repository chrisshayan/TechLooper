angular.module("Common").factory("connectionFactory", [ "jsonValue", function(jsonValue) {
   var _this = this;
   var sockketUri = jsonValue.socketUri;
   var events = jsonValue.events;
   var callbacks = [];
   var scope;
   var connected = false;
   var terms = [];
   var stompClient;

   return {
      initialize : function($scope) {
         scope = $scope;
      },

      connectSocket : function() {
         stompClient = Stomp.over(new SockJS(sockketUri.sockjs));
         // stompClient.debug = function() {};

         stompClient.connect({}, function() {
            connected = true;
            $.each(callbacks, function(index, callback) {
               callback.fn.call(callback.args);
            });
            callbacks.length = 0;
         });
      },

      getTechnicalTerms : function() {
         if (!stompClient.connected) {
            callbacks.push({
               fn : this.getTechnicalTerms,
               args : undefined
            });
            return;
         }
         var client = stompClient.subscribe(sockketUri.subscribeTerms, function(response) {
            scope.$emit(events.terms, JSON.parse(response.body));
            client.unsubscribe();
         });
         stompClient.send(sockketUri.sendTerms);
      },

      isConnected : function() {
         if (stompClient != undefined) {
            return stompClient.connected;
         }
         return false;
      },

      getStompClient : function() {
         return stompClient;
      }
   }
} ]);