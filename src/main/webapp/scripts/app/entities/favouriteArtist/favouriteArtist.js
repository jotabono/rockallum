'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favouriteArtist', {
                parent: 'entity',
                url: '/favouriteArtists',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteArtist.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteArtist/favouriteArtists.html',
                        controller: 'FavouriteArtistController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteArtist');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteArtist.detail', {
                parent: 'entity',
                url: '/favouriteArtist/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteArtist.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteArtist/favouriteArtist-detail.html',
                        controller: 'FavouriteArtistDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteArtist');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FavouriteArtist', function($stateParams, FavouriteArtist) {
                        return FavouriteArtist.get({id : $stateParams.id});
                    }]
                }
            })
            .state('favouriteArtist.new', {
                parent: 'favouriteArtist',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteArtist/favouriteArtist-dialog.html',
                        controller: 'FavouriteArtistDialogController',
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
                        $state.go('favouriteArtist', null, { reload: true });
                    }, function() {
                        $state.go('favouriteArtist');
                    })
                }]
            })
            .state('favouriteArtist.edit', {
                parent: 'favouriteArtist',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteArtist/favouriteArtist-dialog.html',
                        controller: 'FavouriteArtistDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FavouriteArtist', function(FavouriteArtist) {
                                return FavouriteArtist.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteArtist', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('favouriteArtist.delete', {
                parent: 'favouriteArtist',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteArtist/favouriteArtist-delete-dialog.html',
                        controller: 'FavouriteArtistDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FavouriteArtist', function(FavouriteArtist) {
                                return FavouriteArtist.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteArtist', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
