'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('instrument', {
                parent: 'entity',
                url: '/instruments',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.instrument.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/instrument/instruments.html',
                        controller: 'InstrumentController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('instrument');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('instrument.detail', {
                parent: 'entity',
                url: '/instrument/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.instrument.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/instrument/instrument-detail.html',
                        controller: 'InstrumentDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('instrument');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Instrument', function($stateParams, Instrument) {
                        return Instrument.get({id : $stateParams.id});
                    }]
                }
            })
            .state('instrument.new', {
                parent: 'instrument',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/instrument/instrument-dialog.html',
                        controller: 'InstrumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    instrument: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('instrument', null, { reload: true });
                    }, function() {
                        $state.go('instrument');
                    })
                }]
            })
            .state('instrument.edit', {
                parent: 'instrument',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/instrument/instrument-dialog.html',
                        controller: 'InstrumentDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Instrument', function(Instrument) {
                                return Instrument.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('instrument', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('instrument.delete', {
                parent: 'instrument',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/instrument/instrument-delete-dialog.html',
                        controller: 'InstrumentDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Instrument', function(Instrument) {
                                return Instrument.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('instrument', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
