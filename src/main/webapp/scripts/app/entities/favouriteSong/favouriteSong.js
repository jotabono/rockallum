'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favouriteSong', {
                parent: 'entity',
                url: '/favouriteSongs',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteSong.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteSong/favouriteSongs.html',
                        controller: 'FavouriteSongController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteSong');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteSong.detail', {
                parent: 'entity',
                url: '/favouriteSong/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteSong.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteSong/favouriteSong-detail.html',
                        controller: 'FavouriteSongDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteSong');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FavouriteSong', function($stateParams, FavouriteSong) {
                        return FavouriteSong.get({id : $stateParams.id});
                    }]
                }
            })
            .state('favouriteSong.new', {
                parent: 'favouriteSong',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteSong/favouriteSong-dialog.html',
                        controller: 'FavouriteSongDialogController',
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
                        $state.go('favouriteSong', null, { reload: true });
                    }, function() {
                        $state.go('favouriteSong');
                    })
                }]
            })
            .state('favouriteSong.edit', {
                parent: 'favouriteSong',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteSong/favouriteSong-dialog.html',
                        controller: 'FavouriteSongDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FavouriteSong', function(FavouriteSong) {
                                return FavouriteSong.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteSong', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('favouriteSong.delete', {
                parent: 'favouriteSong',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteSong/favouriteSong-delete-dialog.html',
                        controller: 'FavouriteSongDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FavouriteSong', function(FavouriteSong) {
                                return FavouriteSong.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteSong', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
