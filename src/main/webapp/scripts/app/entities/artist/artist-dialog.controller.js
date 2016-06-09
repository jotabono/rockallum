'use strict';

angular.module('therockbibleApp').controller('ArtistDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$timeout', 'Upload', 'entity', 'Artist', 'Band', 'User', 'Country', 'Sex', 'Status', 'Social', 'Instrument',
        function($scope, $stateParams, $uibModalInstance, $timeout, Upload, entity, Artist, Band, User, Country, Sex, Status, Social, Instrument) {

            $scope.artist = entity;
            $scope.bands = Band.query();
            $scope.users = User.query();
            $scope.countrys = Country.query();
            $scope.sexs = Sex.query();
            $scope.statuss = Status.query();
            $scope.socials = Social.query();
            $scope.instruments = Instrument.query();
            $scope.load = function(id) {
                Artist.get({id : id}, function(result) {
                    $scope.artist = result;
                });
            };

            var onSaveSuccess = function (result) {
                $scope.uploadPic($scope.picFile, result);
                var picture = result.name+result.id;
                result.picture = picture;
                console.log(result);
                Artist.update(result);
                $scope.$emit('therockbibleApp:artistUpdate', result);
                $uibModalInstance.close(result);
                $scope.isSaving = false;
            };

            var onSaveError = function (result) {
                $scope.isSaving = false;
            };

            $scope.save = function () {
                $scope.isSaving = true;
                if ($scope.artist.id != null) {
                    Artist.update($scope.artist, onSaveSuccess, onSaveError);
                } else {
                    Artist.save($scope.artist, onSaveSuccess, onSaveError);
                }
            };

            $scope.clear = function() {
                $uibModalInstance.dismiss('cancel');
            };
            $scope.datePickerForBornIn = {};

            $scope.datePickerForBornIn.status = {
                opened: false
            };

            $scope.datePickerForBornInOpen = function($event) {
                $scope.datePickerForBornIn.status.opened = true;
            };

            $scope.uploadPic = function(file, result) {
                file.upload = Upload.upload({
                    url: 'api/uploadartistpic',
                    data: {file: file, 'name': result.name+result.id},
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
