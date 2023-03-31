package ro.myclass.onlineStoreapi.rest;

import com.lowagie.text.DocumentException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.PDFGenerator.ProductPDFGenerator;
import ro.myclass.onlineStoreapi.dto.CancelOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderResponse;
import ro.myclass.onlineStoreapi.dto.ProductDTO;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/api/v1/product")
public class ProductResource {

    CustomerService customerService;
    private ProductService productService;

    public ProductResource(ProductService productService,CustomerService customerService) {
        this.productService = productService;
        this.customerService = customerService;
    }

    @GetMapping("/getAllProducts")
    public ResponseEntity<List<Product>> getAllProducts(){
        List<Product> products = this.productService.showProducts();

        return new ResponseEntity<>(products, HttpStatus.OK);
    }
    @PostMapping("/addProduct")
    public ResponseEntity<String> addProduct(@RequestBody ProductDTO productDTO){
        this.productService.addProduct(productDTO);

        return new ResponseEntity<>("Product was added!",HttpStatus.OK);

    }

    @DeleteMapping("/deleteProduct/")
    public ResponseEntity<String> deleteProduct(@RequestParam String name){
        this.productService.deleteProduct(name);

        return new ResponseEntity<>("Product was deleted!",HttpStatus.OK);
    }

    @GetMapping("/getProductByName/")
    public ResponseEntity<Product> getProductByName(@RequestParam String name){

        Product product = this.productService.getProductbyName(name);

        return new ResponseEntity<>(product,HttpStatus.OK);

    }

    @GetMapping("/exportPDF")
        public ResponseEntity<CreateOrderResponse> generator(HttpServletResponse response) throws DocumentException,IOException {
        response.setContentType("application/pdf");

        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDate = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=productpdf_" + currentDate +".pdf";

        response.setHeader(headerKey,headerValue);

        List<Product> products = productService.showProducts();

        ProductPDFGenerator generator = new ProductPDFGenerator(products);

        generator.generate(response);

        return new ResponseEntity<>( new CreateOrderResponse("Descarcat cu succes"), HttpStatus.OK);

        }


}
