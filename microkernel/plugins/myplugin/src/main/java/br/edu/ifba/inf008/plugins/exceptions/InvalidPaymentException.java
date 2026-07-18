package br.edu.ifba.inf008.plugins.exceptions;

public class InvalidPaymentException extends Exception {
    public InvalidPaymentException(String message){
        super(message);
    }
}
