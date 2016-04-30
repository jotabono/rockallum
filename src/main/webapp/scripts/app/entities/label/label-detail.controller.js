'use strict';

angular.module('therockbibleApp')
    .controller('LabelDetailController', function ($scope, $rootScope, $stateParams, entity, Label, Band, Album, User, Country) {
        $scope.label = entity;
        $scope.load = function (id) {
            Label.get({id: id}, function(result) {
                $scope.label = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:labelUpdate', function(event, result) {
            $scope.label = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
