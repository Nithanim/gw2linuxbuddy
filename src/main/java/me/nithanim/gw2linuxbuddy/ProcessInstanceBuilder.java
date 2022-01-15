package me.nithanim.gw2linuxbuddy;

import java.nio.file.Path;
import java.util.Map;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ProcessInstanceBuilder {
  String instanceId;
  Map<String, String> environmentParameters;
  String arguments;
  Path winePath;
  Path executablePath;
  InstanceDataFolder instanceDataFolder;
}
