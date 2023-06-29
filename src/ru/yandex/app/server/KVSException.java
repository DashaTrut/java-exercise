package ru.yandex.app.server;

public class KVSException extends RuntimeException{
    public KVSException(String message) {
        super(message);
    }

    public KVSException(String message, Throwable cause) {
        super(message, cause);
    }
}
