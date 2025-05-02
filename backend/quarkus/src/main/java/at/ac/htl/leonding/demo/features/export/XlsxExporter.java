package at.ac.htl.leonding.demo.features.export;

import java.io.OutputStream;
import java.util.stream.IntStream;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import at.ac.htl.leonding.demo.features.store.DataRoot;

interface XlsxExporter {
    static void exportTo(OutputStream os) {
        new Exporter().export(os);
    }
}
class Exporter {
    void export(OutputStream os) {
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
            var userSheet = workbook.createSheet("User");
            var userHeaderRow = userSheet.createRow(userIndex++);
            userHeaderRow.setRowStyle(boldStyle);
            //userHeaderRow.getRowStyle().setFont(boldFont);
            var idRow = userHeaderRow.createCell(0);
            idRow.setCellValue("id");
            idRow.setCellStyle(boldStyle);

            var postsSheet = workbook.createSheet("Post");
            var postHeaderRow = postsSheet.createRow(postIndex++);
            var headerIndex = 0;
            postHeaderRow.createCell(headerIndex++).setCellValue("userId");
            postHeaderRow.createCell(headerIndex++).setCellValue("title");
            postHeaderRow.createCell(headerIndex++).setCellValue("body");
            postHeaderRow.createCell(headerIndex++).setCellValue("published");
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
                    postRow.createCell(postColumIndex++).setCellValue(post.body());
                    postRow.createCell(postColumIndex++).setCellValue(post.published());
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
