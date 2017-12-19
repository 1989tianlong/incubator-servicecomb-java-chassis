/*
 * Copyright 2017 Huawei Technologies Co., Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.servicecomb.metrics.core.monitor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import io.servicecomb.metrics.core.metric.InvocationMetric;

public class RegistryMonitor {

  private final Map<String, InvocationMonitor> invocationMonitors;

  public RegistryMonitor() {
    this.invocationMonitors = new ConcurrentHashMap<>();
  }

  public InvocationMonitor getInvocationMonitor(String operationName) {
    InvocationMonitor level = invocationMonitors.getOrDefault(operationName, null);
    if (level == null) {
      level = invocationMonitors.computeIfAbsent(operationName, i -> new InvocationMonitor(operationName));
    }
    return level;
  }

  public Map<String, InvocationMetric> toInvocationMetrics(int pollerIndex) {
    return invocationMonitors.values().stream().collect(Collectors.toMap(InvocationMonitor::getOperationName,
        monitor -> monitor.toInvocationMetric(pollerIndex)));
  }
}