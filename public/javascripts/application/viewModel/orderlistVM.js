define(["application/service/orderService",
    "application/util/callback",
    "application/model/order",
    "application/model/status",
    "application/model/contact",
    "application/model/user"], function(orderService, Callback, Order, Status, Contact, User) {
    "use strict";

    function OrderListVM() {
    	var k;
        var self = this,
            orders = ko.observableArray(),
            PAGE_SIZE = 2,
        	SHOW_PAGES = 3,
        	currentPage = ko.observable(1),
        	totalPages = ko.observable(),
            reply,
            numbers = ko.observableArray([]),
        	availableStatuses = ko.observableArray(),
        	//lastStatus,
        	//isStatusChanged,
        	selectedStatus = ko.observable();
        var getStatusList = function(){
        	orderService.getStatusList(
        			new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {
                        	selectedStatus(null);
                        	availableStatuses([]);
                            for(var i = 0, lth = reply.data.statusList.length; i < lth; i++) {
                                var status = reply.data.statusList[i];
                                availableStatuses.push(new Status(status.id, status.title));
                            }
                        }
                    }, self, {}),
                    new Callback(function(params){
                	alert(params.reply.responseJSON.data);
                    }, self, {})
        	)
        };

           var list = function(page, pageSize) {
                orderService.list(page, pageSize, selectedStatus() ? selectedStatus().title : "",
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                            	//debugger;
                            	/*if (isStatusChanged){
                             	   currentPage(1);
                             	   numbers([]);
                             	   isStatusChanged = false;
                         	   }*/
                                orders([]);
                                totalPages(reply.data.totalPages);
                                if (numbers().length==0){
                                	numbers([]);
                                	for (k=1; k<=(totalPages()<SHOW_PAGES?totalPages():SHOW_PAGES); k++){
                            			numbers.push(k);
                            		}
                                }
                                for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                    var order = reply.data.list[i];
                                    orders.push(new Order(order.id, order.description, null, null, null, null, null, order.orderStatus, order.date, order.price, null));
                                }
                            }
                        }, self, {}
                    ),
                    new Callback(function(params){
                    	alert(params.reply.responseJSON.data);
                        }, self, {})
                	)
            };
            /*
            self.ordersByStatus = ko.computed(function(){
            	if (lastStatus!=selectedStatus()){
                    //numbers();
                	list(1, PAGE_SIZE);
            	}
            	lastStatus=selectedStatus();
            	isStatusChanged = true;
            });
*/
            var changeStatus = function(){
            	numbers([]);
            	currentPage(1);
            	totalPages(0);
            	list(currentPage(), PAGE_SIZE);
            };
            
            var goToOrderDetails = function(data, event, root) {
                orderService.getOrder(data.id,
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                var order = reply.data.order;
                                var statuslist = reply.data.statuslist;
                                var orderHistory = reply.data.orderHistory;
                                root.orderDetailsVM.setOrder(
                                		new Order(order.id, order.description,
                                				new Contact(order.customer.contactId, order.customer.firstName, order.customer.lastName, order.customer.middleName,
                                						null, null, null, null, null, null), 
                                				new Contact(order.recipient.contactId, order.recipient.firstName, order.recipient.lastName, order.recipient.middleName,
                                						null, null, null, null, null, null), 
                                        		new User(order.orderMng.id,order.orderMng.firstName, order.orderMng.lastName, order.orderMng.middleName, 
                                        				order.orderMng.roleTitle, order.orderMng.companyTitle, order.orderMng.login, order.orderMng.menu, null),
                                        		new User(order.processMng.id,order.processMng.firstName, order.processMng.lastName, order.processMng.middleName, 
                                        				order.processMng.roleTitle, order.processMng.companyTitle, order.processMng.login, order.processMng.menu, null),
                                                new User(order.deliveryMng.id,order.deliveryMng.firstName, order.deliveryMng.lastName, order.deliveryMng.middleName, 
                                        				order.deliveryMng.roleTitle, order.deliveryMng.companyTitle, order.deliveryMng.login, order.deliveryMng.menu, null),
                                				order.orderStatus, order.date, order.price),
                                		statuslist, orderHistory
                                );
                                location.hash = "ordadd";
                            }
                        }, self, {}
                    ),
                    new Callback(function(params){
                    	alert(params.reply.responseJSON.data);
                        }, self, {}
                    )
                );

            };
            
        return {
            orders: orders,
            list: list,
            changeStatus: changeStatus,
            availableStatuses: availableStatuses,
            selectedStatus: selectedStatus,
            getStatusList: getStatusList,
            goToOrderDetails: goToOrderDetails,
            numbers: numbers,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES
        }
    }

    return new OrderListVM();

});