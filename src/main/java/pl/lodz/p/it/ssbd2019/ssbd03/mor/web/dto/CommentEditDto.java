package pl.lodz.p.it.ssbd2019.ssbd03.mor.web.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.ws.rs.FormParam;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CommentEditDto {
    @NotNull(message = "{validate.commentContentNotNull}")
    @NotBlank(message = "{validate.commentContentNotNull}")
    @Size(max = 256, message = "{validate.commentContentLength}")
    @FormParam("content")
    private String content;
}
