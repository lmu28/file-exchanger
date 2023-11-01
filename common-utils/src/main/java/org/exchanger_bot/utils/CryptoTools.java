package org.exchanger_bot.utils;

import lombok.extern.log4j.Log4j;
import org.hashids.Hashids;


@Log4j
public class CryptoTools {

    private Hashids hashids;

    public CryptoTools(String salt) {
        int hashSize = 10;
        hashids = new Hashids(salt,hashSize);
    }

    public Long idOf(String hash){
        long[] ids = hashids.decode(hash);
        if(ids != null && ids.length > 0){
            return ids[0];
        }else {
            log.error("Error decode hash to id");
            return null;
        }

    }

    public String hashOf(long id){
        String hash = hashids.encode(id);
        return hash;
    }


}
