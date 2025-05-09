package at.ac.htl.leonding.demo.features.backup;

import org.eclipse.store.afs.nio.types.NioFileSystem;
import org.eclipse.store.storage.types.StorageDataConverterTypeBinaryToCsv;

import at.ac.htl.leonding.demo.lib.Store;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/** write a backup of our database to a different folder. */
@Path("/backup")
@Produces(MediaType.APPLICATION_JSON)
public class Backup {
    static final String TARGET = "target";

    @GET
    @PermitAll
    public Response backup() {
        var storageManager = Store.instance();
        storageManager.issueFullBackup(NioFileSystem.New().ensureDirectoryPath(TARGET, "backup"));
        var fileSystem = NioFileSystem.New();
        var connection = storageManager.createConnection();
        var exportResult = connection.exportTypes(
	    fileSystem.ensureDirectoryPath(TARGET, "export"));
        var converter = StorageDataConverterTypeBinaryToCsv.New(
            fileSystem.ensureDirectoryPath(TARGET, "csv"),
            storageManager.typeDictionary());
        converter.convertDataFiles(exportResult.files());
        return Response.ok().build();
    }
}
