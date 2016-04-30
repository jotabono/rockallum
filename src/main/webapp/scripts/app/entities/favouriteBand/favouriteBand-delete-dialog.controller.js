'use strict';

angular.module('therockbibleApp')
	.controller('FavouriteBandDeleteController', function($scope, $uibModalInstance, entity, FavouriteBand) {

        $scope.favouriteBand = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FavouriteBand.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
