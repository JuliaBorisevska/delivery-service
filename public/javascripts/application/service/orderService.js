
define(["application/service/baseService"], function(baseService) {

    function OrderService(){
        var self = this;

        self.changeStatus = function(id, status, comment, success, error, done) {
            baseService.send(
                "/order/status",
                "POST",
                {id: id, status: status, comment: comment},
                success,
                error,
                done
            );
        };
        
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
        
        self.search = function(page, pageSize, status, order, success, error, done) {
        	baseService.send(
        		"/order/search/" + page + "/" + pageSize + "/" + status,
                "POST",
                order,
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
        
        self.getFirstStatus = function(success, error, done){
        	baseService.send(
                    "/status/first",
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

        return {
            add: self.add,
            list: self.list,
            getStatusList: self.getStatusList,
            getOrder: self.getOrder,
            getFirstStatus: self.getFirstStatus,
            changeStatus: self.changeStatus,
            update: self.update,
            search: self.search

        }
    }

    return new OrderService();
});