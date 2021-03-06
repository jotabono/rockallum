'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('home', {
                parent: 'site',
                url: '/',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/main/main.html',
                        controller: 'MainController',
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('main');
                        return $translate.refresh();
                    }]
                }
            })
            .state('bands', {
                parent: 'site',
                url: '/bands',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/band/bands.html',
                        controller: 'BandController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('band');
                        return $translate.refresh();
                    }]
                }
            })
        .state('favouriteBands', {
            parent: 'site',
            url: '/favouriteBands',
            data: {
                authorities: []
            },
            views: {
                'content@': {
                    templateUrl: 'scripts/app/entities/favouriteBand/favouriteBands.html',
                    controller: 'FavouriteBandController'
                }
            },
            resolve: {
                mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                    $translatePartialLoader.addPart('band');
                    return $translate.refresh();
                }]
            }
        })
            .state('favouriteAlbums', {
                parent: 'site',
                url: '/favouriteAlbums',
                data: {
                    authorities: []
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteAlbum/favouriteAlbums.html',
                        controller: 'FavouriteAlbumController'
                    }
                },
                resolve: {
                    mainTranslatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate,$translatePartialLoader) {
                        $translatePartialLoader.addPart('album');
                        return $translate.refresh();
                    }]
                }
            })
    });
