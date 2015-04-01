package controllers;

import play.mvc.Controller;

public class BaseController extends Controller {


    public static class Reply<T> {
        public Status status;
        public T data;

        public Reply() {
            this.status = Status.ERROR;
        }

        public Reply(Status status, T data) {
            this.status = status;
            this.data = data;
        }
    }

    public static enum Status{
        SUCCESS,
        ERROR
    }

}
