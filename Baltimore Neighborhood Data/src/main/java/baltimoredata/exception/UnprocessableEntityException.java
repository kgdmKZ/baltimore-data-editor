package baltimoredata.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
public class UnprocessableEntityException extends RuntimeException {
     public UnprocessableEntityException(String linkedEntityType, String linkedEntityField, String linkedEntityVal) {
    	 super("The linked " + linkedEntityType + " resource where " + linkedEntityField +" = " + linkedEntityVal
    			 + "must already exist. Create it or select an alternative.");
     }
}
