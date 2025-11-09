package com.blackpantech.central_module.domain.ports;

import com.blackpantech.central_module.domain.Task;

public interface TaskMessageBroker {
  void sendTask(Task newTask);
}
