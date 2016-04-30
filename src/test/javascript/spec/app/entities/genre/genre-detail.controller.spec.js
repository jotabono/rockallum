'use strict';

describe('Controller Tests', function() {

    describe('Genre Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockGenre, MockBand;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockGenre = jasmine.createSpy('MockGenre');
            MockBand = jasmine.createSpy('MockBand');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Genre': MockGenre,
                'Band': MockBand
            };
            createController = function() {
                $injector.get('$controller')("GenreDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:genreUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
