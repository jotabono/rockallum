'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('band', {
                parent: 'entity',
                url: '/bands',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.band.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/band/bands.html',
                        controller: 'BandController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('band');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('band.detail', {
                parent: 'entity',
                url: '/band/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.band.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/band/band-detail.html',
                        controller: 'BandDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('band');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Band', function($stateParams, Band) {
                        return Band.get({id : $stateParams.id});
                    }]
                }
            })
            .state('band.new', {
                parent: 'band',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/band/band-dialog.html',
                        controller: 'BandDialogController as vm',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
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
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('band', null, { reload: true });
                    }, function() {
                        $state.go('band');
                    })
                }]
            })
            .state('band.edit', {
                parent: 'band',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/band/band-dialog.html',
                        controller: 'BandDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Band', function(Band) {
                                return Band.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('band', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('band.delete', {
                parent: 'band',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/band/band-delete-dialog.html',
                        controller: 'BandDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Band', function(Band) {
                                return Band.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('band', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
