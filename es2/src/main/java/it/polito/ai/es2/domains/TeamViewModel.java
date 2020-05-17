package it.polito.ai.es2.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamViewModel {
  @NotBlank
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String courseId;
  @NotBlank
  List<String> memberIds;
}
