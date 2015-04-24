define(function(){
    "use strict";
    function OrderHistory(/*id,*/ modificationDate, userComment,status, user/*, order*/){
        /*this.id = id;*/
        this.status = status;
        this.user = user;
        this.modificationDate = modificationDate;
        this.userComment = userComment;
        /*this.order = order;*/

    }

    return OrderHistory;
});