
define(["application/service/baseService"], function(baseService) {

    function OrderService(){
        var self = this;

        self.add = function(order, success, error, done) {
            baseService.send(
                "/order",
                "POST",
                order,
                success,
                error,
                done
            );
        };

        self.setNextStatus  = function(id, success, error, done){

            baseService.send(
                "/order/getNextStatus" + id ,
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.list = function(page, pageSize, success, error, done) {
            baseService.send(
                "/order/list/" + page + "/" + pageSize,
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.get = function(id, success, error, done) {
            baseService.send(
                "/order/" + id,
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.update = function(order, success, error, done) {
            baseService.send(
                "/order/" + order.id,
                "PUT",
                order,
                success,
                error,
                done
            );
        };

        self.remove = function(id, success, error, done) {
            baseService.send(
                "/order/" + id,
                "DELETE",
                {},
                success,
                error,
                done
            );
        };

        return {
            add: self.add,
            list: self.list,
            get: self.get,
            update: self.update,
            remove: self.remove,
            setNextStatus: self.setNextStatus

        }
    }

    return new OrderService();
});