'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteLabelDetailController', function ($scope, $rootScope, $stateParams, entity, FavouriteLabel, Band, User) {
        $scope.favouriteLabel = entity;
        $scope.load = function (id) {
            FavouriteLabel.get({id: id}, function(result) {
                $scope.favouriteLabel = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:favouriteLabelUpdate', function(event, result) {
            $scope.favouriteLabel = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
