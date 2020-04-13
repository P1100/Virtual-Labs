package ai.polito.es1;

import lombok.Data;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
public class RegistrationCommand {
    private static final int MIN = 5;
    private static final int MAX = 25;
    @NotNull
    @Size(min = 1, max = MAX)
    private String name;
    @NotNull
    @Size(min = 1, max = MAX)
    private String surname;
    @NotNull
    @AssertTrue
    private boolean privacy;
    @NotNull
    @Email(message = "{command.textField.email.message}")
    @Size.List({
            @Size(min = MIN, message = "{command.email.min.message}"),
            @Size(max = MAX, message = "{command.email.max.message}")
    })
    private String email;
    @NotNull
    @Size(min = MIN, max = MAX)
    private String password1;
    @NotNull
    @Size(min = MIN, max = MAX)
    private String password2;

    public static int getMIN() {
        return MIN;
    }
    public static int getMAX() {
        return MAX;
    }
}
