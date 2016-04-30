'use strict';

angular.module('therockbibleApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('country', {
                parent: 'entity',
                url: '/countrys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.country.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/country/countrys.html',
                        controller: 'CountryController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('country');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('country.detail', {
                parent: 'entity',
                url: '/country/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'therockbibleApp.country.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/country/country-detail.html',
                        controller: 'CountryDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('country');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'Country', function($stateParams, Country) {
                        return Country.get({id : $stateParams.id});
                    }]
                }
            })
            .state('country.new', {
                parent: 'country',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/country/country-dialog.html',
                        controller: 'CountryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('country', null, { reload: true });
                    }, function() {
                        $state.go('country');
                    })
                }]
            })
            .state('country.edit', {
                parent: 'country',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/country/country-dialog.html',
                        controller: 'CountryDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['Country', function(Country) {
                                return Country.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('country', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('country.delete', {
                parent: 'country',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/country/country-delete-dialog.html',
                        controller: 'CountryDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['Country', function(Country) {
                                return Country.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('country', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
