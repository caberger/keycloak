package at.ac.htl.leonding.demo.features.importexport;

import java.io.InputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Iterator;
import java.util.ServiceLoader;
import java.util.stream.Stream;

import at.ac.htl.leonding.demo.features.category.CategoryRepository;
import at.ac.htl.leonding.demo.features.user.UserRepository;
import at.ac.htl.leonding.demo.importexport.ExportProcessor;
import at.ac.htl.leonding.demo.importexport.ImportProcessor;
import at.ac.htl.leonding.demo.importexport.ImportResult;
import at.ac.htl.leonding.demo.importexport.Portation;
import at.ac.htl.leonding.demo.lib.Log;
import at.ac.htl.leonding.demo.lib.Responses;
import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

interface ImportExportProvider {
    static <T extends Portation> T load(Class<T> clazz, String type) {
        var locator = ServiceLoader.load(clazz);
        var iterator = locator.iterator();
        T t = null;
        while(iterator.hasNext() && t == null) {
            var next = iterator.next();
            if (next.fileType().equals(type)) {
                t = next;
            }
        }
        return t;
    }
}

@Path("/portation/{type}")
@PermitAll // only for debugging, remove this!
public class ImportExportResource {
    Logger log = Log.log(ImportExportResource.class);

    public static final String MEDIA_TYPE = "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet";
    @GET
    @Produces(MEDIA_TYPE)
    public Response export(@PathParam("type") String type) {
        var processor = ImportExportProvider.load(ExportProcessor.class, type);
        return Responses.ok(processor.export(UserRepository.all(), CategoryRepository.all())::accept)
            .header("Content-Disposition", "attachment; filename=\"export.xlsx\"")
            .build();
    }
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MEDIA_TYPE)
    @POST
    public Response parse(@PathParam("type") String type, InputStream inputStream) {
        var processor = ImportExportProvider.load(ImportProcessor.class, type);
        return switch(processor.parse(inputStream)) {
            case ImportResult.Success ok -> {
                UserRepository.add(ok.users());
                CategoryRepository.save(ok.categories());
                log.log(Level.INFO, "{0} users and {1} categories imported", ok.users().size(), ok.categories().size());
                yield Responses.ok();
            }
            case ImportResult.Failed withError -> Responses.badRequest(withError.exception());
        };
    }
}
