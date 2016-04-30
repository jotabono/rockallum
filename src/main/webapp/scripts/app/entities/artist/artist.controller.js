'use strict';

angular.module('therockbibleApp')
    .controller('ArtistController', function ($scope, $state, Artist, ArtistSearch, ParseLinks) {

        $scope.artists = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 0;
        $scope.loadAll = function() {
            Artist.query({page: $scope.page, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                for (var i = 0; i < result.length; i++) {
                    $scope.artists.push(result[i]);
                }
            });
        };
        $scope.reset = function() {
            $scope.page = 0;
            $scope.artists = [];
            $scope.loadAll();
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            ArtistSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.artists = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.reset();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.artist = {
                name: null,
                realName: null,
                bornIn: null,
                age: null,
                bio: null,
                role: null,
                yearsActive: null,
                links: null,
                picture: null,
                id: null
            };
        };
    });
