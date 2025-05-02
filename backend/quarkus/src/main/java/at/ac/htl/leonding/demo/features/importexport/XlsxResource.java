package at.ac.htl.leonding.demo.features.importexport;

import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Response;

@Path("/export")
@PermitAll // remove this!
@Produces("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")
public class XlsxResource {
    @GET
    public Response export() {
        return Responses.ok(XlsxExportProcessor::exportTo);
    }
}
