package uk.co.badgersinfoil.metaas.dom;

/**
 * @author Alexander Eliseyev
 */
public interface ASNamespaceDeclaration extends ScriptElement, CommentableBefore {

    /**
	 * Returns the name of this namespace.
	 */
	String getName();

    /**
	 * Returns a value representing any protection-against-access defined
	 * for this ActionScript field.
	 */
	public Visibility getVisibility();

    /**
     * Returns the namespace URI (if any).
     */
    String getURI();

}
