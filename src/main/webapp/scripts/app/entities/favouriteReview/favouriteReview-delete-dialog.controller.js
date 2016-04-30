'use strict';

angular.module('therockbibleApp')
	.controller('FavouriteReviewDeleteController', function($scope, $uibModalInstance, entity, FavouriteReview) {

        $scope.favouriteReview = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            FavouriteReview.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
