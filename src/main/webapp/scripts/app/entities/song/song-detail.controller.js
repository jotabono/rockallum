'use strict';

angular.module('therockbibleApp')
    .controller('SongDetailController', function ($scope, $rootScope, $stateParams, entity, Song, Album) {
        $scope.song = entity;
        $scope.load = function (id) {
            Song.get({id: id}, function(result) {
                $scope.song = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:songUpdate', function(event, result) {
            $scope.song = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
