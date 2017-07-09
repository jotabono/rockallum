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
        $scope.reviewsByCurrentAlbum = [];

        $scope.loadAllByAlbum = function(id) {
            Review.reviewsByCurrentAlbum({id: id}, function(result) {
                $scope.reviewsByCurrentAlbum = result;
            });

            Song.songsByCurrentAlbum({id: id}, function(result) {
                $scope.songsByCurrentAlbum = result;
                for(var i = 0; i < $scope.songsByCurrentAlbum.length; i++){
                    $scope.numberOfSongs = $scope.numberOfSongs + 1
                }
            });
        };
        $scope.loadAllByAlbum($stateParams.id);


        $scope.addReviewAlbum = function (){
            Review.addReviewAlbum({id:$stateParams.id}, {mark:$scope.text1, title:$scope.text2, reviewText:$scope.text3}, function(result){
                $scope.reviewsByCurrentAlbum.push(result);
                $scope.test = true;
            });
        }
    })
    .filter("toTrusted", function ($sce) {
        return function(value){
            return $sce.trustAsHtml(value);
        }
    });
