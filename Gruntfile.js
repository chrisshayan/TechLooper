module.exports = function(grunt) {

   grunt.initConfig({
      pkg : grunt.file.readJSON("package.json"),

      clean : {
         build : [ "<%=pkg.public%>" ],
         dev : ["<%=pkg.public%>index.tpl.html", "<%=pkg.public%>sass", "<%=pkg.assets%>css"],
         release : ["<%=pkg.public%>index.tpl.html", "<%=pkg.public%>sass", "<%=pkg.public%>custom-js", "<%=pkg.assets%>css"]
      },

      copy : {
         main : {
            files : [ {
               cwd : "<%=pkg.assets%>",
               expand : true,
               src : [ "**" ],
               dest : "<%=pkg.public%>"
            } ]
         }
      },

      useminPrepare : {
         html : "<%=pkg.public%>index.html",
         options : {
            dest : "<%=pkg.public%>"
         }
      },

      usemin : {
         html : [ "<%=pkg.public%>index.html" ],
         options : {
            publicDirs : [ "<%=pkg.public%>css" ]
         }
      },

      "bower-install-simple" : {
         options : {
            color : true,
            directory : "<%=pkg.public%>bower_components"
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
            directory : "<%=pkg.public%>bower_components"
         },
         target : {
            src : [ "<%=pkg.public%>index.html" ]
         }
      },

      includeSource : {
         options : {
            basePath : "<%=pkg.public%>",
            duplicates : false,
            debug : true
         },
         target : {
            files : {
               "<%=pkg.public%>index.html" : "<%=pkg.public%>index.tpl.html"
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
   grunt.loadNpmTasks("grunt-bower-install-simple");
   grunt.loadNpmTasks("grunt-contrib-uglify");
   grunt.loadNpmTasks("grunt-contrib-concat");
   grunt.loadNpmTasks("grunt-contrib-cssmin");
   grunt.loadNpmTasks("grunt-usemin");
   grunt.loadNpmTasks("grunt-contrib-copy");
   grunt.loadNpmTasks("grunt-contrib-clean");

   grunt.registerTask("html", [ "clean:build", "bower-install-simple", "includeSource:target", "wiredep:target" ]);
   grunt.registerTask("build", [ "clean:build", "copy", "bower-install-simple", "includeSource:target", 
                                 "wiredep:target", "useminPrepare", "concat", "uglify", "cssmin", "usemin", "clean:release" ]);
   grunt.registerTask("dev", [ "clean:build", "copy", "bower-install-simple", "includeSource:target", 
                                 "wiredep:target", "clean:dev" ]);
   grunt.registerTask("run", [ "connect", "watch" ]);
   grunt.registerTask("default", [ "clean:build", "copy" ]);
};