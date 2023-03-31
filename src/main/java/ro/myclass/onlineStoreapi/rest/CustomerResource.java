package ro.myclass.onlineStoreapi.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.PDFGenerator.CustomerOrderPDFGenerator;
import ro.myclass.onlineStoreapi.PDFGenerator.CustomerPDFGenerator;
import ro.myclass.onlineStoreapi.dto.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;

import javax.servlet.http.HttpServletResponse;

import javax.validation.Valid;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/customer")
@Slf4j
public class CustomerResource {
    private CustomerService customerService;



    public CustomerResource(CustomerService customerService,ProductService productService) {
        this.customerService = customerService;
    }

 @PostMapping("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer){

    
        this.customerService.addCustomer(customer);

        return new ResponseEntity<>("Customer was added!", HttpStatus.ACCEPTED);
 }

 @DeleteMapping("/deleteCustomer/{email}")
    public ResponseEntity<String> removeCustomer(@PathVariable String email){
        this.customerService.removeCustomer(email);

        return new ResponseEntity<>("Customer was deleted!",HttpStatus.OK);
 }

 @GetMapping("/getCustomerByEmail/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email){
        Customer m = this.customerService.returnCustomerByEmail(email);

        return new ResponseEntity<>(m,HttpStatus.FOUND);
 }

 @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(){
        List<Customer> customerList = this.customerService.getAllCustomer();

        return new ResponseEntity<>(customerList,HttpStatus.OK);
 }



   @PutMapping("/updateQuantityProduct")
   public ResponseEntity<CreateOrderResponse> updateQuantity(@RequestBody  UpdateOrderRequest updateOrderRequest){
     this.customerService.updateQuantityProduct(updateOrderRequest);

     return new ResponseEntity<>(new CreateOrderResponse("cantitatea  a fost actualizata!"),HttpStatus.OK);
    }

@PostMapping("/addOrder")
public ResponseEntity<CreateOrderResponse> addOrder(@RequestBody CreateOrderRequest createOrderRequest){

        this.customerService.addOrder(createOrderRequest);
    return new ResponseEntity<>(new CreateOrderResponse("adaugat cu succes!"),HttpStatus.OK);

    }

    @DeleteMapping(path = "/cancelOrder")
    public ResponseEntity<CreateOrderResponse> deleteOrder(@RequestBody CancelOrderRequest orderRequest){
        this.customerService.cancelOrder(orderRequest);


        return new ResponseEntity<>(new CreateOrderResponse("sters cu succes!"),HttpStatus.OK);

    }

    @GetMapping("/exportPDF")
    public ResponseEntity<CreateOrderResponse> generator(HttpServletResponse response) throws DocumentException, IOException {
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDate = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment; filename=customerpdf_" + currentDate + ".pdf";

        response.setHeader(headerKey,headerValue);

        List<Customer> customers = customerService.getAllCustomer();
        CustomerPDFGenerator generator = new CustomerPDFGenerator(customers);

       generator.generate(response);

        return new ResponseEntity<>( new CreateOrderResponse("Descarcat cu succes"), HttpStatus.OK);
    }

    @GetMapping("/exportOrderPDF/{customerId}")
    public ResponseEntity<CreateOrderResponse> generatorPdfOrder(HttpServletResponse response,@PathVariable int customerId)throws DocumentException,IOException{
        response.setContentType("application/pdf");
        DateFormat dateFormat = new SimpleDateFormat("YYYY-MM-DD:HH:MM:SS");
        String currentDate = dateFormat.format(new Date());
        String headerKey = "Content-Disposition";
        String headerValue = "attachment;filename=customerorderpdf_" + currentDate + ".pdf";

        response.setHeader(headerKey,headerValue);

        List<OrderDetail> orderDetails = this.customerService.returnAllOrderDetailsByCustomerID(customerId);

        CustomerOrderPDFGenerator customerOrderPDFGenerator = new CustomerOrderPDFGenerator(orderDetails);

        customerOrderPDFGenerator.generate(response);

        return new ResponseEntity<>(new CreateOrderResponse("Descarcat cu succes"),HttpStatus.OK);

    }

}



