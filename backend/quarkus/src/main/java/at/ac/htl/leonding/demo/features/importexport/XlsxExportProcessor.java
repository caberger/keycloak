package at.ac.htl.leonding.demo.features.importexport;

import java.io.OutputStream;
import java.util.stream.IntStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import at.ac.htl.leonding.demo.features.store.DataRoot;

interface XlsxExportProcessor {
    static void exportTo(OutputStream os) {
        Exporter.export(os);
    }
}
class Exporter {
    static void export(OutputStream os) {
        try (var workbook = new XSSFWorkbook()) {
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
            //userHeaderRow.getRowStyle().setFont(boldFont);
            var idRow = userHeaderRow.createCell(0);
            idRow.setCellValue(UserTableHeaders.id.name());
            idRow.setCellStyle(boldStyle);

            var postsSheet = workbook.createSheet(SheetNames.Post.name());
            var postHeaderRow = postsSheet.createRow(postIndex++);
            var headerIndex = 0;
            postHeaderRow.createCell(headerIndex++).setCellValue(PostTableHeaders.userId.name());
            postHeaderRow.createCell(headerIndex++).setCellValue(PostTableHeaders.title.name());
            postHeaderRow.createCell(headerIndex++).setCellValue(PostTableHeaders.published.name());
            postHeaderRow.createCell(headerIndex++).setCellValue(PostTableHeaders.body.name());
            var it = postHeaderRow.cellIterator();
            while(it.hasNext()) {
                it.next().setCellStyle(boldStyle);
            }
            
            var users = DataRoot.instance().users();
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
                    postRow.createCell(postColumIndex++).setCellValue(post.published());
                    postRow.createCell(postColumIndex++).setCellValue(post.body());
                    postColumns = postColumIndex;
                }
            }
            IntStream.range(0, postColumns).forEach(col -> postsSheet.autoSizeColumn(col));
            userSheet.autoSizeColumn(0);
            workbook.write(os);
            os.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
 }
