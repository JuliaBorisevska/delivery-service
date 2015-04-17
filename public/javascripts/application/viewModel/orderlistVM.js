define(["application/service/orderService",
    "application/util/callback",
    "application/model/order",
    "application/model/status"], function(orderService, Callback, Order, Status) {
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
        	lastStatus,
        	isStatusChanged,
        	selectedStatus = ko.observable();
        var getStatusList = function(){
        	orderService.getStatusList(
        			new Callback(function(params){
                        reply = params.reply;
                        if(reply.status === "SUCCESS") {
                        	selectedStatus();
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
        /*
        var setNextStatusBtn = function(id) {

                orderService.setNextStatus(id,
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                currentPage(1);
                                list(currentPage(), PAGE_SIZE);
                            }
                        }, self, {}
                    ),
                    new Callback(function(params){
                        reply = params.reply;
                        var message = reply.responseText ? reply.responseText : reply.statusText;
                        alert(message);
                    }, self, {})
                )

            };
			*/
           var list = function(page, pageSize) {
                orderService.list(page, pageSize, selectedStatus() ? selectedStatus().title : "",
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                            	if (isStatusChanged){
                             	   currentPage(1);
                             	   numbers([]);
                             	   isStatusChanged = false;
                         	   }
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
                                    orders.push(new Order(order.id, order.description,/* order.customer, order.recipient, order.user, */order.orderStatus, order.date, order.price/*, order.nextStatus*/));
                                }
                            }
                        }, self, {}
                    ),
                    new Callback(function(params){
                    	alert(params.reply.responseJSON.data);
                        }, self, {})
                	)
            };
            self.ordersByStatus = ko.computed(function(){
            	if (lastStatus!=selectedStatus()){
                    //numbers();
                	list(1, PAGE_SIZE);
            	}
            	lastStatus=selectedStatus();
            	isStatusChanged = true;
            });

        return {
            orders: orders,
            list: list,
            availableStatuses: availableStatuses,
            selectedStatus: selectedStatus,
            getStatusList: getStatusList,
            numbers: numbers,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES
        }
    }

    return new OrderListVM();

});