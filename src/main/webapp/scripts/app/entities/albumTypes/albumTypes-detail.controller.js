'use strict';

angular.module('therockbibleApp')
    .controller('AlbumTypesDetailController', function ($scope, $rootScope, $stateParams, entity, AlbumTypes, Album) {
        $scope.albumTypes = entity;
        $scope.load = function (id) {
            AlbumTypes.get({id: id}, function(result) {
                $scope.albumTypes = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:albumTypesUpdate', function(event, result) {
            $scope.albumTypes = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
