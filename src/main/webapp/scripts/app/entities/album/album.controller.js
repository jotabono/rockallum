'use strict';

angular.module('therockbibleApp')
    .controller('AlbumController', function ($scope, $state, Album, AlbumSearch, ParseLinks) {

        $scope.albums = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Album.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.albums = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            AlbumSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.albums = result;
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
            $scope.album = {
                title: null,
                releaseDate: null,
                catalogId: null,
                numCopies: null,
                format: null,
                addNotes: null,
                recInfo: null,
                independent: null,
                picture: null,
                id: null
            };
        };
    });
