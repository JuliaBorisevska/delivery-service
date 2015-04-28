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
        	personRole,
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
        
        var showContactModal = function(data, event, root, role) {
        	//alert(JSON.stringify(data));
        	personRole = role;
        	switch (personRole){
        	case "customer":
        		root.contactListVM.checkedContact(order().customer().id);
        		//alert(root.contactListVM.checkedContact());
        		break;
        	case "recipient":
        		root.contactListVM.checkedContact(order().recipient().id);
        		break;
        	}
        	root.contactListVM.currentPage(1);
        	root.contactListVM.numbers([]);
        	root.contactListVM.list(root.contactListVM.currentPage(), root.contactListVM.PAGE_SIZE);
            $('#select-contact').modal({
            	  keyboard: false
            });
        };
        
        var showUserModal = function(data, event, root, role) {
        	personRole = role;
        	switch (personRole){
        	case "PROCESS_MNG":
        		root.userlistVM.checkedUserId(order().processMng().id);
        		//alert(root.contactListVM.checkedContact());
        		break;
        	case "DELIVERY_MNG":
        		root.userlistVM.checkedUserId(order().deliveryMng().id);
        		break;
        	}
        	root.userlistVM.numbers([]);
            root.userlistVM.currentPage(1);
    		root.userlistVM.list(root.userlistVM.currentPage(), root.userlistVM.PAGE_SIZE);
            $('#select-user').modal({
            	  keyboard: false
            });
        };
        
        var closeContactModal = function(data, event, root) {
        	switch (personRole){
        	case "customer":
        		order().customer(data);
        		break;
        	case "recipient":
        		order().recipient(data);
        		break;
        	}
        	//alert(JSON.stringify(data));
        	$('#select-contact').modal('hide');
        };
        
        var closeUserModal = function(data, event, root) {
        	switch (personRole){
        	case "PROCESS_MNG":
        		order().processMng(data);
        		break;
        	case "DELIVERY_MNG":
        		order().deliveryMng(data);
        		break;
        	}
        	//alert(JSON.stringify(data));
        	$('#select-user').modal('hide');
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
            changeStatus: changeStatus,
            showContactModal: showContactModal,
            closeContactModal: closeContactModal,
            showUserModal: showUserModal,
            closeUserModal: closeUserModal
        }
    }

    return new OrderDetailsVM();
});