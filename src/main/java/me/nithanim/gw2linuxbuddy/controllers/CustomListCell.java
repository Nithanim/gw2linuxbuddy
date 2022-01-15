package me.nithanim.gw2linuxbuddy.controllers;

import javafx.scene.control.ListCell;

class CustomListCell extends ListCell<ProfileController> {
    public CustomListCell() {
        super();
    }

    @Override
    protected void updateItem(ProfileController item, boolean empty) {
        super.updateItem(item, empty);
        if (item != null && !empty) {
            setGraphic(item.getRoot());
        } else {
            setGraphic(null);
        }
    }
}
