package org.acabativa.rc.patrimonio.test;

import java.util.Arrays;
import java.util.List;

import org.acabativa.rc.patrimonio.entity.Property;
import org.springframework.web.client.RestTemplate;

public class BasicPropertyTest {

    private static final String BASE_URL = "http://renovacampo.acabativa.com:8080/api/v1/property";

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        
        System.out.println("Iniciando a demo com a API do Renovacampo...");

        // 1. Cadastrar 3 propriedades
        // Property p1 = new Property(null, "Fazenda Boa Vista", "Produção de café",
        //         150, 100, "Rural", "Rodovia BR-262, km 50", "MG", "Manhuaçu", -20.2589, -42.0286);
        // Property p2 = new Property(null, "Sítio das Palmeiras", "Cultivo de hortaliças",
        //         20, 15, "Rural", "Estrada Municipal, s/n", "MG", "Lavras", -21.2456, -45.0003);
        // Property p3 = new Property(null, "Chácara São José", "Área de lazer com lago",
        //         10, 8, "Chácara", "Zona Rural", "MG", "Ouro Preto", -20.3858, -43.5033);

        // Long id1 = restTemplate.postForObject(BASE_URL, p1, Long.class);
        // Long id2 = restTemplate.postForObject(BASE_URL, p2, Long.class);
        // Long id3 = restTemplate.postForObject(BASE_URL, p3, Long.class);

        // // 2. Buscar e mostrar todas as propriedades
        // Property[] props = restTemplate.getForObject(BASE_URL, Property[].class);
        // System.out.println("\n Lista de propriedades:");
        // List<Property> all = Arrays.asList(props != null ? props : new Property[0]);
        // all.forEach(System.out :: println);

        // 3. Deletar a primeira
        restTemplate.delete(BASE_URL + "/" + 1);

        // // 4. Mostrar lista atualizada
        // props = restTemplate.getForObject(BASE_URL, Property[].class);
        // all = Arrays.asList(props != null ? props : new Property[0]);
        // System.out.println("\n Lista atualizada de propriedades:");
        // all.forEach(System.out::println);
    }

    

}
