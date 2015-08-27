'use strict';

/**
 * @ngdoc overview
 * @name mytodoApp
 * @description
 * # mytodoApp
 *
 * Main module of the application.
 */
var test = angular.module('testApp', [
  'ngRoute',
  'requestModule',
  'serviceModule',
  'ui.bootstrap'
  ]);
test.config(['$routeProvider',
  function($routeProvider) {
    $routeProvider.
      when('/node/:nodePath', {
        templateUrl: 'views/TestView.html',
      }).
      otherwise({
        redirectTo: '/node/1'
      });
  }]);


