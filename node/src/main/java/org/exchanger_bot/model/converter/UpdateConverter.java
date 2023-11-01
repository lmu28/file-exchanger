//package org.exchanger_bot.model.converter;
//
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import jakarta.persistence.AttributeConverter;
//import jakarta.persistence.Converter;
//import lombok.extern.log4j.Log4j;
//import org.postgresql.util.PGobject;
//import org.telegram.telegrambots.meta.api.objects.Update;
//
//import java.sql.SQLException;
//
//@Converter(autoApply = true)
//@Log4j
//public class UpdateConverter implements AttributeConverter<Update,PGobject> {
//
//
//
//    private ObjectMapper objectMapper = new ObjectMapper();
//
//    @Override
//    public PGobject convertToDatabaseColumn(Update update) {
//        try {
//            PGobject pGobject = new PGobject();
//            pGobject.setType("jsonb");
//            pGobject.setValue(objectMapper.writeValueAsString(update));
//            return pGobject;
//        } catch (SQLException | JsonProcessingException e) {
//            log.error("NODE: error convert Update to json");
//            throw new RuntimeException(e);
//        }
//    }
//
//    @Override
//    public Update convertToEntityAttribute(PGobject pGobject) {
//        Update update = null;
//        try {
//             update = objectMapper.readValue(pGobject.getValue(),Update.class);
//        } catch (JsonProcessingException e) {
//            log.error("NODE: error convert json to Update");
//            throw new RuntimeException(e);
//        }
//        return update;
//    }
//}
