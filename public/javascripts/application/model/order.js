
define(function(){
    "use strict";
    function Order(id, description, customer, recipient, user, orderStatus,  date,  price, nextStatus){
        this.id = id;
        this.orderStatus = orderStatus;
        this.user = user;
        this.customer = customer;
        this.recipient = recipient;
        this.date = date;
        this.description = description;
        this.price = price;
        this.nextStatus = nextStatus;
    }

    return Order;
});