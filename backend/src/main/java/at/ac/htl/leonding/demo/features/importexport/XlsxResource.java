package at.ac.htl.leonding.demo.features.importexport;

import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;

import at.ac.htl.leonding.demo.features.category.CategoryRepository;
import at.ac.htl.leonding.demo.features.user.UserRepository;
import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/portation/xlsx")
@PermitAll // remove this!
public class XlsxResource {
    public static final String MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @Inject
    Logger log;

    @GET
    @Produces(MEDIA_TYPE)
    public Response export() {
        return Responses.ok(XlsxExportProcessor.export(UserRepository.all(), CategoryRepository.all())::accept)
            .header("Content-Disposition", "attachment; filename=\"export.xlsx\"")
            .build();
    }
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MEDIA_TYPE)
    @POST
    public Response parse(InputStream inputStream) {
        return switch(XlsxImportProcessor.parse(inputStream)) {
            case XlsxImportProcessor.Result.Success ok -> {
                UserRepository.add(ok.users());
                CategoryRepository.save(ok.categories());
                log.log(Level.INFO, "{0} users and {1} categories imported", ok.users().size(), ok.categories().size());
                yield Responses.ok();
            }
            case XlsxImportProcessor.Result.Failed withError -> Responses.badRequest(withError.exception());
        };
    }
}
