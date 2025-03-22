package de.sebampuerom.userJoinedNotifier.notification;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;


@Getter
@Setter
@AllArgsConstructor
public class NotificationFields {

    private String username;
    private String serverName;
    private String coordinates;
    private ZonedDateTime dateTime;

}
