/**
 * Created by antonkw on 24.04.2015.
 */
define(["application/service/baseService"], function (baseService) {

    function RolesService() {
        var self = this;

        self.list = function (success, error, done) {
            baseService.send(
                "/role/list",
                "GET",
                {},
                success,
                error,
                done
            );
        };

        return {
            list: self.list
        }
    }

    return new RolesService();
});