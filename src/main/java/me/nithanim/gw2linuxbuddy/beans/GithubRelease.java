package me.nithanim.gw2linuxbuddy.beans;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

/** https://api.github.com/repos/doitsujin/dxvk/releases */
@Data
public class GithubRelease {
  String name;
  String htmlUrl;
  List<Asset> assets;

  public static class Asset {
    String browserDownloadUrl;
    OffsetDateTime updatedAt;
    long size;
  }
}
