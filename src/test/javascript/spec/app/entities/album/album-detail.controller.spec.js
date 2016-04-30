'use strict';

describe('Controller Tests', function() {

    describe('Album Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockAlbum, MockSong, MockReview, MockLabel, MockAlbumTypes;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockAlbum = jasmine.createSpy('MockAlbum');
            MockSong = jasmine.createSpy('MockSong');
            MockReview = jasmine.createSpy('MockReview');
            MockLabel = jasmine.createSpy('MockLabel');
            MockAlbumTypes = jasmine.createSpy('MockAlbumTypes');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'Album': MockAlbum,
                'Song': MockSong,
                'Review': MockReview,
                'Label': MockLabel,
                'AlbumTypes': MockAlbumTypes
            };
            createController = function() {
                $injector.get('$controller')("AlbumDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'therockbibleApp:albumUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
