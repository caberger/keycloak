package at.ac.htl.leonding.demo.features.importexport;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.util.DocumentFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.ac.htl.leonding.demo.features.post.Post;
import at.ac.htl.leonding.demo.features.user.User;

sealed interface XlsxImportResult permits XlsxImportResult.Imported, XlsxImportResult.Failed {
    record Imported(List<User> users) implements XlsxImportResult {}
    record Failed(Exception e) implements XlsxImportResult {}
}
interface XlsxImportProcessor {
    static XlsxImportResult parse(InputStream is) {
        XlsxImportResult result = null;
        try (var workbook = new XSSFWorkbook(is)) {
            result = UserImporter.parseUsers(workbook);
            List<User> users = switch(result) {
                case XlsxImportResult.Imported imported -> imported.users();
                case XlsxImportResult.Failed failed -> List.of();
            };
            if (!users.isEmpty()) {
                result = PostsImporter.parse(workbook, users);
            }
        } catch (Exception e) {
            result = new XlsxImportResult.Failed(e);
        }
        return result;
    }
}
interface UserImporter {
    static XlsxImportResult parseUsers(XSSFWorkbook workbook) {
        var sheet = workbook.getSheet(SheetNames.User.name());
        if (sheet == null) {
            throw new DocumentFormatException("no such sheet: " + SheetNames.User.name());
        }
        var rowIterator = sheet.rowIterator();
        if (!rowIterator.hasNext()) {
            throw new DocumentFormatException("no header in " + SheetNames.User.name());
        }
        var header = rowIterator.next().getCell(0).getStringCellValue();
        if (!header.equals(UserTableHeaders.id.name())) {
            throw new DocumentFormatException("user table header in cell 0 is not " + UserTableHeaders.id.name());
        }
        var users = new ArrayList<User>(sheet.getLastRowNum());
        while (rowIterator.hasNext()) {
            var row = rowIterator.next();
            var id = row.getCell(0).getStringCellValue();
            if (id != null) {
                users.add(new User(UUID.fromString(id)));
            }
        }
        return new XlsxImportResult.Imported(users);
    }
}
interface PostsImporter {
    static XlsxImportResult parse(XSSFWorkbook workbook, List<User> userList) {
        var userMap = new HashMap<UUID, User>();
        userList.forEach(user -> userMap.put(user.id(), user));
        var sheet = workbook.getSheet(SheetNames.Post.name());
        if (sheet == null) {
            throw new DocumentFormatException("no such sheet: " + SheetNames.Post.name());
        }
        var rowIterator = sheet.rowIterator();
        if (!rowIterator.hasNext()) {
            throw new DocumentFormatException("no header in " + SheetNames.Post.name());
        }
        while (rowIterator.hasNext()) {
            var row = rowIterator.next();
            var col = 0;
            var userId = row.getCell(col++).getStringCellValue();
            var title = row.getCell(col++).getStringCellValue();
            var published = row.getCell(col++).getBooleanCellValue();
            var body = row.getCell(col++).getStringCellValue();
            var user = userMap.get(UUID.fromString(userId));
            if (user == null) {
                throw new POIXMLException("Post user user not found: " + userId);
            }
            var post = new Post(title, body, published);
            user.posts().add(post);
        }
        var users = new ArrayList<User>(userMap.size());
        users.addAll(userMap.values());
        return new XlsxImportResult.Imported(users);
    }
}