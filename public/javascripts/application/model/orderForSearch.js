define(function(){
    "use strict";
    function OrderForSearch(customer, recipient, dateMin, dateMax, priceMin, priceMax){
        this.customer = ko.observable(customer).extend({ maxLength: 30 });
        this.recipient = ko.observable(recipient).extend({ maxLength: 30 });
        this.priceMin = ko.observable(priceMin).extend({ number: true });
        this.priceMax = ko.observable(priceMax).extend({ number: true });
        this.dateMin = ko.observable(dateMin).extend({ dateISO: true });
        this.dateMax = ko.observable(dateMax).extend({ dateISO: true });
    }

    return OrderForSearch;
});