package ru.yandex.practicum.requests;

public class CourierLoginRequestBody {
    private String login;
    private String password;

    public CourierLoginRequestBody(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierLoginRequestBody() {}

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
