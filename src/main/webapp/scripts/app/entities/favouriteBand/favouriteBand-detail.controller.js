'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteBandDetailController', function ($scope, $rootScope, $stateParams, entity, FavouriteBand, Band, User) {
        $scope.favouriteBand = entity;
        $scope.load = function (id) {
            FavouriteBand.get({id: id}, function(result) {
                $scope.favouriteBand = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:favouriteBandUpdate', function(event, result) {
            $scope.favouriteBand = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
