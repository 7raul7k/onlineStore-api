package ro.myclass.onlineStoreapi.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.validation.constraints.NotEmpty;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CustomerDTO {


    @NotEmpty
    private String email;
    @NotEmpty
    private String password;
    @NotEmpty
    private String fullName;
}
