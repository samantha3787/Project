package project.data;

import project.common.ParkingViolation;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ParkingViolationData {

    private final List<ParkingViolation> violations = new ArrayList<>();
    private final Map<String, List<ParkingViolation>> violationsByZip = new HashMap<>();

    public ParkingViolationData() {
    }

    public static ParkingViolationData fromCsvFile(String filename) throws IOException {
        ParkingViolationData data = new ParkingViolationData();
        data.readCsv(filename);
        return data;
    }

    public static ParkingViolationData fromJsonFile(String filename) throws IOException {
        ParkingViolationData data = new ParkingViolationData();
        data.readJson(filename);
        return data;
    }

    private void readCsv(String filename) throws IOException {
        File file = new File(filename);
        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts = line.split(",", -1);
                if (parts.length < 7) {
                    continue;
                }

                String timestamp = parts[0];
                Integer fine = parseInteger(parts[1]);
                String description = parts[2];
                String vehicleId = parts[3];
                String state = parts[4];
                String violationId = parts[5];
                String zip = processZipCode(parts[6]);

                if (fine == null) {
                    continue;
                }

                addViolation(new ParkingViolation(
                        timestamp, fine, description, vehicleId, state, violationId, zip));
            }
        }
    }

    private void readJson(String filename) throws IOException {
        try (FileReader reader = new FileReader(filename)) {
            Object root = new JSONParser().parse(reader);
            if (!(root instanceof JSONArray)) {
                throw new IOException();
            }
            JSONArray array = (JSONArray) root;

            for (Object o : array) {
                if(!(o instanceof JSONObject)) {
                    throw new IOException();
                }
                JSONObject obj = (JSONObject) o;

                String timestamp = (String) obj.get("date");
                Number fineNumber = (Number) obj.get("fine");
                if (fineNumber == null) {
                    continue;
                }
                int fine = fineNumber.intValue();

                String description = (String) obj.get("violation");
                String vehicleId = (String) obj.get("plate_id");
                String state = (String) obj.get("state");
                String violationId = String.valueOf(obj.get("ticket_number"));
                String zip = processZipCode((String) obj.get("zip_code"));

                addViolation(new ParkingViolation(
                        timestamp, fine, description, vehicleId, state, violationId, zip));
            }
        } catch (ParseException e) {
            throw new IOException("Invalid JSON in parking file: " + filename, e);
        }
    }

    private void addViolation(ParkingViolation v) {
        violations.add(v);
        String zip = v.getZipCode();
        if (zip != null) {
            violationsByZip.computeIfAbsent(zip, z -> new ArrayList<>()).add(v);
        }
    }

    private Integer parseInteger(String s) {
        if (s == null) {
            return null;
        }

        s = s.trim();
        if (s.isEmpty()) {
            return null;
        }

        try {
            double d = Double.parseDouble(s);
            return (int) Math.round(d);
        } catch (NumberFormatException e) {
            return null;
        }
    }

    private String processZipCode(String zip) {
        if (zip == null) {
            return null;
        }
        zip = zip.trim();
        if (zip.isEmpty()) {
            return null;
        }
        return zip.length() >= 5 ? zip.substring(0, 5) : zip;
    }

    public List<ParkingViolation> getViolations() {
        return violations;
    }

    public List<ParkingViolation> getViolationsByZip(String zip) {
        if (zip == null || zip.isEmpty()) {
            return Collections.emptyList();
        }

        List<ParkingViolation> list = violationsByZip.get(zip);
        return list == null ? Collections.emptyList() : list;
    }
}







