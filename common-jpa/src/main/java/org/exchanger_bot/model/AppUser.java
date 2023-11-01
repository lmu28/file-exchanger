package org.exchanger_bot.model;


import lombok.*;
import org.exchanger_bot.model.enums.AppUserState;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    private long telegramUserId;

    @CreationTimestamp
    private LocalDateTime firstLoginDate;

    private String firstName;
    private String lastName;
    private String username;
    private String email;
    private boolean isActive;
    @Enumerated(EnumType.STRING)
    private AppUserState state;

}
