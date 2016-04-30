'use strict';

describe('Controller Tests', function() {

    describe('Country Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockCountry, MockBand, MockLabel, MockArtist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockCountry = jasmine.createSpy('MockCountry');
            MockBand = jasmine.createSpy('MockBand');
            MockLabel = jasmine.createSpy('MockLabel');
            MockArtist = jasmine.createSpy('MockArtist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Country': MockCountry,
                'Band': MockBand,
                'Label': MockLabel,
                'Artist': MockArtist
            };
            createController = function() {
                $injector.get('$controller')("CountryDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:countryUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
