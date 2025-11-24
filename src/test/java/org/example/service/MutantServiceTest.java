package org.example.service;

import org.example.dto.StatsResponse;
import org.example.entity.DnaRecord;
import org.example.repository.DnaRecordRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class MutantServiceTest {

    @Mock
    private MutantDetector mutantDetector;

    @Mock
    private DnaRecordRepository dnaRecordRepository;

    @InjectMocks
    private MutantService mutantService;

    @Test
    void analyzeDna_ShouldReturnTrue_WhenMutant() {
        String[] dna = {"AAAA", "CCCC", "TCAG", "GGTC"};
        // Simulamos que NO existe en BD
        when(dnaRecordRepository.findByDnaHash(any())).thenReturn(Optional.empty());
        // Simulamos que el detector dice que ES mutante
        when(mutantDetector.isMutant(any())).thenReturn(true);

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        // Verificamos que se guardó en la BD
        verify(dnaRecordRepository, times(1)).save(any(DnaRecord.class));
    }

    @Test
    void analyzeDna_ShouldUseCache_WhenExists() {
        String[] dna = {"AAAA", "CCCC", "TCAG", "GGTC"};
        DnaRecord existingRecord = new DnaRecord();
        existingRecord.setMutant(true);

        // Simulamos que SÍ existe en BD
        when(dnaRecordRepository.findByDnaHash(any())).thenReturn(Optional.of(existingRecord));

        boolean result = mutantService.analyzeDna(dna);

        assertTrue(result);
        // Verificamos que NO se llamó al detector (usó el caché)
        verify(mutantDetector, never()).isMutant(any());
    }

    @Test
    void getStats_ShouldCalculateRatio() {
        // Simulamos 40 mutantes y 100 humanos
        when(dnaRecordRepository.countByIsMutant(true)).thenReturn(40L);
        when(dnaRecordRepository.countByIsMutant(false)).thenReturn(100L);

        StatsResponse stats = mutantService.getStats();

        assertEquals(40, stats.getCountMutantDna());
        assertEquals(100, stats.getCountHumanDna());
        assertEquals(0.4, stats.getRatio()); // 40 / 100 = 0.4
    }
}