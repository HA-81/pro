package com.sdzee.forms;

public class FormValidationException extends Exception {

    private static final long serialVersionUID = 1L;

    /*
     * Constructeurs
     */
    public FormValidationException( String message ) {
        super( message );
    }
}
