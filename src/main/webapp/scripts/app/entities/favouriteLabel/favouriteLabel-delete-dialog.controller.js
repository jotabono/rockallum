'use strict';

angular.module('therockbibleApp')
	.controller('FavouriteLabelDeleteController', function($scope, $uibModalInstance, entity, FavouriteLabel) {

        $scope.favouriteLabel = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FavouriteLabel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
