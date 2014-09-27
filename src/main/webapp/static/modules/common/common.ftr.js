angular.module("Common").factory("jsonFactory", function() {

   return {
      shortcuts : function() {
         return [ {
            "id" : 1,
            "name" : "Career Analytics",
            "keyShort" : "Ctrl + Alt + A"
         }, {
            "id" : 2,
            "name" : "Find Job",
            "keyShort" : "Ctrl + Alt + F"
         }, {
            "id" : 3,
            "name" : "Function Name 1",
            "keyShort" : "Ctrl + Alt + 1"
         }, {
            "id" : 4,
            "name" : "Function Name 2",
            "keyShort" : "Ctrl + Alt + 2"
         } ];
      },

      companies : function() {
         return [ {
            "id" : 1,
            "img" : "images/cp-logo-atlassian.png",
            "url" : "https://www.atlassian.com/"
         }, {
            "id" : 2,
            "img" : "images/cp-logo-google.png",
            "url" : "https://www.google.com/"
         }, {
            "id" : 3,
            "img" : "images/cp-logo-techcombank.png",
            "url" : "https://www.techcombank.com.vn/"
         }, {
            "id" : 4,
            "img" : "images/cp-logo-fpt.png",
            "url" : "https://www.fpt.vn/"
         }, {
            "id" : 5,
            "img" : "images/cp-logo-harveynash.png",
            "url" : "https://www.harveynash.vn/"
         }, {
            "id" : 6,
            "img" : "images/cp-logo-vtv.png",
            "url" : "https://www.vtv.vn/"
         } ];
      }
   }
});