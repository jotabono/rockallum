'use strict';

angular.module('therockbibleApp').controller('CountryDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Country', 'Band', 'Label', 'Artist',
        function($scope, $stateParams, $uibModalInstance, entity, Country, Band, Label, Artist) {

        $scope.country = entity;
        $scope.bands = Band.query();
        $scope.labels = Label.query();
        $scope.artists = Artist.query();
        $scope.load = function(id) {
            Country.get({id : id}, function(result) {
                $scope.country = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:countryUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.country.id != null) {
                Country.update($scope.country, onSaveSuccess, onSaveError);
            } else {
                Country.save($scope.country, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
