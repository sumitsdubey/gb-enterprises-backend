package in.hexarise.gb_enterprises.crm.util;

import com.opencsv.CSVReader;
import in.hexarise.gb_enterprises.crm.exception.AppException;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

@Component
public class LeadExcelParser {

    public record ParsedLead(String name, String mobile, String address, String load) {}

    public List<ParsedLead> parse(MultipartFile file) {
        String name = file.getOriginalFilename();
        if (name == null) throw AppException.badRequest("Invalid file");

        try {
            if (name.endsWith(".csv")) return parseCsv(file);
            if (name.endsWith(".xlsx") || name.endsWith(".xls")) return parseExcel(file);
            throw AppException.badRequest("Unsupported file type. Use .xlsx or .csv");
        } catch (AppException e) {
            throw e;
        } catch (Exception e) {
            throw AppException.badRequest("Failed to parse file: " + e.getMessage());
        }
    }

    private List<ParsedLead> parseExcel(MultipartFile file) throws Exception {
        List<ParsedLead> leads = new ArrayList<>();
        try (Workbook wb = new XSSFWorkbook(file.getInputStream())) {
            Sheet sheet = wb.getSheetAt(0);
            for (int i = 1; i <= sheet.getLastRowNum(); i++) { // skip header row
                Row row = sheet.getRow(i);
                if (row == null) continue;
                String nm = cellStr(row.getCell(0));
                String mob = cellStr(row.getCell(1));
                String addr = cellStr(row.getCell(2));
                String load = cellStr(row.getCell(3));
                if (!nm.isBlank() && !mob.isBlank())
                    leads.add(new ParsedLead(nm, mob, addr, load));
            }
        }
        return leads;
    }

    private List<ParsedLead> parseCsv(MultipartFile file) throws Exception {
        List<ParsedLead> leads = new ArrayList<>();
        try (CSVReader reader = new CSVReader(new InputStreamReader(file.getInputStream()))) {
            reader.readNext(); // skip header
            String[] row;
            while ((row = reader.readNext()) != null) {
                if (row.length >= 2 && !row[0].isBlank() && !row[1].isBlank())
                    leads.add(new ParsedLead(row[0].trim(), row[1].trim(), row.length > 2 ? row[2].trim() : "", row.length > 3 ? row[3].trim() : ""));
            }
        }
        return leads;
    }

    private String cellStr(Cell cell) {
        if (cell == null) return "";
        return switch (cell.getCellType()) {
            case STRING -> cell.getStringCellValue().trim();
            case NUMERIC -> String.valueOf((long) cell.getNumericCellValue());
            default -> "";
        };
    }
}
