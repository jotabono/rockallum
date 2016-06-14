'use strict';

angular.module('therockbibleApp')
    .controller('ReviewDetailController', function ($scope, $rootScope, $stateParams, entity, Review, Album, User, Band) {
        $scope.review = entity;
        $scope.load = function (id) {
            Review.get({id: id}, function(result) {
                $scope.review = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:reviewUpdate', function(event, result) {
            $scope.review = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
