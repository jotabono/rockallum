'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteAlbumDetailController', function ($scope, $rootScope, $stateParams, entity, FavouriteAlbum, Band, User) {
        $scope.favouriteAlbum = entity;
        $scope.load = function (id) {
            FavouriteAlbum.get({id: id}, function(result) {
                $scope.favouriteAlbum = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:favouriteAlbumUpdate', function(event, result) {
            $scope.favouriteAlbum = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
