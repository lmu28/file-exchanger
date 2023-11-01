package org.exchanger_bot.model;


import javax.persistence.*;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.telegram.telegrambots.meta.api.objects.Update;



@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@EqualsAndHashCode(exclude = "id") // id - mutable
@Builder
@Entity
@Table(name = "row_data")
@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
public class RowData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;


    @Type(type = "jsonb")
    @Column(columnDefinition = "jsonb")
    private Update event;



}
