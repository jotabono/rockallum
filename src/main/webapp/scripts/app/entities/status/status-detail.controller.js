'use strict';

angular.module('therockbibleApp')
    .controller('StatusDetailController', function ($scope, $rootScope, $stateParams, entity, Status, Band, Artist) {
        $scope.status = entity;
        $scope.load = function (id) {
            Status.get({id: id}, function(result) {
                $scope.status = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:statusUpdate', function(event, result) {
            $scope.status = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
