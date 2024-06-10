package com.nancho313.loqui.auth.integrationtest.util;

import com.nancho313.loqui.events.CreatedUserEvent;
import org.springframework.stereotype.Component;

@Component
@ITKafkaListener(topics = "created-users")
public class CreatedUserEventMessageCaptor extends KafkaMessageCaptor<CreatedUserEvent> {

}