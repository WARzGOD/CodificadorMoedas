import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class Principal {
    private static final String API_KEY = "SUA_CHAVE_DE_API";
    private static final String API_URL = "https://api.exchangeratesapi.io/latest";

    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            exibirMenu();
            try {
                int opcao = Integer.parseInt(reader.readLine());
                if (opcao == 7) {
                    System.out.println("Saindo...");
                    break;
                } else if (opcao < 1 || opcao > 6) {
                    System.out.println("Opção inválida.");
                    continue;
                }
                System.out.print("Digite o valor a ser convertido: ");
                double valor = Double.parseDouble(reader.readLine());

                String base, destino;
                switch (opcao) {
                    case 1:
                        base = "USD"; destino = "EUR";
                        break;
                    case 2:
                        base = "EUR"; destino = "USD";
                        break;
                    case 3:
                        base = "BRL"; destino = "USD";
                        break;
                    case 4:
                        base = "USD"; destino = "BRL";
                        break;
                    case 5:
                        base = "EUR"; destino = "BRL";
                        break;
                    case 6:
                        base = "BRL"; destino = "EUR";
                        break;
                    default:
                        base = destino = "";
                }

                double taxa = obterTaxaConversao(base, destino);
                double resultado = valor * taxa;
                System.out.printf("Resultado: %.2f %s\n", resultado, destino);

            } catch (NumberFormatException | IOException e) {
                System.out.println("Entrada inválida.");
            }
        }
    }

    private static void exibirMenu() {
        System.out.println("Conversor de Moedas");
        System.out.println("Escolha a opção:");
        System.out.println("1. USD para EUR");
        System.out.println("2. EUR para USD");
        System.out.println("3. BRL para USD");
        System.out.println("4. USD para BRL");
        System.out.println("5. EUR para BRL");
        System.out.println("6. BRL para EUR");
        System.out.println("7. Sair");
        System.out.print("Opção: ");
    }

    private static double obterTaxaConversao(String base, String destino) {
        try {
            URL url = new URL(API_URL + "?base=" + base + "&symbols=" + destino);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
            reader.close();

            String jsonResponse = response.toString();
            String rateString = jsonResponse.substring(jsonResponse.indexOf(destino) + 5, jsonResponse.indexOf("}"));
            return Double.parseDouble(rateString);

        } catch (IOException e) {
            e.printStackTrace();
            return 0.0;
        }
    }
}
