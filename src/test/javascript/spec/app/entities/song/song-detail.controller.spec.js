'use strict';

describe('Controller Tests', function() {

    describe('Song Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockSong, MockAlbum;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockSong = jasmine.createSpy('MockSong');
            MockAlbum = jasmine.createSpy('MockAlbum');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Song': MockSong,
                'Album': MockAlbum
            };
            createController = function() {
                $injector.get('$controller')("SongDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:songUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
