package ro.myclass.onlineStoreapi.PDFGenerator;

import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CustomerOrderPDFGenerator {

    List<OrderDetail> orderDetailList;

    public CustomerOrderPDFGenerator(List<OrderDetail> orderDetailList) {
        this.orderDetailList = orderDetailList;
    }

    public void writeTableHeader(PdfPTable table){

        PdfPCell cell = new PdfPCell();

        cell.setBackgroundColor(Color.cyan);

        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        font.setColor(Color.white);

        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Price",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Quantity",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Order Date",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Product name",font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for(OrderDetail orderDetail : orderDetailList){
            table.addCell(String.valueOf(orderDetail.getId()));
            table.addCell(String.valueOf(orderDetail.getPrice()));
            table.addCell(String.valueOf(orderDetail.getQuantity()));
            table.addCell(String.valueOf(orderDetail.getOrder().getId()));
            table.addCell(String.valueOf(orderDetail.getOrder().getOrderDate()));
            table.addCell(String.valueOf(orderDetail.getProduct().getName()));
        }
    }
    public void generate(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        fontTitle.setSize(18);
        fontTitle.setColor(Color.black);

        Paragraph paragraph = new Paragraph("Customer Orders",fontTitle);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        PdfPTable pdfPTable = new PdfPTable(6);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setWidths(new int[]{1,1,3,2,3,3});
        pdfPTable.setSpacingBefore(6);

        writeTableHeader(pdfPTable);
        writeTableData(pdfPTable);
        document.add(paragraph);

        document.add(pdfPTable);

        document.close();
    }
}
