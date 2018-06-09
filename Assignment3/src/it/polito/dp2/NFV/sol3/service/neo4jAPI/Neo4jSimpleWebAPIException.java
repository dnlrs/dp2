package it.polito.dp2.NFV.sol3.service.neo4jAPI;


public class Neo4jSimpleWebAPIException extends Exception {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    /**
     * The underlying cause of this exception, if any.
     */
    private Exception exception;

    public Neo4jSimpleWebAPIException() {
        super();
    }

    public Neo4jSimpleWebAPIException( String msg ) {
        super( msg );
    }

    public Neo4jSimpleWebAPIException( Exception e ) {
        super( e );
        this.exception = e;
    }

    public Neo4jSimpleWebAPIException( Exception e, String msg ) {
        super( msg, e );
        this.exception = e;
    }

    /**
     * Returns the message for this exception, if any.
     */
    @Override
    public String getMessage() {
        String message = super.getMessage();
        if ((message == null) && (this.exception != null)) {
            message = this.exception.getMessage();
        }
        return message;
    }

    /**
     * Returns the underlying cause of this error, if any.
     *
     * @return Returns the underlying cause of this error, if any.
     */
    public Exception getException() {
        return this.exception;
    }


}
