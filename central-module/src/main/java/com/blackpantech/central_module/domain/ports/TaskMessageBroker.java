package com.blackpantech.central_module.domain.ports;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskQueueingException;

public interface TaskMessageBroker {
  void sendTask(Task newTask) throws TaskQueueingException;
}
