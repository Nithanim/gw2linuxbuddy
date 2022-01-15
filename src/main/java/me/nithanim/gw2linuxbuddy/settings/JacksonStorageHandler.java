package me.nithanim.gw2linuxbuddy.settings;

import java.util.List;

import com.dlsc.preferencesfx.util.PreferencesBasedStorageHandler;

class JacksonStorageHandler extends PreferencesBasedStorageHandler {

    public JacksonStorageHandler(Class<?> saveClass) {
        super(saveClass);
    }

    @Override protected String serialize(Object object) {
    return null;
    }

    @Override protected <T> T deserialize(String serialized, Class<T> type) {
        return null;
    }

    @Override protected <T> List<T> deserializeList(String serialized, Class<T> type) {
        return null;
    }
}
