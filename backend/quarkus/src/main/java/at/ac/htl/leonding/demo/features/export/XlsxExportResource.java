package at.ac.htl.leonding.demo.features.export;

import java.io.IOException;
import java.io.OutputStream;

import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.StreamingOutput;

@Path("/export")
@PermitAll // remove this!
@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
public class XlsxExportResource {
    @GET
    public Response export() {
        var output = new StreamingOutput() {
            public void write(OutputStream output) throws IOException, WebApplicationException {
                XlsxExporter.exportTo(output);
            }
        };
        return Responses.ok(output);
    }
}
