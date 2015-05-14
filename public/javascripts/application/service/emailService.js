/**
 * Created by antonkw on 09.05.2015.
 */
define(["application/service/baseService"], function (baseService) {

    function EmailService() {
        var self = this;

        self.sending = function (mail, success, error, done) {
            baseService.send(
                "/mail",
                "POST",
                mail,
                success,
                error,
                done
            );
        };

        self.list = function (success, error, done) {
            baseService.send(
                "/mail/listTemplates",
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.reloadTemplates = function (success, error, done) {
            baseService.send(
                "/mail/reloadTemplates",
                "GET",
                {},
                success,
                error,
                done
            );
        };

        return {
            list: self.list,
            sending: self.sending,
            reloadTemplates: self.reloadTemplates
        }
    }

    return new EmailService();
});
