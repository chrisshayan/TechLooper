angular.module("Home").controller('registerController', [ "connectionFactory", function(connectionFactory) {
   connectionFactory.connectSocket();
} ]);