angular.module("Home").controller('registerController', [ "connectionFactory", function(connectionFactory) {
   if (!connectionFactory.isConnected()) {
      connectionFactory.connectSocket();
   }
} ]);