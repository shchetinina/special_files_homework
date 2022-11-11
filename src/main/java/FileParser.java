import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import com.opencsv.CSVReader;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.CsvToBean;
import com.opencsv.bean.CsvToBeanBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class FileParser {
    public List<Employee> parseCSV(String[] columnNames, String fileName){
        ColumnPositionMappingStrategy<Employee> strategy = new ColumnPositionMappingStrategy<>();
        strategy.setColumnMapping(columnNames);
        strategy.setType(Employee.class);

        try (CSVReader csvReader = new CSVReader(new FileReader(fileName))){
            CsvToBean<Employee> csvToBean = new CsvToBeanBuilder<Employee>(csvReader)
                    .withMappingStrategy(strategy)
                    .build();
            return csvToBean.parse();
        }catch (IOException e){
            return null;
        }
    }

    public List<Employee> parseXML(String fileName) throws ParserConfigurationException, IOException, SAXException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(fileName);

        List<Employee> employeeList = new ArrayList<>();
        Node root = document.getDocumentElement();
        NodeList nodeList = root.getChildNodes();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node child = nodeList.item(i);
            if (child.getNodeType() == Node.ELEMENT_NODE){
                Employee employee = new Employee();
                NodeList employeeParams = child.getChildNodes();
                for (int j = 0; j < employeeParams.getLength(); j++) {
                    Node currentNode = employeeParams.item(j);
                    switch (currentNode.getNodeName()){
                        case "id": employee.id = Long.parseLong(currentNode.getTextContent()); break;
                        case "firstName": employee.firstName = currentNode.getTextContent(); break;
                        case "lastName": employee.lastName = currentNode.getTextContent(); break;
                        case "country": employee.country = currentNode.getTextContent(); break;
                        case "age": employee.age = Integer.parseInt(currentNode.getTextContent()); break;
                        default: break;
                    }
                }
                employeeList.add(employee);
            }
        }
        return employeeList;
    }

    public String listToJson(List<Employee> list){
        Type listType = new TypeToken<List<Employee>>() {}.getType();
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        return gson.toJson(list, listType);
    }

    public List<Employee> jsonToList(String json){
        List<Employee> employeeList = new ArrayList<>();
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();

        JsonArray parser = JsonParser.parseString(json).getAsJsonArray();
        for (var item : parser){
            employeeList.add(gson.fromJson(item, Employee.class));
        }
        return employeeList;
    }
}
