package org.androidorm.exceptions;

/**
 * Database sql exception.
 */
public class DatabaseSqlException extends AndroidOrmException {

    private static final long serialVersionUID = 8766048229530057L;

    /**
     * Constructor.
     *
     * @param msg       exception message
     * @param cause     exception cause
     * @param arguments arguments for formating
     */
    public DatabaseSqlException(String msg, Throwable cause, Object... arguments) {
        super(msg, cause, arguments);
    }

}
