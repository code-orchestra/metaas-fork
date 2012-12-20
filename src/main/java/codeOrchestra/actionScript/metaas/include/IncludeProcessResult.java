package codeOrchestra.actionScript.metaas.include;

import java.util.List;

/**
 * @author Alexander Eliseyev
 */
public class IncludeProcessResult {

    private List<IncludeProcessError> errors = new java.util.ArrayList<IncludeProcessError>();
    private List<String> processedFiles = new java.util.ArrayList<String>();

    public void addNotFoundFile(String file) {
        errors.add(new IncludeProcessError(file));
    }

    public void addParsingError(String file, Throwable t) {
        errors.add(new IncludeProcessError(t, file));
    }

    public List<IncludeProcessError> getErrors() {
        return errors;
    }

    public void addProcessedFile(String file) {
        processedFiles.add(file);
    }

    public List<String> getProcessedFiles() {
        return processedFiles;
    }
}
