define(["application/service/baseService"], function(baseService) {
    function UserService(){
        var self = this;

        self.list = function(page, pageSize, success, error, done) {
            baseService.send(
                "/user/list/" + page + "/" + pageSize,
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

    return new UserService();
});