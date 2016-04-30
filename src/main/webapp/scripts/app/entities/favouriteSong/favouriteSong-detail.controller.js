'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteSongDetailController', function ($scope, $rootScope, $stateParams, entity, FavouriteSong, Band, User) {
        $scope.favouriteSong = entity;
        $scope.load = function (id) {
            FavouriteSong.get({id: id}, function(result) {
                $scope.favouriteSong = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:favouriteSongUpdate', function(event, result) {
            $scope.favouriteSong = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
