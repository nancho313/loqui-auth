package com.nancho313.loqui.auth.persistence.client.kafka.emitter;

import com.nancho313.loqui.events.CreatedUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class CreatedUserKafkaEmitter extends LoquiKafkaEmitter<CreatedUserEvent> {
  
  private static final String TOPIC_NAME = "created-users";
  
  public CreatedUserKafkaEmitter(ProducerFactory<String, CreatedUserEvent> producerFactory) {
    super(producerFactory);
  }
  
  protected String getTopic() {
    return TOPIC_NAME;
  }
}
