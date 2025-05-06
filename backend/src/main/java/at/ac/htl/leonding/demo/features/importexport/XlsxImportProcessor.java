package at.ac.htl.leonding.demo.features.importexport;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.util.DocumentFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.ac.htl.leonding.demo.features.category.Category;
import at.ac.htl.leonding.demo.features.post.Post;
import at.ac.htl.leonding.demo.features.user.User;


interface XlsxImportProcessor {
    sealed interface Result permits Result.Success, Result.Failed {
        record Success(List<User> users) implements Result {}
        record Failed(Exception exception) implements Result {}
    }
    
    static Result parse(InputStream is) {
        Result result;
        try (var workbook = new XSSFWorkbook(is)) {
            result = UserImporter.parseUsers(workbook);
            if (result instanceof Result.Success success) {
                result = PostsImporter.parse(workbook, success.users());
            }
        } catch (Exception e) {
            result = new XlsxImportProcessor.Result.Failed(e);
        }
        return result;
    }
}

interface UserImporter {
    static XlsxImportProcessor.Result parseUsers(XSSFWorkbook workbook) {
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
        return new XlsxImportProcessor.Result.Success(users);
    }
}
interface PostsImporter {
    static XlsxImportProcessor.Result parse(XSSFWorkbook workbook, List<User> userList) {
        var defaultCategory = new Category("not yet", "ready");
        var userMap = new HashMap<UUID, User>();
        userList.forEach(user -> userMap.put(user.id(), user));
        var sheet = workbook.getSheet(SheetNames.Post.name());
        if (sheet == null) {
            throw new DocumentFormatException("no such sheet: " + SheetNames.Post.name());
        }
        var rowIterator = sheet.rowIterator();
        var lineNumber = 0;
        if (!rowIterator.hasNext()) {
            throw new DocumentFormatException("no header in " + SheetNames.Post.name());
        }
        rowIterator.next(); //TODO: check headers
        lineNumber++;
        while (rowIterator.hasNext()) {
            var row = rowIterator.next();
            var col = 0;
            var userId = row.getCell(col++).getStringCellValue();
            if (userId == null || userId.isBlank()) {
                throw new POIXMLException("userId is empty in row: " + lineNumber);
            }
            var user = userMap.get(UUID.fromString(userId));
            if (user == null) {
                throw new POIXMLException("Post user user not found: " + userId);
            }
            var title = row.getCell(col++).getStringCellValue();
            var published = row.getCell(col++).getStringCellValue();
            var body = row.getCell(col++).getStringCellValue();
            var dateTime = row.getCell(col++).getStringCellValue();
            var date = LocalDateTime.parse(dateTime, Formatters.dateFormatter);
            if (date == null) {
                throw new DocumentFormatException("date must not be null");
            }
            var post = new Post(user, title, body, published == "TRUE", date, defaultCategory);
            
            user.posts().add(post);
            lineNumber++;
        }
        var users = new ArrayList<User>(userMap.size());
        users.addAll(userMap.values());
        return new XlsxImportProcessor.Result.Success(users);
    }
}