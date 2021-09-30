package model;

public class AuthToken {

    /**
     * Auth token to return after successful login
     */
    String authToken;

    /**
     * Username associated to the authToken
     */
    String username;

    /**
     * Creates an authToken object
     * @param authToken Auth token to return after successful login
     * @param username Username associated to the authToken
     */
    public AuthToken(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthToken) {
            AuthToken oAuthToken = (AuthToken) o;
            return oAuthToken.getAuthToken().equals(getAuthToken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        } else {
            return false;
        }
    }
}
