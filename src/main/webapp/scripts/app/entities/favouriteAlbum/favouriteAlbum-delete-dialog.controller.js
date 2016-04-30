'use strict';

angular.module('therockbibleApp')
	.controller('FavouriteAlbumDeleteController', function($scope, $uibModalInstance, entity, FavouriteAlbum) {

        $scope.favouriteAlbum = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FavouriteAlbum.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
