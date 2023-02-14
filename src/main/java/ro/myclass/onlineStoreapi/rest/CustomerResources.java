package ro.myclass.onlineStoreapi.rest;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ro.myclass.onlineStoreapi.dto.CustomerDTO;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.services.CustomerService;

@RestController
@RequestMapping("/api/v1")
public class CustomerResources {
    private CustomerService customerService;

    public CustomerResources(CustomerService customerService) {
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
}
