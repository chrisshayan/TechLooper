require.config({
    baseUrl: "js",    
    paths: {
        'angular': 'bower_components/angular/angular.min'
    },
    shim: { 'angularAMD': ['angular'], 'angular-route': ['angular'] },
    deps: ['app']
});