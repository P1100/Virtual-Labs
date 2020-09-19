package it.polito.ai.es2.domains;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TeamViewModel {
  @NotNull
  private Long id;
  @NotBlank
  private String name;
  @NotBlank
  private String courseId;
  @NotBlank
  List<Long> memberIds;
}
