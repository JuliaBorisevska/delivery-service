
define(function(){
    "use strict";
    function Order(id, description, customer, recipient, user, processMng, deliveryMng, orderStatus,  date,  price/*, history*/){
        this.id = id;
        this.orderStatus = orderStatus;
        this.user = user;
        this.processMng = processMng;
        this.deliveryMng = deliveryMng;
        this.customer = customer;
        this.recipient = recipient;
        this.date = date;
        this.description = description;
        this.price = price;
        /*this.history = history;*/
    }

    return Order;
});