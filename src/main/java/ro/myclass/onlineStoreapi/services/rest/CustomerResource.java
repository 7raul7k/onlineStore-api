package ro.myclass.onlineStoreapi.services.rest;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.*;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/customer")
@Slf4j
public class CustomerResource {
    private CustomerService customerService;

    private ProductService productService;

    public CustomerResource(CustomerService customerService,ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
    }

 @PostMapping("/addCustomer")
    public ResponseEntity<String> addCustomer(@RequestBody CustomerDTO customer){

<<<<<<< HEAD:src/main/java/ro/myclass/onlineStoreapi/rest/CustomerResource.java
    
=======
     log.info("REST request to add customer {}",customer);
>>>>>>> onlineStore-api/task-6-add-logs:src/main/java/ro/myclass/onlineStoreapi/services/rest/CustomerResource.java
        this.customerService.addCustomer(customer);

        return new ResponseEntity<>("Customer was added!", HttpStatus.ACCEPTED);
 }

 @DeleteMapping("/deleteCustomer/{email}")
    public ResponseEntity<String> removeCustomer(@PathVariable String email){

     log.info("REST request to delete customer by email {}",email);
        this.customerService.removeCustomer(email);

        return new ResponseEntity<>("Customer was deleted!",HttpStatus.OK);
 }

 @GetMapping("/getCustomerByEmail/{email}")
    public ResponseEntity<Customer> getCustomerByEmail(@PathVariable String email){

     log.info("REST request to get customer by email {}",email);
        Customer m = this.customerService.returnCustomerByEmail(email);

        return new ResponseEntity<>(m,HttpStatus.FOUND);
 }

 @GetMapping("/getAllCustomers")
    public ResponseEntity<List<Customer>> getAllCustomers(){

        List<Customer> customerList = this.customerService.getAllCustomer();
     log.info("REST request to get all customers {}",customerList);

        return new ResponseEntity<>(customerList,HttpStatus.OK);
 }



   @GetMapping("/updateQuantityProduct/{customerId}")
   public ResponseEntity<CreateOrderResponse> updateQuantity(@PathVariable int customerId,@RequestParam int quantity,@RequestParam int productID){
        log.info("REST request to update quantity product",productID);
     this.customerService.updateQuantityProduct(customerId,quantity,productID);

     return new ResponseEntity<>(new CreateOrderResponse("cantitatea  a fost actualizata!"),HttpStatus.OK);
    }

    @GetMapping("/getOrderDetails/{customerId}")
        public ResponseEntity<List<OrderDetail>> getOrderDetails ( @PathVariable int customerId){
        log.info("REST request to get all orders detail by customer ID {}",customerId);
            List<OrderDetail> orderDetails = this.customerService.returnAllOrdersDetailbyOrderId(customerId);

            return new ResponseEntity<>(orderDetails,HttpStatus.OK);
        }

@PostMapping("/{customerId}")
public ResponseEntity<CreateOrderResponse> addOrder(@RequestBody CreateOrderRequest createOrderRequest){

        log.info("REST request to addOrder by createOrderRequest {}",createOrderRequest);
        this.customerService.addOrder(createOrderRequest);
    return new ResponseEntity<>(new CreateOrderResponse("adaugat cu succes!"),HttpStatus.OK);

    }

}



