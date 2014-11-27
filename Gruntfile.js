module.exports = function (grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON("package.json"),

    ngAnnotate: {
      main: {
        files: [{
          cwd: "<%=pkg.public%>",
          expand: true,
          src: ['modules/**/*.js'],
          dest: "<%=pkg.public%>"
        }]
      }
    },

    clean: {
      build: ["<%=pkg.public%>"],
      release: [
        "<%=pkg.public%>index.tem.html",
        "<%=pkg.public%>sass",
        "<%=pkg.assets%>css",
        "<%=pkg.public%>custom-js",
        "<%=pkg.public%>css",
        "<%=pkg.public%>bower_components"
      ]
    },

    copy: {
      build: {
        files: [
          {
            cwd: "<%=pkg.assets%>",
            expand: true,
            src: ["**"],
            dest: "<%=pkg.public%>"
          }
        ]
      },
      font: {
        files: [{
          cwd: "<%=pkg.assets%>",
          expand: true,
          src: ["bower_components/components-font-awesome/**"],
          dest: "<%=pkg.public%>"
        }]
      },
      dev: {
        files: [{
          cwd: "<%=pkg.public%>",
          expand: true,
          src: ["css/**", "bower_components/**", "index.html"],
          dest: "<%=pkg.assets%>"
        }]
      }
    },

    useminPrepare: {
      html: "<%=pkg.public%>index.html",
      options: {
        dest: "<%=pkg.public%>"
      }
    },

    usemin: {
      html: ["<%=pkg.public%>index.html"],
      options: {
        publicDirs: ["<%=pkg.public%>css"]
      }
    },

    "bower-install-simple": {
      options: {
        color: true,
        directory: "<%=pkg.public%>bower_components"
      },
      prod: {
        options: {
          production: true
        }
      }
    },

    wiredep: {
      options: {
        color: true,
        directory: "<%=pkg.public%>bower_components",
        fileTypes: {
          html: {
            block: /(([ \t]*)<!--\s*bower:*(\S*)\s*-->)(\n|\r|.)*?(<!--\s*endbower\s*-->)/gi,
            detect: {
              js: /<script.*src=['"]([^'"]+)/gi,
              css: /<link.*href=['"]([^'"]+)/gi
            },
            replace: {
              js: '<script src="{{filePath}}" charset="utf-8"></script>',
              css: '<link rel="stylesheet" href="{{filePath}}" />'
            }
          }
        }
      },
      target: {
        src: ["<%=pkg.public%>index.html"]
      }
    },

    includeSource: {
      options: {
        basePath: "<%=pkg.public%>",
        duplicates: false,
        debug: true
      },
      target: {
        files: {
          "<%=pkg.public%>index.html": "<%=pkg.public%>index.tem.html"
        }
      }
    },

    watch: {
      scripts: {
        files: ["*.js", "*.json"],
        options: {
          livereload: true
        }
      },
      markup: {
        files: ["*.html"],
        options: {
          livereload: true
        }
      },
      stylesheets: {
        files: ["*.css"],
        options: {
          livereload: true
        }
      }
    },

    connect: {
      server: {
        options: {
          port: 8080,
          base: "src/main/webapp/assets",
          keepalive: true
        }
      }
    },

    concat: {
      generated: {
        options: {
          separator: grunt.util.linefeed + ';' + grunt.util.linefeed
        }
      }
    },

    cssmin: {
      generated: {
        options: {
          keepSpecialComments: 0
        }
      }
    },

    uglify: {
      generated: {
        options: {
          preserveComments: false
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
  grunt.loadNpmTasks("grunt-ng-annotate");

  grunt.registerTask("build", [
    "clean:build",
    "copy:build",
    "bower-install-simple",
    "includeSource:target",
    "wiredep:target",
    "ngAnnotate:main",
    "useminPrepare",
    "concat:generated",
    "uglify:generated",
    "cssmin:generated",
    "usemin",
    "clean:release",
    "copy:font"
  ]);

  grunt.registerTask("local", [
    "clean:build",
    "copy",
    "bower-install-simple",
    "includeSource:target",
    "wiredep:target",
    "copy:dev"
  ]);

  // start a http server and serve at folder "assets"
  grunt.registerTask("run", ["connect", "watch"]);
};