package devracom.Mnemosyne.exceptions.Auth;

public class WrongCredentialsException extends RuntimeException{
    public WrongCredentialsException(String msg) {
        super(msg);
    }
}
