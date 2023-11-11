package org.exchanger_bot.utils.dto;


import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
public class MailActivationResp {
    private String CryptoId;
    private boolean isSentEmail;

    private String email;
}
