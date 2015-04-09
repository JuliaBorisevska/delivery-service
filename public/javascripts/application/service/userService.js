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

        self.remove = function(ids, success, error, done) {
            baseService.send(
                "/user/" + ids,
                "DELETE",
                {},
                success,
                error,
                done
            );
        };
        
        return {
            list: self.list,
            remove: self.remove
        }
    }

    return new UserService();
});