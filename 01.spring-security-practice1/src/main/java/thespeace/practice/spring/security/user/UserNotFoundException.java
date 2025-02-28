package thespeace.practice.spring.security.user;

/**
 * <h1>유저를 찾을 수 없을 때 발생하는 Exception</h1>
 */
public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException() {
        super("유저를 찾을 수 없습니다.");
    }
}
