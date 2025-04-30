package at.ac.htl.leonding.demo.features.backup;

import org.eclipse.store.afs.nio.types.NioFileSystem;
import org.eclipse.store.storage.embedded.types.EmbeddedStorageManager;
import org.eclipse.store.storage.types.StorageDataConverterTypeBinaryToCsv;
import org.eclipse.store.storage.types.StorageEntityTypeExportStatistics;

import jakarta.annotation.security.PermitAll;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/backup")
@Produces(MediaType.APPLICATION_JSON)
public class Backup {
    @Inject
    EmbeddedStorageManager storageManager;

    @GET
    @PermitAll
    public Response backup() {
        storageManager.issueFullBackup(NioFileSystem.New().ensureDirectoryPath("storage-backup"));
        var fileSystem = NioFileSystem.New();
        var connection = storageManager.createConnection();
        var exportResult = connection.exportTypes(
	    fileSystem.ensureDirectoryPath("storage-export"));
        var converter = StorageDataConverterTypeBinaryToCsv.New(
            fileSystem.ensureDirectoryPath("storage-csv"),
            storageManager.typeDictionary());
        converter.convertDataFiles(exportResult.files());
        return Response.ok().build();
    }
}
