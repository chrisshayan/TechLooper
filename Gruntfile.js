module.exports = function(grunt) {

   grunt.initConfig({
      pkg : grunt.file.readJSON("package.json"),

      bower : {
          install : {
          }
      },

      wiredep : {
         target : {
            src : [ "src/main/webapp/assets/index.html" ]
         }
      },

      includeSource : {
         options : {
            basePath : "src/main/webapp/assets",
            duplicates : false,
            debug : true
         },
         target : {
            files : {
               "src/main/webapp/assets/index.html" : "src/main/webapp/assets/index.tpl.html"
            }
         }
      },

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
               port : 8080,
               base : ".",
               keepalive : true
            }
         }
      }
   });

   grunt.loadNpmTasks("grunt-contrib-watch");
   grunt.loadNpmTasks("grunt-contrib-connect");
   grunt.loadNpmTasks("grunt-include-source");
   grunt.loadNpmTasks("grunt-wiredep");
   grunt.loadNpmTasks("grunt-bower-task");

   grunt.registerTask("build", [ "bower:install", "includeSource:target", "wiredep:target" ]);
   grunt.registerTask("run", [ "connect", "watch" ]);
};