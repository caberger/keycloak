package at.ac.htl.leonding.demo.xlsx;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;
import org.apache.poi.ooxml.POIXMLException;
import org.apache.poi.util.DocumentFormatException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.ac.htl.leonding.demo.importexport.ImportProcessor;
import at.ac.htl.leonding.demo.importexport.ImportResult;
import at.ac.htl.leonding.demo.model.Category;
import at.ac.htl.leonding.demo.model.Post;
import at.ac.htl.leonding.demo.model.User;

class XlsxImportProcessor implements ImportProcessor {
    public ImportResult parse(InputStream is) {
        ImportResult result;
        try (var workbook = new XSSFWorkbook(is)) {
            var categories = CategoryImporter.parseCategories(workbook);
            var users = UserImporter.parseUsers(workbook);
            PostsImporter.parse(workbook, users, categories);
            result = new ImportResult.Success(users, categories);
        } catch (Exception e) {
            result = new ImportResult.Failed(e);
        }
        return result;
    }
}

interface UserImporter {
    static List<User> parseUsers(XSSFWorkbook workbook) {
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
        return users;
    }
}
interface PostsImporter {
    static List<User> parse(XSSFWorkbook workbook, List<User> userList, List<Category> categories) {
        var userMap = new HashMap<UUID, User>();
        userList.forEach(user -> userMap.put(user.id(), user));
        var categoryMap = new HashMap<String, Category>();
        categories.forEach(category -> categoryMap.put(category.name(), category));
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
                throw new DocumentFormatException("date must not be null in row " + row);
            }
            var categoryName = row.getCell(col++).getStringCellValue();
            var category = categoryMap.get(categoryName);
            if (category == null) {
                throw new DocumentFormatException("category not found in row " + row);
            }
            var post = new Post(user, title, body, published == "TRUE", date, category);

            user.posts().add(post);
            lineNumber++;
        }
        var users = new ArrayList<User>(userMap.size());
        users.addAll(userMap.values());

        return users;
    }
}
interface CategoryImporter {
    static List<Category> parseCategories(XSSFWorkbook workbook) {
        // todo: remove copy & paste code or ... do we really need to be better here as an LLM is?
        var sheet = workbook.getSheet(SheetNames.Category.name());
        if (sheet == null) {
            throw new DocumentFormatException("no such sheet: " + SheetNames.Category.name());
        }
        var rowIterator = sheet.rowIterator();
        if (!rowIterator.hasNext()) {
            throw new DocumentFormatException("no header in " + SheetNames.Post.name());
        }
        rowIterator.next(); //TODO: check headers
        var categories = new LinkedList<Category>();
        while (rowIterator.hasNext()) {
            var row = rowIterator.next();
            var col = 0;
            var name = row.getCell(col++).getStringCellValue();
            var description = row.getCell(col++).getStringCellValue();
            categories.add(new Category(name, description));
        }
        return categories;
    }
}
