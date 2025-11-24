package org.example.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class MutantDetectorTest {

    private MutantDetector detector;

    @BeforeEach
    void setUp() {
        detector = new MutantDetector();
    }

    @Test
    @DisplayName("Debe detectar mutante horizontal")
    void testMutantHorizontal() {
        String[] dna = {"AAAA", "CCCC", "TCAG", "GGTC"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante vertical")
    void testMutantVertical() {
        String[] dna = {"ATCG", "ATCG", "ATCG", "ATCG"};
        assertTrue(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe detectar mutante diagonal")
    void testMutantDiagonal() {
        // Diagonal AAAA y Horizontal GGGG para asegurar >1 secuencia
        String[] dna = {
                "ATCGT",
                "GAGGT",
                "TTAGT",
                "AGAAG",
                "GGGGG"
        };
        assertTrue(detector.isMutant(dna));
    }

    @Test
    @DisplayName("No debe detectar humano")
    void testHuman() {
        String[] dna = {"ATCG", "CAGT", "TCGA", "GATC"};
        assertFalse(detector.isMutant(dna));
    }

    @Test
    @DisplayName("Debe lanzar error con ADN nulo")
    void testNullDna() {
        assertThrows(IllegalArgumentException.class, () -> detector.isMutant(null));
    }
}