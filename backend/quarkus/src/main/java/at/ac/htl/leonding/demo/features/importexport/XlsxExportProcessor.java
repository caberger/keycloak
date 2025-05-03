package at.ac.htl.leonding.demo.features.importexport;

import java.io.OutputStream;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Collection;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.IntStream;

import org.apache.poi.xssf.streaming.SXSSFWorkbook;

import at.ac.htl.leonding.demo.features.post.Post;
import at.ac.htl.leonding.demo.features.user.User;

interface XlsxExportProcessor {
    static Consumer<OutputStream> export(Collection<User> users) {
        return new Exporter(users);
    }
}
class Exporter implements Consumer<OutputStream> {
    final Collection<User> users;

    Exporter(Collection<User> someUsersThatShouldBeExported) {
        users = someUsersThatShouldBeExported;
    }
    @Override
    public void accept(OutputStream os) {
        try (var workbook = new SXSSFWorkbook()) {
            export(workbook, os);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    void export(SXSSFWorkbook workbook, OutputStream os) throws Exception {
        var font = workbook.createFont();
        font.setFontHeightInPoints((short) 15);
        font.setBold(true);
        var boldStyle = workbook.createCellStyle();
        var boldFont = workbook.createFont();
        boldFont.setFontHeightInPoints((short) 12);
        boldFont.setBold(true);
        boldStyle.setFont(boldFont);

        var userIndex = 0;
        var postIndex = 0;
        var userSheet = workbook.createSheet(SheetNames.User.name());
        var userHeaderRow = userSheet.createRow(userIndex++);
        userHeaderRow.setRowStyle(boldStyle);
        var idRow = userHeaderRow.createCell(0);
        idRow.setCellValue(UserTableHeaders.id.name());
        idRow.setCellStyle(boldStyle);

        var postsSheet = workbook.createSheet(SheetNames.Post.name());
        var postHeaderRow = postsSheet.createRow(postIndex++);
        
        Consumer<PostTableHeaders[]> createHeaders = headers -> {
            var col = 0;
            for (var header: headers) {
                postHeaderRow.createCell(col++).setCellValue(header.name());
            }
        }; 
        createHeaders.accept(PostTableHeaders.values());
        var it = postHeaderRow.cellIterator();
        while(it.hasNext()) {
            it.next().setCellStyle(boldStyle);
        }
        
        var postColumns = 0; 
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
                if (post.createdAt() == null) {
                    throw new RuntimeException("date must not be null"); 
                }
                var dt = Formatters.dateFormatter.format(post.createdAt());
                postRow.createCell(postColumIndex++).setCellValue(dt);
                postColumns = postColumIndex;
            }
        }
        userSheet.trackAllColumnsForAutoSizing();
        postsSheet.trackAllColumnsForAutoSizing();
        userSheet.autoSizeColumn(0);
        IntStream.range(0, postColumns).forEach(col -> postsSheet.autoSizeColumn(col));
        workbook.write(os);
        os.flush();
    }
 }
