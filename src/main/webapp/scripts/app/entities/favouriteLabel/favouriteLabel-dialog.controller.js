'use strict';

angular.module('therockbibleApp').controller('FavouriteLabelDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavouriteLabel', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, FavouriteLabel, Band, User) {

        $scope.favouriteLabel = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            FavouriteLabel.get({id : id}, function(result) {
                $scope.favouriteLabel = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:favouriteLabelUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.favouriteLabel.id != null) {
                FavouriteLabel.update($scope.favouriteLabel, onSaveSuccess, onSaveError);
            } else {
                FavouriteLabel.save($scope.favouriteLabel, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
