package org.exchanger_bot.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;


class CryptoToolsTest {


    private CryptoTools cryptoTools;


    @BeforeEach
    public void setUp() {
        String testSalt = "testSalt";
        this.cryptoTools = new CryptoTools(testSalt);
    }

    @Test
    void idOf_ValidHash_ReturnsId() {
        long id = 123;
        String hash = cryptoTools.hashOf(id);

        assertThat(id).isEqualTo(cryptoTools.idOf(hash));


    }

    @Test
    void idOf_InvalidHash_Null() {
        Long id = cryptoTools.idOf("invalidHash");
        assertThat(id).isNull();
    }


    @Test
    void hashOf_ReturnsHash() {
        long id = 123;
        String hash = cryptoTools.hashOf(id);
        assertThat(hash).isNotNull();
        assertThat(hash).isNotEmpty();
    }
}