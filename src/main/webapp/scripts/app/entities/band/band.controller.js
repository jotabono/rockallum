'use strict';

angular.module('therockbibleApp')
    .controller('BandController', function ($scope, $state, Band, BandSearch, ParseLinks) {

        $scope.bands = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Band.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.bands = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            BandSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.bands = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.band = {
                name: null,
                location: null,
                latitude: null,
                longitude: null,
                foundingDate: null,
                yearsActive: null,
                lyricalThemes: null,
                independent: null,
                picture: null,
                links: null,
                logo: null,
                bio: null,
                id: null
            };
        };
    });
