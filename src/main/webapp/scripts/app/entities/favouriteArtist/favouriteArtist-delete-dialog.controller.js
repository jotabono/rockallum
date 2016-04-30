'use strict';

angular.module('therockbibleApp')
	.controller('FavouriteArtistDeleteController', function($scope, $uibModalInstance, entity, FavouriteArtist) {

        $scope.favouriteArtist = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FavouriteArtist.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
