/**
 * Created by antonkw on 09.05.2015.
 */
define(["application/service/emailService",
    "application/util/callback",
    "application/model/template"], function (emailService, Callback, Template) {
    "use strict";

    function EmailListVM() {
        var self = this,
            templates = ko.observableArray([]),


            reply;
        var list = function () {
            emailService.list(
                new Callback(function (params) {
                    reply = params.reply;
                    if (reply.status === "SUCCESS") {
                        templates([]);
                        for (var i = 0, lth = reply.data.list.length; i < lth; i++) {
                            var template = reply.data.list[i];
                            templates.push(new Template(template.title, template.html));
                        }
                    }
                }, self, {}),
                new Callback(function (params) {
                    alert(params.reply.responseJSON.data);
                }, self, {})
            )
        };

        return {
            templates: templates,
            list: list
        }
    }

    return new EmailListVM();
});
