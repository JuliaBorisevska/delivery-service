define([
    "application/service/orderService",
    "application/util/callback"],
    function(orderService, Callback) {
    "use strict";

    function OrderDetailsVM() {
        var self = this,
            reply,
            order = ko.observable();
            
        var setOrder = function(ord){
                order(ord);
            };


        return {
            setOrder: setOrder,
            order: order
        }
    }

    return new OrderDetailsVM();
});