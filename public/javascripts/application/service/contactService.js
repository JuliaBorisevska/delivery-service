define(["application/service/baseService"], function(baseService) {
    function ContactService(){
        var self = this;

        self.list = function(page, pageSize, success, error, done) {
            baseService.send(
                "/contact/list/" + page + "/" + pageSize,
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.add = function(contact, success, error, done) {
            baseService.send(
                "/contact",
                "POST",
                contact,
                success,
                error,
                done
            );
        };

        self.get = function(id, success, error, done) {
            baseService.send(
                "/contact/" + id,
                "GET",
                {},
                success,
                error,
                done
            );
        };

        self.update = function(contact, success, error, done) {
            baseService.send(
                "/contact/" + contact.id,
                "PUT",
                contact,
                success,
                error,
                done
            );
        };
        
        self.search = function(page, pageSize, contact, success, error, done) {
            baseService.send(
                "/contact/search/"+ page + "/" + pageSize,
                "POST",
                contact,
                success,
                error,
                done
            );
        };
        
        self.remove = function(ids, success, error, done) {
            baseService.send(
                "/contact/" + ids,
                "DELETE",
                {},
                success,
                error,
                done
            );
        };

        return {
            list: self.list,
            add: self.add,
            get: self.get,
            update: self.update,
            remove: self.remove,
            search: self.search
        }
    }

    return new ContactService();
});
