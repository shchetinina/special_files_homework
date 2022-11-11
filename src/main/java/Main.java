import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.util.List;

public class Main {
    static FileParser fileParser = new FileParser();
    static String csvFileName = "data.csv";
    static String xmlFileName = "data.xml";

    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException {
        //Задание 1
        String[] columnNames = {"id", "firstName", "lastName", "country", "age"};
        List<Employee> listFromCSV = fileParser.parseCSV(columnNames, csvFileName);
        String jsonFromCSV = fileParser.listToJson(listFromCSV);
        writeString(jsonFromCSV, "fromCSV");

        //Задание 2
        List<Employee> listFromXml = fileParser.parseXML(xmlFileName);
        String jsonFromXML = fileParser.listToJson(listFromXml);
        writeString(jsonFromXML, "fromXML");

        //Задание 3
        String json = readString("fromXML_new_data.json");
        List<Employee> list = fileParser.jsonToList(json);
        System.out.println(list.toString());
    }

    public static void writeString(String json, String format){
        try (FileWriter fileWriter = new FileWriter(format + "_new_data.json")) {
            fileWriter.write(json);
            fileWriter.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String readString(String fileName){
        StringBuilder stringBuilder = new StringBuilder();
        try(BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName))){
            String nextLine = bufferedReader.readLine();
            while (nextLine != null){
                stringBuilder.append(nextLine);
                nextLine = bufferedReader.readLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuilder.toString();
    }
}
