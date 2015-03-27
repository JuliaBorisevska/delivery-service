
define(function() {
    "use strict";
    function User (firstName, role) {
        var self = this;
        self.firstName = firstName;
        self.role = role;
    }

    return User;
});
