'use strict';

angular.module('therockbibleApp')
    .controller('GenreDetailController', function ($scope, $rootScope, $stateParams, entity, Genre, Band) {
        $scope.genre = entity;
        $scope.load = function (id) {
            Genre.get({id: id}, function(result) {
                $scope.genre = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:genreUpdate', function(event, result) {
            $scope.genre = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
