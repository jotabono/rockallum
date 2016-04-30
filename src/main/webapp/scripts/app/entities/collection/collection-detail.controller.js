'use strict';

angular.module('therockbibleApp')
    .controller('CollectionDetailController', function ($scope, $rootScope, $stateParams, entity, Collection, Band, User) {
        $scope.collection = entity;
        $scope.load = function (id) {
            Collection.get({id: id}, function(result) {
                $scope.collection = result;
            });
        };
        var unsubscribe = $rootScope.$on('therockbibleApp:collectionUpdate', function(event, result) {
            $scope.collection = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
