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

public class ParkingViolationData implements Iterable<ParkingViolation> {

    private final List<ParkingViolation> violations = new ArrayList<>();
    private final Map<String, List<ParkingViolation>> violationsByZip = new HashMap<>();

    public ParkingViolationData() {
    }

    public static ParkingViolationData fromCsvFile(String filename) throws IOException {
        if(filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("CSV parking file name cannot be null or empty.");
        }
        ParkingViolationData data = new ParkingViolationData();
        data.readCsv(filename);
        return data;
    }

    public static ParkingViolationData fromJsonFile(String filename) throws IOException {
        if(filename == null || filename.trim().isEmpty()) {
            throw new IllegalArgumentException("JSON parking file name cannot be null or empty.");
        }
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
                throw new IOException("Invalid JSONArray");
            }
            JSONArray array = (JSONArray) root;

            for (Object o : array) {
                if(!(o instanceof JSONObject)) {
                    throw new IOException("Invalid JSONObject");
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
        } catch (org.json.simple.parser.ParseException e) {
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

    @Override
    public Iterator<ParkingViolation> iterator() {
        return violations.iterator();
    }

    public Iterable<ParkingViolation> paViolationsWithZipIterator() {
        return () -> new Iterator<ParkingViolation>() {
            private int index = 0;
            private ParkingViolation nextViolation = null;

            @Override
            public boolean hasNext() {
                if(nextViolation != null) {
                    return true;
                }
                while(index < violations.size()) {
                    ParkingViolation v = violations.get(index);
                    index++;
                    if(isValid(v)) {
                        nextViolation = v;
                        return true;
                    }
                }
                return false;
            }

            @Override
            public ParkingViolation next() {
                if(!hasNext()) {
                    throw new NoSuchElementException();
                }
                ParkingViolation v = nextViolation;
                nextViolation = null;
                return v;
            }

            private boolean isValid(ParkingViolation v) {
                return "PA".equals(v.getState()) && v.getZipCode() != null && !v.getZipCode().isEmpty();
            }

        };

    }


}







