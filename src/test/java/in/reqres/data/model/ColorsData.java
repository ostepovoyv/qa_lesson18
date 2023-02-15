package in.reqres.data.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ColorsData {

    private Integer id;

    private String name;

    private Integer year;

    private String color;

    @JsonProperty("pantone_value")
    private String pantoneValue;

}
