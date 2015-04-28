define(["application/service/baseService"], function (baseService) {
    function UserService() {
        var self = this;

        self.availableRoles = ko.observableArray(['SUPERVISOR', 'ADMIN', 'ORDER_MNG', 'ORDER_MNG', 'PROCESS_MNG', 'DELIVERY_MNG']);

        self.add = function (user, success, error, done) {
            baseService.send(
                "/user",
                "POST",
                user,
                success,
                error,
                done
            );
        };

        self.list = function (page, pageSize, success, error, done) {
            baseService.send(
                "/user/list/" + page + "/" + pageSize,
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.remove = function (ids, success, error, done) {
            baseService.send(
                "/user/" + ids,
                "DELETE",
                {},
                success,
                error,
                done
            );
        };

        self.showModal = function () {
            $('#select-contact').modal({
                keyboard: false
            });
            return true;
        };

        self.closeModal = function () {
            $('#select-contact').modal('hide');
            return true;
        };

        return {
            add: self.add,
            list: self.list,
            remove: self.remove,
            showModal: self.showModal,
            closeModal: self.closeModal,
            availableRoles: self.availableRoles
        }
    }

    return new UserService();
});