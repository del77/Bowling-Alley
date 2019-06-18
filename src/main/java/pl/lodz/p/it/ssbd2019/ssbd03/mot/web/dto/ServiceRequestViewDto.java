package pl.lodz.p.it.ssbd2019.ssbd03.mot.web.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Data
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class ServiceRequestViewDto {
    private Long id;
    private int alleyNumber;
    private String content;
    protected String userLogin;
}
