package ro.myclass.onlineStoreapi.repo;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ro.myclass.onlineStoreapi.models.Customer;
import ro.myclass.onlineStoreapi.models.Order;
import ro.myclass.onlineStoreapi.models.OrderDetail;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepo extends JpaRepository<Order,Long> {

    @Query("select o from Order o where o.customer.id = ?1")
    List<Order> getOrderByCustomerId(long customerId);


 @Query("select o from Order o where o.id = ?1 and o.customer.id = ?2")
 Optional<Order> getOrderByIdAndCustomerId(long orderId, long customerId);

   @Query("select o from Order o where o.id = ?1")
   List<Order> getOrderById(long id);

   @Query("select o from Order o where o.customer.id = ?1 order by o.orderDate ASC")
    List<Order> sortOrderListByDate(long customerID);




}
