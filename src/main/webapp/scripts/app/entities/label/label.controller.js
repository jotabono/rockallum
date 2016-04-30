'use strict';

angular.module('therockbibleApp')
    .controller('LabelController', function ($scope, $state, Label, LabelSearch, ParseLinks) {

        $scope.labels = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            Label.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.labels = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            LabelSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.labels = result;
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
            $scope.label = {
                name: null,
                foundingDate: null,
                location: null,
                status: null,
                genres: null,
                description: null,
                addNotes: null,
                links: null,
                phone: null,
                address: null,
                onlineshop: null,
                picture: null,
                id: null
            };
        };
    });
