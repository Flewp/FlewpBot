package com.flewp.flewpbot;

import com.flewp.flewpbot.model.events.BaseEvent;
import reactor.core.publisher.EmitterProcessor;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxProcessor;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

public class EventManager {
    private final Scheduler scheduler;

    private final FluxProcessor<BaseEvent, BaseEvent> processor;

    public EventManager() {
        this.scheduler = Schedulers.elastic();
        this.processor = EmitterProcessor.create(256);
    }

    public void dispatchEvent(BaseEvent event) {
        processor.onNext(event);
    }

    public <E extends BaseEvent> Flux<E> onEvent(Class<E> eventClass) {
        return processor.publishOn(scheduler).ofType(eventClass);
    }
}
