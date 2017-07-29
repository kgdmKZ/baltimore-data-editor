package baltimoredata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
	public ResourceNotFoundException(String resourceType, String resourceField, String resourceVal) {
		super("A " + resourceType + " resource where " + resourceField + " = " + resourceVal + "does not exist.");
	}
}
