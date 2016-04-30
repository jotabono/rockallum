'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favouriteAlbum', {
                parent: 'entity',
                url: '/favouriteAlbums',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteAlbum.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteAlbum/favouriteAlbums.html',
                        controller: 'FavouriteAlbumController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteAlbum');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteAlbum.detail', {
                parent: 'entity',
                url: '/favouriteAlbum/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteAlbum.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteAlbum/favouriteAlbum-detail.html',
                        controller: 'FavouriteAlbumDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteAlbum');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FavouriteAlbum', function($stateParams, FavouriteAlbum) {
                        return FavouriteAlbum.get({id : $stateParams.id});
                    }]
                }
            })
            .state('favouriteAlbum.new', {
                parent: 'favouriteAlbum',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteAlbum/favouriteAlbum-dialog.html',
                        controller: 'FavouriteAlbumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    liked: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteAlbum', null, { reload: true });
                    }, function() {
                        $state.go('favouriteAlbum');
                    })
                }]
            })
            .state('favouriteAlbum.edit', {
                parent: 'favouriteAlbum',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteAlbum/favouriteAlbum-dialog.html',
                        controller: 'FavouriteAlbumDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FavouriteAlbum', function(FavouriteAlbum) {
                                return FavouriteAlbum.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteAlbum', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('favouriteAlbum.delete', {
                parent: 'favouriteAlbum',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteAlbum/favouriteAlbum-delete-dialog.html',
                        controller: 'FavouriteAlbumDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FavouriteAlbum', function(FavouriteAlbum) {
                                return FavouriteAlbum.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteAlbum', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
