package ai.polito.es1;

import lombok.Data;

/**
 * NOTA: vincoli di validazione in questo caso non usati, in quanto controlli a priori sul form di login
 * potrebbero essere superflui: se coppia username-password errata comunque non entro (n.d.r. slack)
 */
@Data
public class LoginCommand {
//    @NotBlank //must not be null and must contain at least one non-whitespace character.
//    @Email
    private String email;
//    @NotBlank
    private String password;
}