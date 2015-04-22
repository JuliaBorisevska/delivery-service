define([
    "application/service/orderService",
    "application/util/callback"],
    function(orderService, Callback) {
    "use strict";

    function OrderDetailsVM() {
        var self = this,
            reply,
            order = ko.observable(),
            statusList = ko.observableArray();
            
        var setOrder = function(ord, list){
                order(ord);
                for(var i = 0, lth = list.length; i < lth; i++) {
                	statusList.push(list[i]);
                }
        };


        return {
            setOrder: setOrder,
            order: order
        }
    }

    return new OrderDetailsVM();
});