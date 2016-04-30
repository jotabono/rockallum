'use strict';

angular.module('therockbibleApp')
    .controller('BandDetailController', function ($scope, $rootScope, $stateParams, entity, Band, Genre, Artist, FavouriteBand, FavouriteAlbum, FavouriteSong, FavouriteLabel, FavouriteArtist, FavouriteReview, Collection, User, Country, Label, Status) {
        $scope.band = entity;
        $scope.load = function (id) {
            Band.get({id: id}, function(result) {
                $scope.band = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:bandUpdate', function(event, result) {
            $scope.band = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
