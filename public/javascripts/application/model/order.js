
define(function(){
    "use strict";
    function Order(id, description, customer, recipient, user, processMng, deliveryMng, orderStatus,  date,  price){
        this.id = id;
        this.orderStatus = orderStatus;
        this.user = user;
        //this.processMng = processMng;
        this.processMng = ko.observable();
        this.processMng(processMng);
        //this.deliveryMng = deliveryMng;
        this.deliveryMng = ko.observable();
        this.deliveryMng(deliveryMng);
        this.customer = ko.observable();
        //this.customer = customer;
        this.customer(customer);
        //this.recipient = recipient;
        this.recipient = ko.observable();
        this.recipient(recipient);
        this.date = date;
        this.description = ko.observable(description).extend({ required: true });
        this.price = ko.observable(price).extend({ required: true, number: true });
    }

    return Order;
});