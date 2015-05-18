define(function(){
    "use strict";
    function OrderForSearch(customer, recipient, dateMin, dateMax, priceMin, priceMax){
        this.customer = customer;
        this.recipient = recipient;
        this.priceMin = priceMin;
        this.priceMax = priceMax;
        this.dateMin = dateMin;
        this.dateMax = dateMax;
    }

    return OrderForSearch;
});