package baltimoredata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ConflictException extends RuntimeException {
    public ConflictException(String resourceType, String resourceField, String resourceVal) {
    	super("A " + resourceType + " resource where " + resourceField + " = " + resourceVal + " already exists. "
    			+ "Delete the resource or modify the " + resourceType + " contained in the request.");
    }
}
