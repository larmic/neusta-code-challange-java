package de.neusta.ncc.infrastructure.mapper;

import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvParser;
import de.neusta.ncc.domain.Person;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException;
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Maps a given {@link MultipartFile} to list of {@link Room}. Does not validate any content of given file.
 */
@Component
public class CsvImportMapper {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private final CsvPersonToPersonMapper csvPersonToPersonMapper;

    @Autowired
    public CsvImportMapper(CsvPersonToPersonMapper csvPersonToPersonMapper) {
        this.csvPersonToPersonMapper = csvPersonToPersonMapper;
    }

    public List<Room> map(MultipartFile file) throws FileImportException, EmptyFileImportException {
        try {
            final byte[] csvData = loadDate(file);
            final List<SimpleImportRoom> importRooms = mapToSimpleImportModel(csvData);

            return convertCsvImportRooms(importRooms);
        } catch (IOException e) {
            log.error("Could not import {}", file.getOriginalFilename(), e);
            throw new FileImportException(e);
        }
    }

    private byte[] loadDate(MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            log.error("File %s is empty", file.getOriginalFilename());
            throw new EmptyFileImportException();
        }

        return file.getBytes();
    }

    private List<SimpleImportRoom> mapToSimpleImportModel(byte[] csvData) throws IOException {
        final List<SimpleImportRoom> dataModel = new ArrayList<>();

        final MappingIterator<String[]> it = readCsvValues(csvData);
        while (it.hasNext()) {
            final String[] csvRow = it.next();
            final String roomNumber = csvRow[0];
            final List<String> persons = Arrays.asList(csvRow).subList(1, csvRow.length);

            dataModel.add(new SimpleImportRoom(roomNumber, persons.stream()
                    .filter(v -> v.length() > 0)
                    .collect(Collectors.toList())));
        }

        return dataModel;
    }

    private MappingIterator<String[]> readCsvValues(byte[] csvData) throws IOException {
        final CsvMapper mapper = new CsvMapper();
        mapper.enable(CsvParser.Feature.WRAP_AS_ARRAY);
        return mapper.readerFor(String[].class).readValues(csvData);
    }

    private List<Room> convertCsvImportRooms(List<SimpleImportRoom> simpleImportRooms) throws CsvPersonNotValidException {
        return simpleImportRooms.stream()
                .map(SimpleImportRoom::convertToRoom)
                .collect(Collectors.toList());
    }

    private class SimpleImportRoom {

        private final String room;
        private final List<String> persons;

        SimpleImportRoom(String room, List<String> persons) {
            this.room = room;
            this.persons = Collections.unmodifiableList(persons);
        }

        Room convertToRoom() throws CsvPersonNotValidException {
            final List<Person> people = persons.stream()
                    .map(csvPersonToPersonMapper::map)
                    .collect(Collectors.toList());
            return new Room.RoomBuilder(room)
                    .persons(people)
                    .build();
        }

    }
}
