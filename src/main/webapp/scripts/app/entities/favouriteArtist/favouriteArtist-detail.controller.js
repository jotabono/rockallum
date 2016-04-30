'use strict';

angular.module('therockbibleApp')
    .controller('FavouriteArtistDetailController', function ($scope, $rootScope, $stateParams, entity, FavouriteArtist, Band, User) {
        $scope.favouriteArtist = entity;
        $scope.load = function (id) {
            FavouriteArtist.get({id: id}, function(result) {
                $scope.favouriteArtist = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:favouriteArtistUpdate', function(event, result) {
            $scope.favouriteArtist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
