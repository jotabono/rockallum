'use strict';

angular.module('therockbibleApp').controller('FavouriteAlbumDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavouriteAlbum', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, FavouriteAlbum, Band, User) {

        $scope.favouriteAlbum = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            FavouriteAlbum.get({id : id}, function(result) {
                $scope.favouriteAlbum = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:favouriteAlbumUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.favouriteAlbum.id != null) {
                FavouriteAlbum.update($scope.favouriteAlbum, onSaveSuccess, onSaveError);
            } else {
                FavouriteAlbum.save($scope.favouriteAlbum, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
