/**
 * Created by antonkw on 24.04.2015.
 */
define(["application/service/roleService",
    "application/util/callback",
    "application/model/role"], function (roleService, Callback, Role) {
    "use strict";

    function RoleListVM() {
        var self = this,
            roles = ko.observableArray(),


            reply;
        var list = function () {
            roleService.list(
                new Callback(function (params) {
                    reply = params.reply;
                    if (reply.status === "SUCCESS") {
                        roles([]);
                        for (var i = 0, lth = reply.data.list.length; i < lth; i++) {
                            var role = reply.data.list[i];
                            roles.push(new Role(role.id, role.name));
                        }
                    }
                }, self, {}),
                new Callback(function (params) {
                    alert(params.reply.responseJSON.data);
                }, self, {})
            )
        };

        return {
            roles: roles,
            list: list
        }
    }

    return new RoleListVM();
});