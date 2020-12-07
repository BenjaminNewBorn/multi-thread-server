package exception;

public class ServerInternalException extends Exception{
    public ServerInternalException(){

    }

    public ServerInternalException(String msg) {
        super(msg);
    }
}
