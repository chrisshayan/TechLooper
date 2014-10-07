angular.module("Common").factory("connectionFactory", function() {
   var stomClient, connected = false;
   return {
      connectSocket : function() {
         var socket = new SockJS('ws');
         stompClient = Stomp.over(socket);
         stompClient.debug = function() {};

         stompClient.connect({}, function() {
            connected = true;
         });
      },
      
      getTechnicalTerms : function($scope) {
         var client = stompClient.subscribe('/topic/technical-job/terms', function (response) {
            $scope.emit
            client.unsubscribe();
        });
         stompClient.send("/app/technical-job/terms");
      },

      isConnected : function() {
         return connected;
      },
      
      getStompClient : function() {
         return stompClient;
      }
   }
});