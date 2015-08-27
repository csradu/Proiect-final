/**
 * Created by caradu on 8/24/2015.
 */
var serviceModule = angular.module('serviceModule', []);

serviceModule.factory('MainService', MainService);

function MainService($http) {
  var factory={};
  factory.read = function(currentPath) {
    return $http.get('http://localhost:8000/read/' + currentPath + '/');
  };
  factory.getChildren = function(currentPath) {
    return $http.get('http://localhost:8000/getChildren/' + currentPath + '/');
  };
  factory.update = function(currentPath) {
    return $http.get('http://localhost:8000/read/' + currentPath + '/');
  };
  factory.mergeTopDown = function(currentPath) {
    return $http.get('http://localhost:8000/mergedRead/' + currentPath + '-TOP_DOWN');
  };
  factory.mergeBottomUp = function(currentPath) {
    return $http.get('http://localhost:8000/mergedRead/' + currentPath + '-BOTTOM_UP');
  };
  factory.create = function(config) {
    return $http.post('http://localhost:8000/create', config);
  };
  factory.update = function(node) {
    return $http.put('http://localhost:8000/update', node);
  };
  factory.delete = function(currentPath) {
    return $http({
      method: 'DELETE',
      url: 'http://localhost:8000/delete',
      data: "{_path : '" + currentPath + "'}",
      headers: {
        'Content-Type': 'application/json'
      }
    });
  }
  return factory;
}
