package de.sebampuerom.userJoinedNotifier.notification;

public abstract class Notification implements Runnable {

    public abstract void send();

}
