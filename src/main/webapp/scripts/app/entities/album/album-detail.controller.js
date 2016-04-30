'use strict';

angular.module('therockbibleApp')
    .controller('AlbumDetailController', function ($scope, $rootScope, $stateParams, entity, Album, Song, Review, Label, AlbumTypes) {
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

    });
