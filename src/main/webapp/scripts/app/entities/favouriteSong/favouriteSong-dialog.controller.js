'use strict';

angular.module('therockbibleApp').controller('FavouriteSongDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavouriteSong', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, FavouriteSong, Band, User) {

        $scope.favouriteSong = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            FavouriteSong.get({id : id}, function(result) {
                $scope.favouriteSong = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:favouriteSongUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.favouriteSong.id != null) {
                FavouriteSong.update($scope.favouriteSong, onSaveSuccess, onSaveError);
            } else {
                FavouriteSong.save($scope.favouriteSong, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
