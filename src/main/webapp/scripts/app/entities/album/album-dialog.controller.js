'use strict';

angular.module('therockbibleApp').controller('AlbumDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$timeout', 'Upload', 'entity', 'Album', 'Song', 'Review', 'Label', 'AlbumTypes', 'Band',
        function($scope, $stateParams, $uibModalInstance, $timeout, Upload, entity, Album, Song, Review, Label, AlbumTypes, Band) {

        $scope.album = entity;
        $scope.songs = Song.query();
        $scope.reviews = Review.query();
        $scope.labels = Label.query();
        $scope.albumtypess = AlbumTypes.query();
        $scope.bands = Band.query();
        $scope.load = function(id) {
            Album.get({id : id}, function(result) {
                $scope.album = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.uploadPic($scope.picFile, result);
            var picture = result.title+result.id;
            result.picture = picture;
            console.log(result);
            Album.update(result);
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

            $scope.uploadPic = function(file, result) {
                file.upload = Upload.upload({
                    url: 'api/uploadcover',
                    data: {file: file, 'title': result.title+result.id},
                });
                file.upload.then(function (response) {
                    $timeout(function () {
                        file.result = response.data;
                    });
                }, function (response) {
                    if (response.status > 0)
                        $scope.errorMsg = response.status + ': ' + response.data;
                }, function (evt) {
                    // Math.min is to fix IE which reports 200% sometimes
                    file.progress = Math.min(100, parseInt(100.0 * evt.loaded / evt.total));
                });
            }
}]);
