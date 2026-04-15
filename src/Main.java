import java.sql.*;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner teclado = new Scanner(System.in);
        String url = "jdbc:oracle:thin:@localhost:1521:xe";
        String user = "RIBERA";
        String password = "ribera";
        int opcion;
        do {
            System.out.println("\n==============================");
            System.out.println("         Menu Principal");
            System.out.println("==============================");
            System.out.println("1. Top 5 ciclistas con mejor rendimiento");
            System.out.println("2. Comparativa de equipos");
            System.out.println("3. Etapas 'especiales'");
            System.out.println("0. Salir");
            opcion = teclado.nextInt();
            teclado.nextLine();
            switch (opcion) {
                // ================= Top 5 ciclistas con mejor rendimiento =================
                case 1:
                    int opcionCiclistas;
                    do {
                        System.out.println("\n=====================================");
                        System.out.println("Top 5 ciclistas con mejor rendimiento");
                        System.out.println("=====================================");
                        System.out.println("1. Mostrar la comparativa de rendimiento");
                        System.out.println("0. Volver");
                        System.out.print("Opción: ");

                        opcionCiclistas = teclado.nextInt();
                        teclado.nextLine();

                        switch (opcionCiclistas) {
                            case 1:
                                try (Connection conn = DriverManager.getConnection(url, user, password);
                                     Statement st = conn.createStatement()) {
                                    ResultSet rs = st.executeQuery("select c.nombre as ciclista, e.nombre as equipo, c.nacionalidad as nacionalidad, \n" +
                                            "sum(p.puntos)as total,Round(avg(p.puntos))as promedio, \n" +
                                            "count(distinct p.numero_etapa) as \"numero de etapas\"\n" +
                                            "from ciclista c join participacion p on c.id_ciclista=p.id_ciclista \n" +
                                            "join equipo e on c.id_equipo=e.id_equipo\n" +
                                            "group by c.nombre, e.nombre, c.nacionalidad\n" +
                                            "order by total desc, promedio desc\n" +
                                            "fetch first 5 row only\n");
                                    while (rs.next()) {
                                        System.out.println(
                                                "Ciclista: " + rs.getString("ciclista") +
                                                        " | Equipo: " + rs.getString("equipo") +
                                                        " | País: " + rs.getString("nacionalidad") +
                                                        " | Total: " + rs.getInt("total") +
                                                        " | Promedio: " + rs.getDouble("promedio") +
                                                        " | Etapas: " + rs.getInt("numero de etapas")
                                        );
                                    }
                                } catch (SQLException e) {
                                    System.out.println("ERROR: " + e.getMessage());
                                }
                                break;
                            case 0:
                                System.out.println("Volviendo al menú principal...");
                            break;
                            default:
                                System.out.println("Opción no válida");
                        }

                    } while (opcionCiclistas != 0);

                    break;

                case 2:
                    int opcionEquipos;
                    do {
                        System.out.println("\n==============================");
                        System.out.println("   Comparativa de equipos");
                        System.out.println("==============================");
                        System.out.println("1. Mostrar la comparativa de equipos");
                        System.out.println("0. Volver");
                        System.out.print("Opción: ");

                        opcionEquipos = teclado.nextInt();
                        teclado.nextLine();

                        switch (opcionEquipos) {
                            case 1:
                                try (Connection conn = DriverManager.getConnection(url, user, password);
                                     Statement st = conn.createStatement()) {
                                    ResultSet rs = st.executeQuery("SELECT \n" +
                                            "    e.nombre AS equipo,\n" +
                                            "    e.pais,\n" +
                                            "    COUNT(DISTINCT c.id_ciclista) AS num_ciclistas,\n" +
                                            "    SUM(p.puntos) AS total,\n" +
                                            "    ROUND(AVG(c.edad)) AS edad\n" +
                                            "FROM equipo e\n" +
                                            "JOIN ciclista c ON e.id_equipo = c.id_equipo\n" +
                                            "JOIN participacion p ON c.id_ciclista = p.id_ciclista\n" +
                                            "GROUP BY e.nombre, e.pais\n" +
                                            "HAVING COUNT(DISTINCT c.id_ciclista) <= (\n" +
                                            "    SELECT MAX(puntos)\n" +
                                            "    FROM participacion\n" +
                                            ")\n" +
                                            "ORDER BY total DESC");
                                    while (rs.next()) {
                                        System.out.println(
                                                "Ciclista: " + rs.getString("ciclista") +
                                                        " | Equipo: " + rs.getString("equipo") +
                                                        " | País: " + rs.getString("nacionalidad") +
                                                        " | Total: " + rs.getInt("total") +
                                                        " | Promedio: " + rs.getDouble("promedio") +
                                                        " | Etapas: " + rs.getInt("numero de etapas")
                                        );
                                    }
                                } catch (SQLException e) {
                                    System.out.println("ERROR: " + e.getMessage());
                                }
                            break;
                            case 0:
                                System.out.println("Volviendo al menú principal...");
                            break;
                            default:
                                System.out.println("Opción no válida");
                        }

                    } while (opcionEquipos != 0);

                    break;

                case 3:
                    int opcionEtapas;
                    do {
                        System.out.println("\n==============================");
                        System.out.println("     Etapas 'Especiales'");
                        System.out.println("==============================");
                        System.out.println("1. Mostrar las etapas especiales");
                        System.out.println("0. Volver");
                        System.out.print("Opción: ");

                        opcionEtapas = teclado.nextInt();
                        teclado.nextLine();

                        switch (opcionEtapas) {
                                case 1:

                                break;
                            case 0:
                                System.out.println("Volviendo al menú principal...");
                                break;
                            default:
                                System.out.println("Opción no válida");
                        }

                    } while (opcionEtapas != 0);

                    break;

                case 0:
                    System.out.println("Saliendo del programa...");
                    break;

                default:
                    System.out.println("Opción no válida");
            }
        } while (opcion != 0);
        teclado.close();
    }
}