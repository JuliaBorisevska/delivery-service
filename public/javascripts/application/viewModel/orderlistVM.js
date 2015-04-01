


define(["application/service/orderService",
    "application/util/callback",
    "application/model/order"], function(orderService, Callback, Order) {
    "use strict";

    function ListVM() {
        var self = this,
            orders = ko.observableArray(),
            PAGE_SIZE = 4,
            currentPage = ko.observable(1),
            totalPages = ko.observable(),
            reply,



            setNextStatusBtn = function(id) {

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

            },

            list = function(page, pageSize) {
                orderService.list(page, pageSize,
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                orders([]);
                                totalPages(reply.data.totalPages);
                                for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                    var order = reply.data.list[i];
                                    addOrder(order.id, order.description, order.customer, order.recipient, order.user, order.orderStatus, order.date, order.price, order.nextStatus);
                                }
                            }
                        }, self, {}
                    ),
                    new Callback(function(params){
                            reply = params.reply;
                            var message = reply.responseText ? reply.responseText : reply.statusText;
                            alert(message);
                        }, self, {}
                    )
                )
            },


            addRecord = function(root) {
                root.detailsVM.setOrder(new Order("", "", ""));
                root.goTo("det");
            },
            next = function() {
                var pn = currentPage() < totalPages() ? currentPage() + 1: currentPage();
                currentPage(pn);
                list(currentPage(), PAGE_SIZE);
            },
            previous = function() {
                if(currentPage() <= 1) {
                    currentPage(1);
                    return;
                }
                var pn = currentPage() > 1 ? currentPage() - 1 : 1;
                currentPage(pn);
                list(currentPage(), PAGE_SIZE);
            },
            loadRecord = function(order, event, root) {
                if(event.target.className !== "bm-remove") {

                    orderService.get(order.id,
                        new Callback(function(params){
                                reply = params.reply;
                                if(reply.status === "SUCCESS") {
                                    root.detailsVM.setOrder(new Order(reply.data.id, reply.data.description,
                                        reply.data.customer, reply.data.recipient, reply.data.user,
                                        reply.data.orderStatus, reply.data.date, reply.data.price, reply.data.nextStatus));
                                    root.goTo("det");
                                }
                            }, self, {}
                        ),
                        new Callback(function(params){
                                reply = params.reply;
                                var message = reply.responseText ? reply.responseText : reply.statusText;
                                alert(message);
                            }, self, {}
                        )
                    )
                }
            },
            deleteOrder = function(order) {
                orderService.remove(order.id,
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
            },
            addOrder = function(id, description, customer, recipient, user, orderStatus,  date,  price, nextStatus) {
                orders.push(new Order(id, description, customer, recipient, user, orderStatus,  date,  price, nextStatus));
            };

        return {
            orders: orders,
            list: list,
            previous: previous,
            next: next,
            addOrder: addOrder,
            loadRecord: loadRecord,
            addRecord: addRecord,
            deleteOrder: deleteOrder,
            setNextStatusBtn: setNextStatusBtn,

            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE
        }
    }

    return new ListVM();

});