package baltimoredata.configuration;

import java.io.IOException;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import com.bedatadriven.jackson.datatype.jts.JtsModule;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.Module;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;

/* This configuration class is largely adapted from an answer to 
 * https://stackoverflow.com/questions/31913410/spring-data-pagination-returns-no-results-with-jsonview
 */

@Configuration
public class JsonSerializerConfig {
	 @SuppressWarnings("rawtypes")
	 @Bean
     public static Module springDataPageModule() {
         return new SimpleModule().addSerializer(Page.class, new JsonSerializer<Page>() {
            @Override
            public void serialize(Page value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
            	 gen.writeStartObject();
                 gen.writeNumberField("totalElements",value.getTotalElements());
                 gen.writeNumberField("totalPages", value.getTotalPages());
                 gen.writeNumberField("number", value.getNumber());
                 gen.writeNumberField("size", value.getSize());
                 gen.writeBooleanField("first", value.isFirst());
                 gen.writeBooleanField("last", value.isLast());
                 gen.writeFieldName("content");
                 serializers.defaultSerializeValue(value.getContent(),gen);
                 gen.writeEndObject();
            }
        });
    }
	// Added this bean so that Points in Location entities would be serialized properly
	@Bean
	public JtsModule jtsModule() {
	    return new JtsModule();
    }
}
