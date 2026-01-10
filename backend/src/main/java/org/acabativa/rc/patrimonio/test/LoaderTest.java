package org.acabativa.rc.patrimonio.test;



import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.acabativa.rc.patrimonio.entity.Property;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.springframework.web.client.RestTemplate;

public class LoaderTest {

    private static final String BASE_URL = "http://renovacampo.acabativa.com:8080/api/v1/property";

    public static List<Property> readPropertiesFromCsv(String filePath) {
        List<Property> properties = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            boolean isFirstLine = true;

            while ((line = br.readLine()) != null) {
                if (isFirstLine) {
                    isFirstLine = false;
                    continue; // pula o cabeçalho
                }
                System.out.print("Line: " + line);
                String[] values = line.split("\t");

                // Remove aspas e espaços
                for (int i = 0; i < values.length; i++) {
                    values[i] = values[i].replace("\"", "").trim();
                }
                if(values.length>=9){
                    System.out.print("values[8]: " + values[8]);
                }
                // Cria a instância de Property
                Property property = new Property(
                        null,
                        values[0],
                        values[1],
                        (int)Double.parseDouble(values[2]),
                        null,
                        values[4],
                        values[5],
                        values[6],
                        ((values.length>=8)?values[7]:null),
                        ((values.length>=9&&values[8]!=null&&!values[8].isEmpty())?Double.parseDouble(values[8]):null),
                        ((values.length>=10&&values[9]!=null&&!values[9].isEmpty())?Double.parseDouble(values[9]):null)
                );

                properties.add(property);
            }

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo CSV: " + e.getMessage());
        }

        return properties;
    }
   

    public static void main(String[] args) throws IOException {
        // Caminho do arquivo
        RestTemplate restTemplate = new RestTemplate();
        List<Property> list = readPropertiesFromCsv("C:\\Users\\mathe\\OneDrive\\Desktop\\fazendasRenovaCampo.txt");
        for (Property property : list) {
            System.out.println(property);
            restTemplate.postForObject(BASE_URL, property, Long.class);
        }
        //RestTemplate restTemplate = new RestTemplate();
        //String aux = restTemplate.getForObject("https://maurolopesfazendas.com.br/Fazendas/3.html", String.class);
        //String aux = restTemplate.getForObject("https://maurolopesfazendas.com.br/4197/FAZ%20208%20-%20Fazenda%20em%20Casa%20Branca%20%E2%80%93%20SP,%20com%20170%20alqs%20(411,4%20hect)%20%7C%20lavoura%20e%20piv%C3%B4.html", String.class);
        
        // Document doc = Jsoup.parse(aux);

        // for (Element element : doc.getAllElements()) {
        //     String tag = element.tagName();
        //     String texto = element.ownText().trim();

        //     // Ignorar elementos vazios
        //     if (texto.isEmpty() && element.attributes().size() == 0) continue;

            
        //     if (!texto.isEmpty()) {
        //         System.out.println("  Texto: " + texto);
        //     }

        //     // ID e classe
        //     if (!element.id().isEmpty()) {
        //         System.out.println("  ID: " + element.id());
        //     }

        //     if (!element.className().isEmpty()) {
        //         System.out.println("  Classe(s): " + element.className());
        //     }

        //     // Atributos gerais
        //     element.attributes().forEach(attr -> {
        //         if (!attr.getKey().equals("id") && !attr.getKey().equals("class")) {
        //             System.out.println("  Atributo: " + attr.getKey() + " = " + attr.getValue());
        //         }
        //     });
        //     System.out.println("");
        
        // }

    }
}
