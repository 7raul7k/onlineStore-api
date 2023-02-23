package ro.myclass.onlineStoreapi.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.CreateOrderRequest;
import ro.myclass.onlineStoreapi.dto.CreateOrderResponse;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.dto.ProductCardRequest;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.services.CustomerService;
import ro.myclass.onlineStoreapi.services.ProductService;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping( value = "/api/v1/customer",consumes = "application/json")
public class CustomerResource {
    private CustomerService customerService;

    private ProductService productService;

    public CustomerResource(CustomerService customerService,ProductService productService) {
        this.customerService = customerService;
        this.productService = productService;
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

 @PostMapping( "/addOrder")
    public ResponseEntity<CreateOrderResponse> addOrder(@RequestParam String email,@RequestParam String productName,@RequestParam  int quantity){
     //todo: fara dto

     Customer customer = this.customerService.returnCustomerByEmail(email);
     Product product = this.productService.getProductbyName(productName);
     ProductCardRequest productCardRequest = new ProductCardRequest(Math.toIntExact(product.getId()),quantity);

     List<ProductCardRequest> products = new ArrayList<>();
     products.add(productCardRequest);

     CreateOrderRequest createOrderRequest = CreateOrderRequest.builder().customerId(Math.toIntExact(customer.getId()))
             .productCardRequests(products)
             .build();


     this.customerService.addOrder(createOrderRequest);
        return new ResponseEntity<>(new CreateOrderResponse("adaugat cu succes"),HttpStatus.OK);

 }


}
