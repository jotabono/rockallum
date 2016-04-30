'use strict';

angular.module('therockbibleApp')
	.controller('FavouriteSongDeleteController', function($scope, $uibModalInstance, entity, FavouriteSong) {

        $scope.favouriteSong = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FavouriteSong.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
