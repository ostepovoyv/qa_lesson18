package in.reqres.data.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateUserInfo {

    private String name;
    private String job;
}
