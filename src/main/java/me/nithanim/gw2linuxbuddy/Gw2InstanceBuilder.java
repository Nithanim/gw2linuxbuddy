package me.nithanim.gw2linuxbuddy;

import java.nio.file.Path;
import java.util.Map;

import lombok.Builder;
import lombok.Data;
import lombok.Getter;

@Builder
@Getter
public class Gw2InstanceBuilder {
  String instanceId;
  Map<String, String> environmentParameters;
  String arguments;
  Path winePath;
  Path executablePath;
  InstanceDataFolder instanceDataFolder;
}
