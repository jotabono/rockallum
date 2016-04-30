'use strict';

describe('Controller Tests', function() {

    describe('FavouriteBand Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFavouriteBand, MockBand, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFavouriteBand = jasmine.createSpy('MockFavouriteBand');
            MockBand = jasmine.createSpy('MockBand');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FavouriteBand': MockFavouriteBand,
                'Band': MockBand,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("FavouriteBandDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:favouriteBandUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
