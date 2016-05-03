'use strict';

angular.module('therockbibleApp').controller('BandDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', '$rootScope', 'entity', 'Band', 'Genre', 'Artist', 'FavouriteBand', 'FavouriteAlbum', 'FavouriteSong', 'FavouriteLabel', 'FavouriteArtist', 'FavouriteReview', 'Collection', 'User', 'Country', 'Label', 'Status', 'NgMap',
        function($scope, $stateParams, $uibModalInstance, $rootScope, entity, Band, Genre, Artist, FavouriteBand, FavouriteAlbum, FavouriteSong, FavouriteLabel, FavouriteArtist, FavouriteReview, Collection, User, Country, Label, Status, NgMap) {

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

        var unsubscribe = $rootScope.$on('therockbibleApp:artistUpdate', function(event, result) {
            $scope.artists = Artist.query();
        });

        var unsubscribe2 = $rootScope.$on('therockbibleApp:labelUpdate', function(event, result) {
            $scope.labels = Label.query();
        });

        $scope.save = function () {
            $scope.band.location = $scope.loc;
            $scope.band.latitude = $scope.lat;
            $scope.band.longitude = $scope.lng;

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

            $scope.lat = [];
            $scope.lng = [];
        var vm = this;
        vm.placeChanged = function() {
            vm.place = this.getPlace();
            console.log('location', vm.place.geometry.location);
            vm.map.setCenter(vm.place.geometry.location);
            $scope.loc = vm.address;
            $scope.lat = vm.place.geometry.location.lat();
            $scope.lng = vm.place.geometry.location.lng();
        }

        NgMap.getMap().then(function(map) {
            vm.map = map;
        });

        }]);
