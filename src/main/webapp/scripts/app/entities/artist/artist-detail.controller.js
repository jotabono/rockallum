'use strict';

angular.module('therockbibleApp')
    .controller('ArtistDetailController', function ($scope, $rootScope, $stateParams, entity, Artist, Band, User, Country, Sex, Status) {
        $scope.artist = entity;
        $scope.load = function (id) {
            Artist.get({id: id}, function(result) {
                $scope.artist = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:artistUpdate', function(event, result) {
            $scope.artist = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
