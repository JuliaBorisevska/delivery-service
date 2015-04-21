
define(["application/service/baseService"], function(baseService) {

    function OrderService(){
        var self = this;
/*
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
*/
        self.list = function(page, pageSize, status, success, error, done) {
        	baseService.send(
        		"/order/list/" + page + "/" + pageSize + "/" + status,
                "GET",
                {},
                success,
                error,
                done
            );
        };
        
        self.getStatusList = function(success, error, done){
        	baseService.send(
                    "/status/list",
                    "GET",
                    {},
                    success,
                    error,
                    done
                );
        };

        self.getOrder = function(id, success, error, done) {
            baseService.send(
                "/order/" + id,
                "GET",
                {},
                success,
                error,
                done
            );
        };
        /*
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
        };*/

        return {
            //add: self.add,
            list: self.list,
            getStatusList: self.getStatusList,
            getOrder: self.getOrder
            //update: self.update,
            //remove: self.remove,

        }
    }

    return new OrderService();
});