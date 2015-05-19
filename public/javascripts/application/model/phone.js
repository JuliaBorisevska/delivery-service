/**
 * Created by antonkw on 17.05.2015.
 */
define(function () {
    "use strict";
    function Phone(countryCode, operatorCode, number, type, comment) {
        this.countryCode = countryCode;
        this.operatorCode = operatorCode;
        this.number = number;
        this.type = type;
        this.comment = comment;
    }

    return Phone;
});