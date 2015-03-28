define(["application/service/baseService"], function(baseService) {
    function InitService(){


        var initSections = function(success, error, done) {
            baseService.send(
                "/init",
                "GET",
                {},
                success,
                error,
                done
            );
        }

        return {
        	initSections: initSections
        }
    }

    return new InitService();
});