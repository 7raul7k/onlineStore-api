package ro.myclass.onlineStoreapi.services;

import org.springframework.stereotype.Service;
import ro.myclass.onlineStoreapi.dto.CreateOrderDetailRequest;
import ro.myclass.onlineStoreapi.exceptions.CustomerNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ListEmptyException;
import ro.myclass.onlineStoreapi.exceptions.OrderNotFoundException;
import ro.myclass.onlineStoreapi.exceptions.ProductNotFoundException;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;
import ro.myclass.onlineStoreapi.models.Product;
import ro.myclass.onlineStoreapi.repo.CustomerRepo;
import ro.myclass.onlineStoreapi.repo.OrderDetailRepo;
import ro.myclass.onlineStoreapi.repo.OrderRepo;
import ro.myclass.onlineStoreapi.repo.ProductRepo;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailService {

    private OrderDetailRepo orderDetailRepo;

    private OrderRepo orderRepo;

    private CustomerRepo customerRepo;

    private ProductRepo productRepo;


    public OrderDetailService(OrderDetailRepo orderDetailRepo, OrderRepo orderRepo, CustomerRepo customerRepo, ProductRepo productRepo) {
        this.orderDetailRepo = orderDetailRepo;
        this.orderRepo = orderRepo;
        this.customerRepo = customerRepo;
        this.productRepo = productRepo;
    }

    public List<OrderDetail> getAllOrderDetail(){

        List<OrderDetail> orderDetailList = this.orderDetailRepo.getAllOrderDetails();

        if(orderDetailList.isEmpty()){
            throw new ListEmptyException();
        }else{
            return orderDetailList;
        }
    }

    @Transactional
    public void addOrderDetail(CreateOrderDetailRequest orderDetailRequest) throws Exception {

        Optional<Customer> customerOptional = this.customerRepo.getCustomerById(orderDetailRequest.getCustomerId());

        if(customerOptional.isEmpty()){
            throw new CustomerNotFoundException();
        }



        Optional<Order> orderOptional = this.orderRepo.getOrderByIdAndCustomerId(orderDetailRequest.getOrderId(),customerOptional.get().getId());

        if(orderOptional.isEmpty()){
            throw new OrderNotFoundException();
        }


        Optional<OrderDetail> orderDetail= this.orderDetailRepo.findOrderDetailByProductIdAndOrderId(orderDetailRequest.getProductId(), orderDetailRequest.getOrderId());

        if(orderDetail.isEmpty()){
            throw new OrderNotFoundException();
        }

        Optional<Product> product = this.productRepo.getProductByName(orderDetail.get().getProduct().getName());

        if(product.isEmpty()){
            throw new ProductNotFoundException();
        }

        if(orderDetailRequest.getPrice()  > 0){
            orderDetail.get().setPrice(orderDetailRequest.getPrice());
        }if(orderDetailRequest.getQuantity() > 0) {

            if(orderDetailRequest.getQuantity() > product.get().getStock()){
                throw new Exception("Stock not available");
            }else{
                product.get().setStock(product.get().getStock()-orderDetailRequest.getQuantity());
            }

            orderDetail.get().setQuantity(orderDetailRequest.getQuantity());

            productRepo.save(product.get());

        }

        orderDetail.get().setOrder(orderOptional.get());


        orderDetailRepo.saveAndFlush(orderDetail.get());

    }
    @Transactional
    public void deleteOrderDetail(int productId,int orderId){

        Optional<OrderDetail> orderDetail = this.orderDetailRepo.findOrderDetailByProductIdAndOrderId((long) productId,(long) orderId);

        if(orderDetail.isEmpty()){

            throw new OrderNotFoundException();
        }else{
            this.orderDetailRepo.delete(orderDetail.get());
        }
    }

    public OrderDetail  findOrderDetailByProductIdAndOrderId(long productId,long orderId){

        Optional<OrderDetail> orderDetail = this.orderDetailRepo.findOrderDetailByProductIdAndOrderId(productId,orderId);

        if(orderDetail.isEmpty()){

            throw new OrderNotFoundException();
        }else{
           return orderDetail.get();
        }
    }



}
