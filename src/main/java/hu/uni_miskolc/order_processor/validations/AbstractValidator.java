package hu.uni_miskolc.order_processor.validations;

import hu.uni_miskolc.order_processor.exceptions.ValidationException;

public abstract class AbstractValidator<T> implements Validator<T> {
    protected Validator<T> next;

    @Override
    public void setNext(Validator<T> next) {
        this.next = next;
    }

    protected boolean callNext(T chainElement) throws ValidationException {
        if (next == null) return true;
        return next.validate(chainElement);
    }
}
