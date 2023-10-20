package com.nancho313.loqui.auth.persistence.client.rabbitmq.emitter;

import com.nancho313.loqui.commons.AvroSerializer;
import com.nancho313.loqui.events.CreatedUserEvent;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Slf4j
@Component
public class CreateUserEventEmitter {
  
  private final AvroSerializer<CreatedUserEvent> serializer = new AvroSerializer<>(CreatedUserEvent.getClassSchema());
  
  private final RabbitTemplate rabbitTemplate;
  
  public CreateUserEventEmitter(RabbitTemplate rabbitTemplate) {
    this.rabbitTemplate = rabbitTemplate;
  }
  
  public void sendCreatedUser(String userId, String username, String email) {
    
    var event = CreatedUserEvent.newBuilder().setUserId(userId).setUsername(username).setEmail(email).build();
    
    try {
      
      var serializedMessage = serializer.serialize(event);
      CorrelationData correlationData = new CorrelationData();
      correlationData.setId(UUID.randomUUID().toString());
      String exchange = "created-users-exch";
      rabbitTemplate.send(exchange, "", new Message(serializedMessage), correlationData);
      
    } catch (Exception e) {
      
      throw new RuntimeException("Something went wrong while sending message to RabbitMq", e);
    }
  }
}
