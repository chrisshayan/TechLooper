module.exports = function(grunt) {

   grunt.initConfig({
      pkg : grunt.file.readJSON("package.json"),

      "bower-install-simple" : {
         options : {
            color : true,
            directory : "<%=pkg.assets%>" + "bower_components"
         },
         prod : {
            options : {
               production : true
            }
         }
      },

      wiredep : {
         options : {
            color : true,
            directory : "<%=pkg.assets%>" + "bower_components"
         },
         target : {
            src : [ "<%=pkg.assets%>" + "index.html" ]
         }
      },

      includeSource : {
         options : {
            basePath : "<%=pkg.assets%>",
            duplicates : false,
            debug : true
         },
         target : {
            files : [ {
               expand : true,
               cwd : "<%=pkg.assets%>",
               src : [ "index.tpl.html" ],
               dest : "<%=pkg.assets%>",
               ext : ".html"
            } ]
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
   grunt.loadNpmTasks("grunt-bower-install-simple");

   grunt.registerTask("build", [ "bower-install-simple", "includeSource:target", "wiredep:target" ]);
   grunt.registerTask("run", [ "connect", "watch" ]);
};