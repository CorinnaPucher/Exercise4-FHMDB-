package at.ac.fhcampuswien.fhmdb.exceptions;

import java.sql.SQLException;

public class DatabaseException extends Exception {
    public DatabaseException() {
        super("Database nix working :(");
    }
    public DatabaseException(String message) {
        super(message);
    }
}
