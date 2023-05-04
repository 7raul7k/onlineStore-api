package ro.myclass.onlineStoreapi.PDFGenerator;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CustomerBillPDFGenerator {

   private Customer customer;

   private List<OrderDetail> orderDetailList;

    public CustomerBillPDFGenerator(Customer customer, List<OrderDetail> orderDetailList) {
        this.customer = customer;
        this.orderDetailList = orderDetailList;
    }

    public void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.cyan);

        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        font.setColor(Color.white);

       cell.setPhrase(new Phrase("Id",font));
       table.addCell(cell);
       cell.setPhrase(new Phrase("Product Name",font));
       table.addCell(cell);
       cell.setPhrase(new Phrase("Product Price",font));
       table.addCell(cell);
       cell.setPhrase(new Phrase("Product quantity",font));
       table.addCell(cell);
    }

    public void writeTableDataOrders(PdfPTable table){
        for(OrderDetail orderDetail : orderDetailList){
            table.addCell(String.valueOf(orderDetail.getId()));
            table.addCell(String.valueOf(orderDetail.getProduct().getName()));
            table.addCell(String.valueOf(orderDetail.getProduct().getPrice()));
            table.addCell(String.valueOf(orderDetail.getQuantity()));
        }

    }
    public void writeTableDataCustomer(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.white);

        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);
        font.setColor(Color.white);

        PdfPCell cell1 = new PdfPCell();
        cell1.setPadding(5);
        cell1.setBackgroundColor(Color.cyan);

        cell1.setPhrase(new Phrase("Customer Information"));
        table.addCell(cell1);
        cell.setPhrase(new Phrase("\n"));
        table.addCell(cell1);
        cell.setPhrase(new Phrase("Full name :"));
        table.addCell(cell);
        cell.setPhrase(new Phrase(customer.getFullName()));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email : " ));
        table.addCell(cell);
        cell.setPhrase(new Phrase(customer.getEmail()));
        table.addCell(cell);
    }

    public void generate(HttpServletResponse response) throws IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());

        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.COURIER);
        fontTitle.setSize(20);
        fontTitle.setColor(Color.black);

        Paragraph paragraph = new Paragraph("Customer Details");
        paragraph.setAlignment(Element.ALIGN_LEFT);

        Paragraph paragraph1 =  new Paragraph("Product List");
        paragraph.setAlignment(Element.ALIGN_CENTER);
        PdfPTable pdfPTable = new PdfPTable(2);
        pdfPTable.setWidthPercentage(100f);
        pdfPTable.setWidths(new int[]{3,2});
        pdfPTable.setSpacingBefore(5);

        PdfPTable pdfPTable1 = new PdfPTable(4);
        pdfPTable1.setWidthPercentage(100f);
        pdfPTable1.setWidths(new float[]{1,3,3,2});
        pdfPTable1.setSpacingBefore(5);

        writeTableHeader(pdfPTable1);
        writeTableDataOrders(pdfPTable1);
        writeTableDataCustomer(pdfPTable);

        document.add(paragraph);
        document.add(pdfPTable);
        document.add(paragraph1);
        document.add(pdfPTable1);

        document.close();



    }
}
