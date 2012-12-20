package uk.co.badgersinfoil.metaas;

/**
 * @author Alexander Eliseyev
 */
public class EmptySourceFileException extends SyntaxException {

    public EmptySourceFileException() {
        super("Empty source file");
    }
}
