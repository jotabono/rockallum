'use strict';

describe('Controller Tests', function() {

    describe('FavouriteSong Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockFavouriteSong, MockBand, MockUser;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockFavouriteSong = jasmine.createSpy('MockFavouriteSong');
            MockBand = jasmine.createSpy('MockBand');
            MockUser = jasmine.createSpy('MockUser');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'FavouriteSong': MockFavouriteSong,
                'Band': MockBand,
                'User': MockUser
            };
            createController = function() {
                $injector.get('$controller')("FavouriteSongDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:favouriteSongUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
