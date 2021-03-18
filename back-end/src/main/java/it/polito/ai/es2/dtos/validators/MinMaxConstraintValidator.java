package it.polito.ai.es2.dtos.validators;

import it.polito.ai.es2.dtos.CourseDTO;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MinMaxConstraintValidator implements ConstraintValidator<MinMaxConstraint, Object> { // StudentDTO
    @Override
    public void initialize(MinMaxConstraint constraintAnnotation) {
    }
    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        if (!(obj instanceof CourseDTO)) {
            throw new IllegalArgumentException("@Address only applies to CourseDTO");
        }
        CourseDTO c = (CourseDTO) obj;
        return c.getMinSizeTeam() < c.getMaxSizeTeam();
    }
}
