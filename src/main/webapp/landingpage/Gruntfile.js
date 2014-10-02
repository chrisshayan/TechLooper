module.exports = function(grunt) {

   grunt.initConfig({
      pkg : grunt.file.readJSON("package.json"),

      watch : {
         scripts : {
            files : [ "*.js", "*.json" ],
            options : {
               livereload : true
            }
         },
         markup : {
            files : [ "*.html" ],
            options : {
               livereload : true
            }
         },
         stylesheets : {
            files : [ "*.css" ],
            options : {
               livereload : true
            }
         }
      },
      connect : {
         server : {
            options : {
               port : 8081,
               base : ".",
               keepalive : true
            }
         }
      }
   });

   grunt.loadNpmTasks("grunt-contrib-watch");
   grunt.loadNpmTasks("grunt-contrib-connect");
   grunt.registerTask("run", [ "connect", "watch" ]);
};