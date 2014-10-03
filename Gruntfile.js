module.exports = function(grunt) {

   grunt.initConfig({
      pkg : grunt.file.readJSON("package.json"),

      useminPrepare: {
         html: "<%=pkg.assets%>index.html",
         options: {
             dest: "<%=pkg.assets%>"
         }
     },

      usemin : {
         html : [ "<%=pkg.assets%>index.html" ],
         options: {
            assetsDirs: ['<%=pkg.assets%>css']
          }
      },
      

      "bower-install-simple" : {
         options : {
            color : true,
            directory : "<%=pkg.assets%>bower_components"
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
            directory : "<%=pkg.assets%>bower_components"
         },
         target : {
            src : [ "<%=pkg.assets%>index.html" ]
         }
      },

      includeSource : {
         options : {
            basePath : "<%=pkg.assets%>",
            duplicates : false,
            debug : true
         },
         target : {
            files : {
               "<%=pkg.assets%>index.html" : "<%=pkg.assets%>index.tpl.html"
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

   grunt.registerTask("default", [ "bower-install-simple", "includeSource:target", "wiredep:target" ]);
   grunt.registerTask("build", [ "bower-install-simple", "includeSource:target", "wiredep:target", "useminPrepare", 'concat', 'uglify', 'cssmin', 'usemin']);
   grunt.registerTask("run", [ "connect", "watch" ]);
};