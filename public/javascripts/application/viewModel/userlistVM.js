define(["application/service/userService",
        "application/util/callback",
        "application/model/user"], function(userService, Callback, User) {
    "use strict";

    function UserListVM() {
    	var k;
    	var PAGE_SIZE = 2,
        	SHOW_PAGES = 3,
        	currentPage = ko.observable(1),
        	totalPages = ko.observable(),
        	reply;
        var self = this,
            users = ko.observableArray(),
            numbers = ko.observableArray([]);
        var checkedUsers = ko.observableArray();
        var list = function(page, pageSize) {
                userService.list(page, pageSize,
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                users([]);
                                totalPages(reply.data.totalPages);
                                if (numbers().length==0){
                                	numbers([]);
                                	for (k=1; k<=(totalPages()<SHOW_PAGES?totalPages():SHOW_PAGES); k++){
                            			numbers.push(k);
                            		}
                                }
                                for(var i = 0, lth = reply.data.list.length; i < lth; i++) {
                                    var user = reply.data.list[i];
                                    users.push(new User(user.id,user.firstName, user.lastName, user.middleName, user.roleTitle, user.companyTitle, user.login, user.menu));
                                }
                            }
                        }, self, {}),
                    new Callback(function(params){
                    	alert(params.reply.responseJSON.data);
                        }, self, {})
                )
            };
        var deleteUsers = function() {
        	if (checkedUsers().length<1){
        		alert("Пользователи не выбраны");
        		return;
        	}
        	//alert(checkedUsers());
        	userService.remove(checkedUsers(),
                    new Callback(function(params){
                            reply = params.reply;
                            if(reply.status === "SUCCESS") {
                                currentPage(1);
                                numbers([]);
                                list(currentPage(), PAGE_SIZE);
                            }
                        }, self, {}
                    ),
                    new Callback(function(params){
                    	alert(params.reply.responseJSON.data);
                    }, self, {})
                )
           checkedUsers([]);
        };
        return {
            users: users,
            checkedUsers: checkedUsers,
            numbers: numbers,
            list: list,
            deleteUsers: deleteUsers,
            totalPages: totalPages,
            currentPage: currentPage,
            PAGE_SIZE: PAGE_SIZE,
            SHOW_PAGES: SHOW_PAGES
        }
    }

    return new UserListVM();

});