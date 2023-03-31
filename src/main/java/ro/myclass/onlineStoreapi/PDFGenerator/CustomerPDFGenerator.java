package ro.myclass.onlineStoreapi.PDFGenerator;


import com.lowagie.text.*;
import com.lowagie.text.Font;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfTable;
import com.lowagie.text.pdf.PdfWriter;
import ro.myclass.onlineStoreapi.models.Customer;

import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.io.IOException;
import java.util.List;

public class CustomerPDFGenerator {

    private List<Customer> customers;

    public CustomerPDFGenerator(List<Customer> customers) {
        this.customers = customers;
    }

    public void writeTableHeader(PdfPTable table){
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.CYAN);

        cell.setPadding(5);

        Font font = FontFactory.getFont(FontFactory.HELVETICA_BOLD);

        font.setColor(Color.white);
        cell.setPhrase(new Phrase("ID",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Email",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Password",font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Full Name",font));
        table.addCell(cell);
    }

    private void writeTableData(PdfPTable table){
        for(Customer customer : customers){
            table.addCell(String.valueOf(customer.getId()));
            table.addCell(customer.getEmail());
            table.addCell(customer.getPassword());
            table.addCell(customer.getFullName());
        }
    }
    public void generate(HttpServletResponse response) throws DocumentException,IOException{
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document,response.getOutputStream());
        document.open();

        Font fontTitle = FontFactory.getFont(FontFactory.COURIER);
        fontTitle.setSize(20);
        fontTitle.setColor(Color.black);

        Paragraph paragraph = new Paragraph("Customer List",fontTitle);
        paragraph.setAlignment(Element.ALIGN_CENTER);

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100f);
        table.setWidths(new int[]{1,1,3,2});
        table.setSpacingBefore(5);

        writeTableHeader(table);
        writeTableData(table);
        document.add(paragraph);

        document.add(table);
        document.close();



    }
}
