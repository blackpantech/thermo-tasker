package com.blackpantech.central_module.domain.ports;

import com.blackpantech.central_module.domain.Task;
import com.blackpantech.central_module.domain.exceptions.TaskSchedulingException;

public interface TaskScheduler {
  void scheduleTask(Task task) throws TaskSchedulingException;
}
