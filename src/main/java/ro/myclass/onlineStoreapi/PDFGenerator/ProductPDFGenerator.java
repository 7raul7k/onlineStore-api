package ro.myclass.onlineStoreapi.PDFGenerator;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ro.myclass.onlineStoreapi.models.Product;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class ProductPDFGenerator {

    private List<Product> productList;

    public ProductPDFGenerator(List<Product> productList) {
        this.productList = productList;
    }

    public void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.CYAN);

        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        font.setColor(Color.white);
        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Name",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Price",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Image",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Stock",font));
        table.addCell(cell);
    }

    public void writeTableData(PdfPTable table){
        for(Product product : productList){
            table.addCell(String.valueOf(product.getId()));
            table.addCell(String.valueOf(product.getImage()));
            table.addCell(String.valueOf(product.getPrice()));
            table.addCell(String.valueOf(product.getImage()));
            table.addCell(String.valueOf(product.getStock()));
        }

    }

    public void generate(HttpServletResponse response)throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.COURIER);
        fontTitle.setSize(20);
        fontTitle.setColor(Color.black);

        Paragraph paragraph = new Paragraph("Product List",fontTitle);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        PdfPTable pdfPTable = new PdfPTable(5);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setWidths(new int[]{1,1,3,2,4});
        pdfPTable.setSpacingBefore(5);

        writeTableHeader(pdfPTable);
        writeTableData(pdfPTable);

        document.add(pdfPTable);

        document.close();
    }

}
