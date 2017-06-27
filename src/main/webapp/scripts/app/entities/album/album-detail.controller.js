'use strict';

angular.module('therockbibleApp')
    .controller('AlbumDetailController', function ($scope, $rootScope, $stateParams, $sce, entity, Album, Song, Review, Label, AlbumTypes, Band) {
        $scope.album = entity;
        $scope.load = function (id) {
            Album.get({id: id}, function(result) {
                $scope.album = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:albumUpdate', function(event, result) {
            $scope.album = result;
        });
        $scope.$on('$destroy', unsubscribe);

        $scope.songsByCurrentAlbum = [];
        $scope.numberOfSongs = 0;

        $scope.loadAllByAlbum = function(id) {
            Song.songsByCurrentAlbum({id: id}, function(result) {
                $scope.songsByCurrentAlbum = result;
                for(var i = 0; i < $scope.songsByCurrentAlbum.length; i++){
                    $scope.numberOfSongs = $scope.numberOfSongs + 1
                }
            });
        };
        $scope.loadAllByAlbum($stateParams.id);
    })
    .filter("toTrusted", function ($sce) {
        return function(value){
            return $sce.trustAsHtml(value);
        }
    });
