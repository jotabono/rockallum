'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('favouriteLabel', {
                parent: 'entity',
                url: '/favouriteLabels',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteLabel.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteLabel/favouriteLabels.html',
                        controller: 'FavouriteLabelController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteLabel');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('favouriteLabel.detail', {
                parent: 'entity',
                url: '/favouriteLabel/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.favouriteLabel.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/favouriteLabel/favouriteLabel-detail.html',
                        controller: 'FavouriteLabelDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('favouriteLabel');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'FavouriteLabel', function($stateParams, FavouriteLabel) {
                        return FavouriteLabel.get({id : $stateParams.id});
                    }]
                }
            })
            .state('favouriteLabel.new', {
                parent: 'favouriteLabel',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteLabel/favouriteLabel-dialog.html',
                        controller: 'FavouriteLabelDialogController',
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
                        $state.go('favouriteLabel', null, { reload: true });
                    }, function() {
                        $state.go('favouriteLabel');
                    })
                }]
            })
            .state('favouriteLabel.edit', {
                parent: 'favouriteLabel',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteLabel/favouriteLabel-dialog.html',
                        controller: 'FavouriteLabelDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['FavouriteLabel', function(FavouriteLabel) {
                                return FavouriteLabel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteLabel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('favouriteLabel.delete', {
                parent: 'favouriteLabel',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/favouriteLabel/favouriteLabel-delete-dialog.html',
                        controller: 'FavouriteLabelDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['FavouriteLabel', function(FavouriteLabel) {
                                return FavouriteLabel.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('favouriteLabel', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
