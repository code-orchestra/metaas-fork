package codeOrchestra.actionScript.metaas.include;

/**
 * @author Alexander Eliseyev
 */
public class IncludeProcessError {

    private IncludeProcessErrorType type;
    private String filePath;
    private Throwable exception;

    public IncludeProcessError(String filePath) {
        this.filePath = filePath;
        this.type = IncludeProcessErrorType.FILE_NOT_FOUND;
    }

    public IncludeProcessError(Throwable exception, String filePath) {
        this.exception = exception;
        this.filePath = filePath;
        this.type = IncludeProcessErrorType.ERROR_PARSING;
    }

    public Throwable getException() {
        return exception;
    }

    public String getFilePath() {
        return filePath;
    }

    public IncludeProcessErrorType getType() {
        return type;
    }
}
