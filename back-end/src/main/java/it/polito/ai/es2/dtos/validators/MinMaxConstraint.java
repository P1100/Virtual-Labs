package it.polito.ai.es2.dtos.validators;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Target({TYPE, ANNOTATION_TYPE}) //, FIELD, METHOD
@Retention(RUNTIME)
@Constraint(validatedBy = MinMaxConstraintValidator.class)
public @interface MinMaxConstraint {

    String message() default "Constrains min max size of teams";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
