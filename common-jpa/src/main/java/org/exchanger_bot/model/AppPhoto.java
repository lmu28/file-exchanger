package org.exchanger_bot.model;


import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "app_photo")
public class AppPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String telegramFileId;
    @OneToOne
    private BinaryContent binaryContent;
    private int fileSize;
}
