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
        
        var showConfirmModal = function(status) {
            //self.editedId = record.id;
            //self.name(record.name());
            //self.description(record.description());
            $('#details').modal({
            	  keyboard: false
            });
        };


        return {
            setOrder: setOrder,
            showConfirmModal: showConfirmModal,
            order: order
        }
    }

    return new OrderDetailsVM();
});