package hu.uni_miskolc.order_processor.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class GenericSpringEvent<T> extends ApplicationEvent {
    private final T event;
    protected String status;

    public GenericSpringEvent(Object object,T event, String status) {
        super(object);
        this.event = event;
        this.status = status;
    }
}
