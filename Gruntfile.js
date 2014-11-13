module.exports = function (grunt) {

  grunt.initConfig({
    pkg: grunt.file.readJSON("package.json"),

    coffee: {
      local: {
        options: {
          bare: true
        },
        expand: true,
        cwd: "<%=pkg.assets%>",
        src: ['custom-js/**/*.coffee', 'modules/**/*.coffee'],
        dest: '<%=pkg.assets%>',
        rename  : function (dest, src) {
          var folder    = src.substring(0, src.lastIndexOf('/'));
          var filename  = src.substring(src.lastIndexOf('/'), src.length);
          filename  = filename.substring(0, filename.lastIndexOf('.'));
          return dest + folder + filename + '.coffee.js';
        }
      },
      release: {
        options: {
          bare: true
        },
        expand: true,
        cwd: "<%=pkg.public%>",
        src: ['custom-js/**/*.coffee', 'modules/**/*.coffee'],
        dest: '<%=pkg.public%>',
        rename  : function (dest, src) {
          var folder    = src.substring(0, src.lastIndexOf('/'));
          var filename  = src.substring(src.lastIndexOf('/'), src.length);
          filename  = filename.substring(0, filename.lastIndexOf('.'));
          return dest + folder + filename + '.coffee.js';
        }
      }
    },

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
        "<%=pkg.public%>**/*.coffee",
        "<%=pkg.public%>sass",
        "<%=pkg.assets%>css",
        "<%=pkg.public%>custom-js",
        "<%=pkg.assets%>css"
      ]
    },

    copy: {
      build: {
        files: [{
          cwd: "<%=pkg.assets%>",
          expand: true,
          src: ["**"],
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
        directory: "<%=pkg.public%>bower_components"
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
  grunt.loadNpmTasks('grunt-ng-annotate');
  grunt.loadNpmTasks('grunt-contrib-coffee');

  grunt.registerTask("build", [
    "clean:build",
    "copy:build",
    "coffee:release",
    "ngAnnotate:main",
    "bower-install-simple",
    "includeSource:target",
    "wiredep:target",
    "useminPrepare",
    "concat",
    "uglify",
    "cssmin",
    "usemin",
    "clean:release"
  ]);

  grunt.registerTask("local", [
    "clean:build",
    "coffee:local",
    "copy",
    "bower-install-simple",
    "includeSource:target",
    "wiredep:target",
    "copy:dev"
  ]);

  // start a http server and serve at folder "assets"
  grunt.registerTask("run", ["connect", "watch"]);
};