'use strict';

angular.module('therockbibleApp').controller('AlbumDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Album', 'Song', 'Review', 'Label', 'AlbumTypes',
        function($scope, $stateParams, $uibModalInstance, entity, Album, Song, Review, Label, AlbumTypes) {

        $scope.album = entity;
        $scope.songs = Song.query();
        $scope.reviews = Review.query();
        $scope.labels = Label.query();
        $scope.albumtypess = AlbumTypes.query();
        $scope.load = function(id) {
            Album.get({id : id}, function(result) {
                $scope.album = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:albumUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.album.id != null) {
                Album.update($scope.album, onSaveSuccess, onSaveError);
            } else {
                Album.save($scope.album, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForReleaseDate = {};

        $scope.datePickerForReleaseDate.status = {
            opened: false
        };

        $scope.datePickerForReleaseDateOpen = function($event) {
            $scope.datePickerForReleaseDate.status.opened = true;
        };
}]);
