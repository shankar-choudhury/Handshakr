package com.SWE2025.project.prototype.user;

public class UserException extends RuntimeException {
    public enum Cause {
        ID_NOT_FOUND("user id not found: "),
        USERNAME_NOT_FOUND("username not found: "),
        EMAIL_NOT_FOUND("user email not found: "),
        USERNAME_ALREADY_EXISTS("username already exists: "),
        EMAIL_ALREADY_EXISTS("email already exists: ");

        private final String description;
        Cause(String description) {this.description = description;}

        /**
         *Gets the description of the cause
         * @param cause The cause enum value.
         * @return A string describing the cause.
         */
        public static String getDescription(Cause cause) {return String.format("%s: %s", cause.name(), cause.description);}
    }

    public final Cause cause;
    public final String explanation;

    public UserException(Cause cause, Object value) {
        this.cause = cause;
        explanation = cause.description + value.toString();
    }
}
