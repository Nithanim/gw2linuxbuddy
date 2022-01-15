package me.nithanim.gw2linuxbuddy.services;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class OsLoginNameProviderImpl implements OsLoginNameProvider {
  @Override
  public String getLoginName() {
    return System.getProperty("user.name");
  }
}
