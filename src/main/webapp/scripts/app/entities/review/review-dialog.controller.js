'use strict';

angular.module('therockbibleApp').controller('ReviewDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Review', 'Album', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, Review, Album, User) {

        $scope.review = entity;
        $scope.albums = Album.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            Review.get({id : id}, function(result) {
                $scope.review = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:reviewUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.review.id != null) {
                Review.update($scope.review, onSaveSuccess, onSaveError);
            } else {
                Review.save($scope.review, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForReviewDate = {};

        $scope.datePickerForReviewDate.status = {
            opened: false
        };

        $scope.datePickerForReviewDateOpen = function($event) {
            $scope.datePickerForReviewDate.status.opened = true;
        };
}]);
