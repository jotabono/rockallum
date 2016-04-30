'use strict';

angular.module('therockbibleApp').controller('FavouriteReviewDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavouriteReview', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, FavouriteReview, Band, User) {

        $scope.favouriteReview = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            FavouriteReview.get({id : id}, function(result) {
                $scope.favouriteReview = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:favouriteReviewUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.favouriteReview.id != null) {
                FavouriteReview.update($scope.favouriteReview, onSaveSuccess, onSaveError);
            } else {
                FavouriteReview.save($scope.favouriteReview, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
