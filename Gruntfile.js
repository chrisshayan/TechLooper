module.exports = function (grunt) {
  var timestamp = Date.now();
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
          cwd: "<%=pkg.assets%>bower_components/components-font-awesome/",
          expand: true,
          src: ["fonts/**"],
          dest: "<%=pkg.public%>/generate-resources"
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
        publicDirs: ["<%=pkg.public%>css"],
        blockReplacements: {
          js: function (block) {
            return '<script src="' + block.dest + "?v=" + timestamp + '" charset="utf-8"></script>';//'<link rel="stylesheet" href="' + block.dest + '">';
          },
          css: function (block) {
            return '<link rel="stylesheet" href="' + block.dest + "?v=" + timestamp + '">';//'<link rel="stylesheet" href="' + block.dest + '">';
          }
        }
      }
    },

    "bower-install-simple": {
      options: {
        directory: "<%=pkg.public%>bower_components"
      },
      build: {
        options: {
          production: true,
          forceLatest: true
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
        debug: true,
        templates: {
          html: {
            js: '<script src="{filePath}" charset="utf-8"></script>'
          }
        }
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

    cssmin: {
      generated: {
        options: {
          keepSpecialComments: 0
        }
      }
    },

    concat: {
      generated: {
        options: {
          separator: grunt.util.linefeed + ";" + grunt.util.linefeed
        }
      }
    },

    replace: {
      cssConcat: {
        src: ["<%=pkg.public%>generate-resources/*.css"],
        dest: "<%=pkg.public%>generate-resources/",
        replacements: [
          {from: "};", to: "}"},
          {from: "../fonts", to: "fonts"},
          {from: ".tmp/concat/", to: ""}
          //{from: "images/", to: "../images/"}
        ]
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
  grunt.loadNpmTasks('grunt-text-replace');

  grunt.registerTask("build", [
    "clean:build",
    "copy:build",
    "bower-install-simple:build",
    "includeSource:target",
    "wiredep:target",
    "ngAnnotate:main",
    "useminPrepare",
    "concat:generated",
    "uglify:generated",
    "cssmin:generated",
    "usemin",
    "clean:release",
    "copy:font",
    "replace:cssConcat"
  ]);

  grunt.registerTask("local", [
    "clean:build",
    "copy",
    "bower-install-simple:build",
    "includeSource:target",
    "wiredep:target",
    "copy:dev"
  ]);

  // start a http server and serve at folder "assets"
  grunt.registerTask("run", ["connect", "watch"]);
};