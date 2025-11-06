package hu.uni_miskolc.order_processor.validations;

import hu.uni_miskolc.order_processor.exceptions.ValidationException;

public interface Validator<T> {
    boolean validate(T chainElement) throws ValidationException;
    void setNext(Validator<T> next);
}
