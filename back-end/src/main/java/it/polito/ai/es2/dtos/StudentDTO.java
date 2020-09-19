package it.polito.ai.es2.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import javax.persistence.Transient;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class StudentDTO extends RepresentationModel<StudentDTO> {
  @NotNull
  private Long id;  // matricola/serial
  @NotBlank
  private String firstName;
  @NotBlank
  private String lastName;
  @Email
  @Pattern(regexp = "s[0-9]{1,9}@studenti\\.polito\\.it")
  private String email;
  /**
   * Used for convenient mapping in userDTOs, studentDTOs, and professorDTOs. Don't remove!
   */
  @Transient
  private Long imageId;
  /* Convenient values related to a Team and Course */
  @Transient
  private String teamName;
  @Transient
  private boolean proposalAccepted;
  @Transient
  private boolean proposalRejected;
  @Transient
  private String urlTokenConfirm;
  @Transient
  private String urlTokenReject;
}

