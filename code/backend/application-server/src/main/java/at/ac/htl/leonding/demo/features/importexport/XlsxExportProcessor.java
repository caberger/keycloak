package at.ac.htl.leonding.demo.features.importexport;

import java.io.OutputStream;
import java.lang.System.Logger;
import java.lang.System.Logger.Level;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.stream.Stream;

import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.xssf.streaming.SXSSFRow;
import org.apache.poi.xssf.streaming.SXSSFSheet;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import at.ac.htl.leonding.demo.model.Category;
import at.ac.htl.leonding.demo.model.User;

interface XlsxExportProcessor {
    static Consumer<OutputStream> export(Collection<User> users, Collection<Category> categories) {
        return new Exporter(users, categories);
    }
}
class Exporter implements Consumer<OutputStream> {
    final Collection<User> users;
    final Collection<Category> categories;
    final Logger log = System.getLogger(Exporter.class.getName());

    Exporter(Collection<User> someUsersThatShouldBeExported, Collection<Category> fewCategories) {
        users = someUsersThatShouldBeExported;
        categories = fewCategories;
    }
    @Override
    public void accept(OutputStream os) {
        try (var workbook = new SXSSFWorkbook()) {
            export(workbook);
            workbook.write(os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    CellStyle createHeaderStyle(SXSSFWorkbook workbook) {
        var boldStyle = workbook.createCellStyle();
        var boldFont = workbook.createFont();
        boldFont.setFontHeightInPoints((short) 12);
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);
        return boldStyle;
    }
    void addHeaders(SXSSFWorkbook workbook, SXSSFRow row, Collection<String> headers) {
        var col = 0;
        for (var header: headers) {
            row.createCell(col++).setCellValue(header);
        }
        var boldStyle = createHeaderStyle(workbook);
        var it = row.cellIterator();
        while(it.hasNext()) {
            it.next().setCellStyle(boldStyle);
        }

}
    void export(SXSSFWorkbook workbook) throws Exception {
        var font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setBold(true);
        var boldStyle = createHeaderStyle(workbook);

        var userIndex = 0;
        var postIndex = 0;
        var userSheet = workbook.createSheet(SheetNames.User.name());
        var userHeaderRow = userSheet.createRow(userIndex++);
        addHeaders(workbook, userHeaderRow, Stream.of(UserTableHeaders.values()).map(UserTableHeaders::name).toList());

        var postsSheet = workbook.createSheet(SheetNames.Post.name());
        var postHeaderRow = postsSheet.createRow(postIndex++);

        addHeaders(workbook, postHeaderRow, Stream.of(PostTableHeaders.values()).map(PostTableHeaders::name).toList());
        var it = postHeaderRow.cellIterator();
        while(it.hasNext()) {
            it.next().setCellStyle(boldStyle);
        }

        for (var user: users) {
            var userId = user.id().toString();
            var userRow = userSheet.createRow(userIndex++);
            var userColumIndex = 0;
            userRow.createCell(userColumIndex++).setCellValue(userId);
            for (var post: user.posts()) {
                var postColumIndex = 0;
                var postRow = postsSheet.createRow(postIndex++);
                postRow.createCell(postColumIndex++).setCellValue(userId);
                postRow.createCell(postColumIndex++).setCellValue(post.title());
                postRow.createCell(postColumIndex++).setCellValue(post.published() ? "TRUE" : "FALSE");
                postRow.createCell(postColumIndex++).setCellValue(post.body());
                var date = post.createdAt();
                if (date == null) {
                    log.log(Level.INFO, "date must not be null {0} {1}", user.id(), post.title());
                }
                var dt = Formatters.dateFormatter.format(date);
                if (dt == null) {
                    throw new RuntimeException("failed to format date, must not be null");
                }
                postRow.createCell(postColumIndex++).setCellValue(dt);
                postRow.createCell(postColumIndex++).setCellValue(post.category().name());
            }
        }
        exportCategories(categories, workbook);
        //userSheet.trackAllColumnsForAutoSizing();
        //postsSheet.trackAllColumnsForAutoSizing();
        //userSheet.autoSizeColumn(0);
        //IntStream.range(0, postColumns).forEach(col -> postsSheet.autoSizeColumn(col));
    }
    SXSSFSheet exportCategories(Collection<Category> categories, SXSSFWorkbook workbook) {
        var row = 0;
        var sheet = workbook.createSheet(SheetNames.Category.name());
        var headerRow = sheet.createRow(row++);
        headerRow.setRowStyle(createHeaderStyle(workbook));
        addHeaders(workbook, headerRow, Stream.of(CategoryTableHeaders.values()).map(CategoryTableHeaders::name).toList());
        for (var category: categories) {
            var col = 0;
            var categoryRow = sheet.createRow(row++);
            categoryRow.createCell(col++).setCellValue(category.name());
            categoryRow.createCell(col++).setCellValue(category.description());
        }
        return sheet;
    }
 }
