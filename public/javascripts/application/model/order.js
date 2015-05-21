
define(function(){
    "use strict";
    function Order(id, description, customer, recipient, user, processMng, deliveryMng, orderStatus,  date,  price){
        this.id = id;
        this.orderStatus = orderStatus;
        this.user = user;
        this.processMng = ko.observable(processMng).extend({
            validation: {
            	validator: function (val) {
                    return val
                },
                message: 'Специалист, обрабатывающий заказ, должен быть выбран'
            }
        });
        this.deliveryMng = ko.observable(deliveryMng).extend({
            validation: {
            	validator: function (val) {
                    return val
                },
                message: 'Менеджер службы доставки должен быть выбран'
            }
        });
        this.customer = ko.observable(customer).extend({
            validation: {
            	validator: function (val) {
                    return val
                },
                message: 'Заказчик должен быть выбран'
            }
        });
        this.recipient = ko.observable(recipient).extend({
            validation: {
            	validator: function (val) {
                    return val
                },
                message: 'Получатель должен быть выбран'
            }
        });
        this.date = date;
        this.description = ko.observable(description).extend({ required: true });
        this.price = ko.observable(price).extend({ required: true, number: true });
    }

    return Order;
});