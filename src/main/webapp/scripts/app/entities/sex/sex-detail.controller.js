'use strict';

angular.module('therockbibleApp')
    .controller('SexDetailController', function ($scope, $rootScope, $stateParams, entity, Sex, Artist) {
        $scope.sex = entity;
        $scope.load = function (id) {
            Sex.get({id: id}, function(result) {
                $scope.sex = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:sexUpdate', function(event, result) {
            $scope.sex = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
