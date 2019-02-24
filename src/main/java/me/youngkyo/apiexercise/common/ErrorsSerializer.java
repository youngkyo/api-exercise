package me.youngkyo.apiexercise.common;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;
;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {
    @Override
    public void serialize(Errors errors, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {
        jsonGenerator.writeStartArray();
        errors.getFieldErrors().forEach(
                e -> {
                    try {
                        jsonGenerator.writeStartObject();
                        jsonGenerator.writeStringField("objectName", e.getObjectName());
                    } catch (IOException e1) {
                        e1.printStackTrace();
                    }
                }
        );

        errors.getGlobalError();
        jsonGenerator.writeEndArray();
    }
}
