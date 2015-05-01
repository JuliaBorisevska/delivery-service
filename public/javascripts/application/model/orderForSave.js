define(function(){
    "use strict";
    function OrderForSave(id, userId, processMngId, deliveryMngId, customerId, recipientId, description, price){
        this.id = id;
        this.userId = userId;
        this.processMngId = processMngId;
        this.deliveryMngId = deliveryMngId;
        this.customerId = customerId;
        this.recipientId = recipientId;
        this.description = description;
        this.price = price;
    }

    return OrderForSave;
});