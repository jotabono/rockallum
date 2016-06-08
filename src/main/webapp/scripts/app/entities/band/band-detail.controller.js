'use strict';

angular.module('therockbibleApp')
    .controller('BandDetailController', function ($scope, $rootScope, $stateParams, entity, Band, Genre, Artist, FavouriteBand, FavouriteAlbum, FavouriteSong, FavouriteLabel, FavouriteArtist, FavouriteReview, Collection, User, Country, Label, Status, Album) {
        $scope.band = entity;
        $scope.load = function (id) {
            Band.get({id: id}, function (result) {
                $scope.band = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:bandUpdate', function (event, result) {
            $scope.band = result;
        });
        $scope.$on('$destroy', unsubscribe);

    })
    .filter('albumType', function () {
        return function (albums, tipo) {
            var albumType;
            var albumsArray = [];

            angular.forEach(albums, function (album) {
                albumType = album.albumTypes.name;
                if (albumType === tipo) {
                    albumsArray.push(album);
                }
            });

            return albumsArray;
        }
    })
    .filter('memberType', function () {
        return function (albums, tipo) {
            var albumType;
            var albumsArray = [];

            angular.forEach(albums, function (album) {
                albumType = album.albumTypes.name;
                if (albumType === tipo) {
                    albumsArray.push(album);
                }
            });

            return albumsArray;
        }
    });
