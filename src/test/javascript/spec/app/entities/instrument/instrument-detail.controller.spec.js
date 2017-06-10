'use strict';

describe('Controller Tests', function() {

    describe('Instrument Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockInstrument, MockArtist;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockInstrument = jasmine.createSpy('MockInstrument');
            MockArtist = jasmine.createSpy('MockArtist');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Instrument': MockInstrument,
                'Artist': MockArtist
            };
            createController = function() {
                $injector.get('$controller')("InstrumentDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:instrumentUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
