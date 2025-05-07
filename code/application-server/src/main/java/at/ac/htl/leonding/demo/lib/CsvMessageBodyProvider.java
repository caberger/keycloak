package at.ac.htl.leonding.demo.lib;

import com.fasterxml.jackson.dataformat.csv.CsvGenerator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.MultivaluedMap;
import jakarta.ws.rs.ext.MessageBodyReader;
import jakarta.ws.rs.ext.MessageBodyWriter;
import jakarta.ws.rs.ext.Provider;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.Optional;

@Provider
@Consumes(CsvMessageBodyProvider.MEDIA_TYPE)
@Produces(CsvMessageBodyProvider.MEDIA_TYPE)
public class CsvMessageBodyProvider implements MessageBodyReader<Object>, MessageBodyWriter<Object> {
    public static final String MEDIA_TYPE = "text/csv";
    CsvMapper mapper() {
        return CsvMapper
            .builder()
            .enable(CsvGenerator.Feature.ALWAYS_QUOTE_STRINGS)
            .build();
    }
    @Override
    public boolean isReadable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }
    @Override
    public Object readFrom(Class<Object> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, String> httpHeaders, InputStream entityStream) throws java.io.IOException, jakarta.ws.rs.WebApplicationException {
        var mapper = mapper();
        var obj = mapper.readValue(entityStream, type);
        return obj;
    }
    @Override
    public boolean isWriteable(Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType) {
        return true;
    }

    @Override
    public void writeTo(Object object, Class<?> type, Type genericType, Annotation[] annotations, MediaType mediaType,
            MultivaluedMap<String, Object> httpHeaders, OutputStream entityStream)
            throws IOException, WebApplicationException {
        var mapper = mapper();
        var clazz = objectClass(object, type);
        if (clazz.isPresent()) {
            var schema = mapper.typedSchemaFor(clazz.get()).withHeader();
            mapper.writer(schema).writeValue(entityStream, object);
        }
    }
    private Optional<Class<?>> objectClass(Object object, Class<?> aClass) {
        Optional<Class<?>> csvClass;
        if (object instanceof Collection) {
            var collection = (Collection<?>) object;
            if (collection.isEmpty()) {
                csvClass = Optional.empty();
            } else {
                csvClass = Optional.of(collection.iterator().next().getClass());
            }
        } else {
            csvClass = Optional.of(aClass);
        }
        return csvClass;
    }
}