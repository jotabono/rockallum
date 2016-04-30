'use strict';

angular.module('therockbibleApp').controller('BandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'Band', 'Genre', 'Artist', 'FavouriteBand', 'FavouriteAlbum', 'FavouriteSong', 'FavouriteLabel', 'FavouriteArtist', 'FavouriteReview', 'Collection', 'User', 'Country', 'Label', 'Status',
        function($scope, $stateParams, $uibModalInstance, entity, Band, Genre, Artist, FavouriteBand, FavouriteAlbum, FavouriteSong, FavouriteLabel, FavouriteArtist, FavouriteReview, Collection, User, Country, Label, Status) {

        $scope.band = entity;
        $scope.genres = Genre.query();
        $scope.artists = Artist.query();
        $scope.favouritebands = FavouriteBand.query();
        $scope.favouritealbums = FavouriteAlbum.query();
        $scope.favouritesongs = FavouriteSong.query();
        $scope.favouritelabels = FavouriteLabel.query();
        $scope.favouriteartists = FavouriteArtist.query();
        $scope.favouritereviews = FavouriteReview.query();
        $scope.collections = Collection.query();
        $scope.users = User.query();
        $scope.countrys = Country.query();
        $scope.labels = Label.query();
        $scope.statuss = Status.query();
        $scope.load = function(id) {
            Band.get({id : id}, function(result) {
                $scope.band = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('therockbibleApp:bandUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.band.id != null) {
                Band.update($scope.band, onSaveSuccess, onSaveError);
            } else {
                Band.save($scope.band, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForFoundingDate = {};

        $scope.datePickerForFoundingDate.status = {
            opened: false
        };

        $scope.datePickerForFoundingDateOpen = function($event) {
            $scope.datePickerForFoundingDate.status.opened = true;
        };

        }]);
