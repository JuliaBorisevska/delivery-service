define([
    "application/service/orderService",
    "application/util/callback",
    "application/model/orderHistory",
    "application/model/contact"],
    function(orderService, Callback, OrderHistory, Contact) {
    "use strict";

    function OrderDetailsVM() {
        var self = this,
        	reply,
            firstStatus = ko.observable(),
            order = ko.observable(),
            newStatus = ko.observable(),
            statusChangeComment = ko.observable(),
            orderHistoryList = ko.observableArray(),
            statusList = ko.observableArray();
            
        var setOrder = function(ord, list, historyList){
        		statusList([]);
        		orderHistoryList([]);
                order(ord);
                for(var i = 0, lth = list.length; i < lth; i++) {
                	statusList.push(list[i]);
                }
                for(var i = 0, lth = historyList.length; i < lth; i++) {
                	var history = historyList[i];
                	orderHistoryList.push(new OrderHistory(history.date, 
                										   history.comment, 
                										   history.status,
                											new Contact(history.user.contactId, 
                														history.user.firstName,
                														history.user.lastName, 
                														history.user.middleName)));
                }
        };
        
        var getFirstStatus = function(){
        	orderService.getFirstStatus(
        			new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {
                        	firstStatus(reply.data.firstStatus);
                        }
                    }, self, {}),
                    new Callback(function(params){
                	alert(params.reply.responseJSON.data);
                    }, self, {})
        	)
        };
        
        var showConfirmModal = function(status) {
            newStatus(status);
            statusChangeComment("");
            $('#status-confirm').modal({
            	  keyboard: false
            });
        };
        
        var changeStatus = function(data, event, root){
        	$('#status-confirm').modal('hide');
        	orderService.changeStatus(order().id, newStatus(), statusChangeComment(),
        			new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {
                        	alert("Статус был успешно изменен!");
                        	root.orderlistVM.goToOrderDetails(order(), event, root);
                        }
                    }, self, {}),
                    new Callback(function(params){
                	alert(params.reply.responseJSON.data);
                    }, self, {})
        	)
        }

        return {
            setOrder: setOrder,
            showConfirmModal: showConfirmModal,
            order: order,
            firstStatus: firstStatus,
            getFirstStatus: getFirstStatus,
            statusList: statusList,
            orderHistoryList: orderHistoryList,
            newStatus: newStatus,
            statusChangeComment: statusChangeComment,
            changeStatus: changeStatus
        }
    }

    return new OrderDetailsVM();
});