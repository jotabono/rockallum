'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favouriteBand', {
                parent: 'entity',
                url: '/favouriteBands',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteBand.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteBand/favouriteBands.html',
                        controller: 'FavouriteBandController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteBand');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteBand.detail', {
                parent: 'entity',
                url: '/favouriteBand/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteBand.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteBand/favouriteBand-detail.html',
                        controller: 'FavouriteBandDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteBand');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FavouriteBand', function($stateParams, FavouriteBand) {
                        return FavouriteBand.get({id : $stateParams.id});
                    }]
                }
            })
            .state('favouriteBand.new', {
                parent: 'favouriteBand',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteBand/favouriteBand-dialog.html',
                        controller: 'FavouriteBandDialogController',
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
                        $state.go('favouriteBand', null, { reload: true });
                    }, function() {
                        $state.go('favouriteBand');
                    })
                }]
            })
            .state('favouriteBand.edit', {
                parent: 'favouriteBand',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteBand/favouriteBand-dialog.html',
                        controller: 'FavouriteBandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FavouriteBand', function(FavouriteBand) {
                                return FavouriteBand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteBand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('favouriteBand.delete', {
                parent: 'favouriteBand',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteBand/favouriteBand-delete-dialog.html',
                        controller: 'FavouriteBandDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FavouriteBand', function(FavouriteBand) {
                                return FavouriteBand.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteBand', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
