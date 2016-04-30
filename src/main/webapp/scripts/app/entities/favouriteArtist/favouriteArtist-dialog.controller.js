'use strict';

angular.module('therockbibleApp').controller('FavouriteArtistDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'FavouriteArtist', 'Band', 'User',
        function($scope, $stateParams, $uibModalInstance, entity, FavouriteArtist, Band, User) {

        $scope.favouriteArtist = entity;
        $scope.bands = Band.query();
        $scope.users = User.query();
        $scope.load = function(id) {
            FavouriteArtist.get({id : id}, function(result) {
                $scope.favouriteArtist = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:favouriteArtistUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.favouriteArtist.id != null) {
                FavouriteArtist.update($scope.favouriteArtist, onSaveSuccess, onSaveError);
            } else {
                FavouriteArtist.save($scope.favouriteArtist, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
