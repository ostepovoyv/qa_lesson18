package in.reqres.data.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdatedUserResponse {

    private String name;
    private String job;
    private String updatedAt;
}
