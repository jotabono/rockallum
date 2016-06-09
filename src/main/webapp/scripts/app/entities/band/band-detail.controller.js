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

    .filter('liveAlbum', function () {
        return function (albums, tipo, tipo2) {
            var liveAlbum;
            var albumsArray = [];

            angular.forEach(albums, function (album) {
                liveAlbum = album.albumTypes.name;
                if (liveAlbum === tipo || liveAlbum === tipo2) {
                    albumsArray.push(album);
                }
            });

            return albumsArray;
        }
    })

    .filter('miscAlbum', function () {
        return function (albums, tipo, tipo2, tipo3, tipo4, tipo5, tipo6, tipo7) {
            var miscAlbum;
            var albumsArray = [];

            angular.forEach(albums, function (album) {
                miscAlbum = album.albumTypes.name;
                if (miscAlbum === tipo || miscAlbum === tipo2 || miscAlbum === tipo3 || miscAlbum === tipo4 ||
                    miscAlbum === tipo5 || miscAlbum === tipo6 || miscAlbum === tipo7) {
                    albumsArray.push(album);
                }
            });

            return albumsArray;
        }
    })

    .filter('memberStill', function () {
        return function (artists, still) {
            var memberStill;
            var artistsArray = [];

            angular.forEach(artists, function (artist) {
                memberStill = artist.stillInBand;
                if (memberStill === still && artist.liveMusician === false) {
                    artistsArray.push(artist);
                }
            });

            return artistsArray;
        }
    })
    .filter('isLiveMusician', function () {
        return function (artists, live) {
            var isLiveMusician;
            var artistsArray = [];

            angular.forEach(artists, function (artist) {
                isLiveMusician = artist.liveMusician;
                if (isLiveMusician === live) {
                    artistsArray.push(artist);
                }
            });

            return artistsArray;
        }
    });

