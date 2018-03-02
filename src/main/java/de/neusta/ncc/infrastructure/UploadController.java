package de.neusta.ncc.infrastructure;

import de.neusta.ncc.application.RoomImportService;
import de.neusta.ncc.infrastructure.mapper.exception.CsvPersonNotValidException;
import de.neusta.ncc.application.validator.exception.LdapUserIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomIsNotUniqueException;
import de.neusta.ncc.application.validator.exception.RoomNumberNotValidException;
import de.neusta.ncc.domain.Room;
import de.neusta.ncc.infrastructure.dto.DefaultSpringErrorDto;
import de.neusta.ncc.infrastructure.dto.ErrorMessageDto;
import de.neusta.ncc.infrastructure.dto.ImportResultDto;
import de.neusta.ncc.infrastructure.mapper.CsvImportMapper;
import de.neusta.ncc.infrastructure.mapper.exception.EmptyFileImportException;
import de.neusta.ncc.infrastructure.mapper.exception.FileImportException;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
public class UploadController {

    private final CsvImportMapper csvImportMapper;
    private final RoomImportService roomImportService;

    @Autowired
    public UploadController(CsvImportMapper csvImportMapper, RoomImportService roomImportService) {
        this.csvImportMapper = csvImportMapper;
        this.roomImportService = roomImportService;
    }

    @ApiOperation(value = "Imports a csv file to internal data storage.",
            notes = "Csv file should have the following syntax:"
                    + " Room number, person1, person2, person3, ..."
                    + " where person should be like:"
                    + " title firstname extraname addition lastname (ldapUserName)")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = ImportResultDto.class),
            @ApiResponse(code = 400, message = "Import failure", response = ErrorMessageDto.class),
            @ApiResponse(code = 405, message = "Wrong method type", response = DefaultSpringErrorDto.class),
            @ApiResponse(code = 500, message = "Internal server error", response = DefaultSpringErrorDto.class)
    })
    @RequestMapping(value = "/api/import", method = RequestMethod.POST, consumes = {"multipart/form-data"}, produces = {"application/json"})
    public ResponseEntity uploadCsv(@RequestParam MultipartFile file) {
        try {
            final List<Room> rooms = csvImportMapper.map(file);
            roomImportService.importRooms(rooms);

            return ResponseEntity.ok(new ImportResultDto(file.getOriginalFilename()));
        } catch (FileImportException | EmptyFileImportException e) {
            return buildError(0, e.getMessage());
        } catch (RoomIsNotUniqueException e) {
            return buildError(2, e.getMessage());
        } catch (LdapUserIsNotUniqueException e) {
            return buildError(3, e.getMessage());
        } catch (CsvPersonNotValidException e) {
            return buildError(4, e.getMessage());
        } catch (RoomNumberNotValidException e) {
            return buildError(6, e.getMessage());
        }
    }

    private ResponseEntity<ErrorMessageDto> buildError(int errorCode, String errorMessage) {
        final ErrorMessageDto errorMessageDto1 = new ErrorMessageDto(errorCode, errorMessage);
        return new ResponseEntity<>(errorMessageDto1, HttpStatus.BAD_REQUEST);
    }
}
