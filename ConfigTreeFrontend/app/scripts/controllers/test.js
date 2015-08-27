/**
 * Created by caradu on 8/21/2015.
 */
'use strict'
var requestModule = angular.module('requestModule', []);

requestModule.controller('RequestController', function ($scope, $http, $routeParams, MainService) {


  //------Request functions
  $scope.read = function () {
    if (!$scope.disabledTabs) {
      MainService.read($scope.currentPath).success(function (data) {
        if (data.Error ==="true") {
          console.log($scope.currentPath);
          alert(data.ErrorMessage);
        } else {
          $scope.node = data;
        }
      })
    }
  };

  $scope.getChildren = function () {
    if (!$scope.disabledTabs) {
      MainService.getChildren($scope.currentPath).success(function (data) {
        if(data.Error === "true") {
          alert(data.ErrorMessage);
        }
         else {
          $scope.childrenResponseMessage = "Children of node " + $scope.currentPath;
        }
        $scope.currentChildren = data;
      });
    }
  };

  $scope.create = function () {
    if (!$scope.disabledTabs) {
      $scope.config._parentPath = $scope.currentPath;
      MainService.create($scope.config).success(function (data) {
        if(data.Error === "true") {
          alert(data.ErrorMessage);
        }
      });
    }
  };

  $scope.update = function () {
    if (!$scope.disabledTabs) {
      MainService.update($scope.node).success(function (data) {
        if(data.Error === "true") {
          alert(data.ErrorMessage);
          $scope.read();
        }
      });
    }
  };

  $scope.delete = function () {
    if (!$scope.disabledTabs) {
      MainService.delete($scope.currentPath).success(function (data) {
        if(data.Error==="true") {
          alert(data.ErrorMessage);
        } else {
          //$scope.currentPath = $scope.currentPath.substring(0, $scope.currentPath.length - 2);
          window.location.href= $scope.previousNode();
        }

      });
    }
  };

  $scope.mergeTopDown = function() {
    if (!$scope.disabledTabs) {
      MainService.mergeTopDown($scope.currentPath).success(function (data) {
        if(data.Error === "true") {
          alert(data.ErrorMessage);
        } else {
          $scope.topDown = data;
        }
      })
    }
  };

  $scope.mergeBottomUp = function() {
    if (!$scope.disabledTabs) {
      MainService.mergeBottomUp($scope.currentPath).success(function (data) {
        if(data.Error === "true") {
          alert(data.ErrorMessage);
        } else {
          $scope.bottomUp = data;
        }
      })
    }
  };

  //-----Utils Functions
  $scope.nodeExists = function () {
    MainService.read($scope.currentPath).success(function (data) {
      if (data.Error ==="true") {
        $scope.disabledTabs = true;
      } else {
        $scope.node = data;
      }
    })
  };
  $scope.previousNode = function () {
    return '#/node/' + $scope.currentPath.substring(0, $scope.currentPath.length - 2);
  };

  $scope.addPropCreate = function () {
    $scope.config.properties.push(
      {
        property: '',
        value: ''
      }
    );
  };

  $scope.removePropCreate = function (config) {
    $scope.config.properties = _.without($scope.config.properties, config);
  };

  $scope.addPropUpdate = function () {
    $scope.node.properties.push(
      {
        property: '',
        value: ''
      }
    )
  };

  $scope.removePropUpdate = function (config) {
    $scope.node.properties = _.without($scope.node.properties, config);
  };

  //-----Initial state
  $scope.currentPath = $routeParams.nodePath;
  $scope.nodeExists();
  var properties = [
    {
      property: '',
      value: ''
    }
  ];
  $scope.config = {};
  $scope.config._name = '';
  $scope.config.properties = properties;

  $scope.tabs = [
    {
      title: 'Read', page: 'views/ReadView.html', disabled: $scope.disabledTabs, read: $scope.read
    },
    {
      title: 'Children', page: 'views/ChildrenView.html', disabled: $scope.disabledTabs, read: $scope.getChildren
    },
    {title: 'Add Node', page: 'views/CreateView.html', disabled: $scope.disabledTabs,},
    {
      title: 'Update', page: 'views/UpdateView.html', disabled: $scope.disabledTabs, read: $scope.read
    },
    {
      title: 'Merge TD', page: 'views/TopDownView.html', disabled: $scope.disabledTabs, read: $scope.mergeTopDown
    },
    {
      title: 'Merge BU', page: 'views/BottomUpView.html', disabled: $scope.disabledTabs, read: $scope.mergeBottomUp
    }
  ];

});
